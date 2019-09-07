package service;


import entity.config.Servlet;
import entity.config.Webapp;
import entity.server.Request;
import entity.server.Response;
import servlet.BaseServlet;
import servlet.ClassNotFoundServlet;
import servlet.StaticServlet;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class HandleService implements Runnable {

    private Socket socket;
    private Webapp webapp;

    HandleService(Socket socket, Webapp webapp) {
        this.socket = socket;
        this.webapp = webapp;
    }

    private void initialization(Socket socket) {
        try {
            Request request = new Request(socket.getInputStream());
            request.setWebapp(this.webapp);
            Response response = new Response(socket.getOutputStream());
            List<Servlet> servlets = webapp.getAppServlet();
            boolean isStatic = false;
            String servletClass = null;
            for (Servlet servlet : servlets) {
                List<String> servletUrl = servlet.getServletUrl();
                String url = request.getUrl();
                if (servletUrl.contains(url)) {
                    isStatic = true;
                    servletClass = servlet.getServletClass();
                    break;
                }
            }

            if (isStatic) {
                try {
                    Class ciz = Class.forName(servletClass);
                    BaseServlet servlet = (BaseServlet) ciz.newInstance();
                    servlet.service(request, response);
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    System.out.println("Servlet加载失败，原因：" + e.toString());
                    ClassNotFoundServlet servlet = new ClassNotFoundServlet();
                    servlet.service(request, response);
                }
            } else {
                StaticServlet servlet = new StaticServlet();
                servlet.service(request, response);
            }
            this.socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.initialization(socket);
    }
}
