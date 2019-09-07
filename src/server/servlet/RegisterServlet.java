package server.servlet;

import server.entity.http.Request;
import server.entity.http.Response;
import server.util.Utils;

public class RegisterServlet implements BaseServlet {

    @Override
    public void doGet(Request request, Response response) {
        response.getWriter().println(response.initContent(Utils.getHtmlErrorPage(request.toString())));
    }

    @Override
    public void doPost(Request request, Response response) {
        this.doGet(request, response);
    }

    @Override
    public void doPut(Request request, Response response) {
        this.doGet(request,response);
    }

    @Override
    public void doDelete(Request request, Response response) {
        this.doGet(request,response);
    }
}