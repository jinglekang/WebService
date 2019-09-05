package servlet;

import entity.server.Request;
import entity.server.Response;

public interface BaseServlet {
    void service(Request request, Response response);
}
