package server.service;


import server.entity.config.WebServlet;
import server.entity.config.Webapp;
import server.entity.http.Request;
import server.entity.http.Response;
import server.servlet.BaseServlet;
import server.servlet.StaticServlet;
import server.util.Utils;

import java.io.IOException;
import java.net.Socket;
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
            boolean isStatic = false;
            String servletClass = null;


            System.out.println(webServlets);

            for (WebServlet webServlet : webServlets) {
                List<String> servletUrl = webServlet.getServletUrl();
                String url = request.getUrl();
                if (servletUrl.contains(url)) {
                    isStatic = true;
                    servletClass = webServlet.getServletClass();
                    break;
                }
            }

            if (isStatic) {
                try {
                    Class ciz = Class.forName(servletClass);
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
