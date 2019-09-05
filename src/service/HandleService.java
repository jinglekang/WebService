package service;


import entity.config.Servlet;
import entity.config.Webapp;
import entity.server.Request;
import entity.server.Response;
import servlet.BaseServlet;
import servlet.StaticServlet;

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
            for (Servlet servlet : servlets) {
                List<String> servletUrl = servlet.getServletUrl();
                String url = request.getUrl();
                if (servletUrl.contains(url)) {
                    Class ciz = Class.forName(servlet.getServletClass());
                    BaseServlet baseServlet = (BaseServlet) ciz.newInstance();
                    baseServlet.service(request, response);
                    return;
                }
            }
            StaticServlet servlet = new StaticServlet();
            servlet.service(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.initialization(socket);
    }
}
