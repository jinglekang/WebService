package service;


import entity.config.Webapp;
import entity.server.Request;
import entity.server.Response;
import servlet.HtmlServlet;

import java.net.Socket;

public class HandleService implements Runnable {

    private Socket socket;
    private Webapp webapp;

    HandleService(Socket socket, Webapp webapp) {
        this.socket = socket;
        this.webapp = webapp;
    }

    private void initialization(Socket socket) {
        try {
            Request request = new Request(socket);
            Response response = new Response(socket);
            String url = request.getUrl();

            // if (url.equals("/") || url.contains(".")) {
            HtmlServlet servlet = new HtmlServlet();
            servlet.service(request, response, webapp);
            // } else {
            //     BaseServlet servlet = new LoginServlet();
            //     servlet.service(request, response);
            // }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.initialization(socket);
    }
}
