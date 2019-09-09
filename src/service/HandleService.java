package service;


import entity.config.WebServlet;
import entity.config.Webapp;
import entity.http.Request;
import entity.http.Response;
import servlet.BaseServlet;
import servlet.StaticServlet;
import util.Utils;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class HandleService implements Runnable {

    private Socket socket;
    private Webapp webapp;

    public HandleService(Socket socket, Webapp webapp) {
        this.socket = socket;
        this.webapp = webapp;
    }

    private void initialization() {
        try {
            Request request = new Request(this.socket.getInputStream());
            request.setWebapp(this.webapp);
            Response response = new Response(this.socket.getOutputStream());
            List<WebServlet> webServlets = this.webapp.getAppWebServlet();

            System.out.println(request.toString());
            System.out.println(webapp);

            boolean isServlet = false;
            String servletClass = null;

            for (WebServlet webServlet : webServlets) {
                List<String> servletUrl = webServlet.getServletUrl();
                String url = request.getUrl();
                if (servletUrl.contains(url)) {
                    isServlet = true;
                    servletClass = webServlet.getServletClass();
                    break;
                }
            }

            if (isServlet) {
                try {
                    String servletPath = webapp.getAppServletPath();
                    URL url = new File(servletPath).toURI().toURL();
                    ClassLoader loader = new URLClassLoader(new URL[]{url});
                    Class ciz = Class.forName(servletClass, true, loader);
                    BaseServlet servlet = (BaseServlet) ciz.newInstance();
                    String method = request.getMethod();
                    if ("GET".equals(method)) {
                        servlet.doGet(request, response);
                    } else if ("POST".equals(method)) {
                        servlet.doPost(request, response);
                    } else if ("PUT".equals(method)) {
                        servlet.doPut(request, response);
                    } else if ("DELETE".equals(method)) {
                        servlet.doDelete(request, response);
                    } else {
                        response.getWriter().println(Utils.getHtmlErrorPage("Method Not Supported"));
                    }
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    System.out.println("Servlet加载失败，原因：" + e.toString());
                    response.getWriter().println(Utils.getHtmlErrorPage(e.toString()));
                }
            } else {
                StaticServlet servlet = new StaticServlet();
                servlet.doGet(request, response);
            }
            // 所有请求完成，释放资源
            this.socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.initialization();
    }
}
