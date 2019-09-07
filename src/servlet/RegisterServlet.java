package servlet;

import entity.server.Request;
import entity.server.Response;

public class RegisterServlet implements BaseServlet {

    @Override
    public void doGet(Request request, Response response) {
        response.getWriter().println(request.toString());
    }

    @Override
    public void doPost(Request request, Response response) {
        this.doGet(request, response);
    }

    @Override
    public void doPut(Request request, Response response) {

    }

    @Override
    public void doDelete(Request request, Response response) {

    }
}