package servlet;

import entity.server.Request;
import entity.server.Response;

public class LoginServlet implements BaseServlet {
    @Override
    public void service(Request request, Response response) {
        response.getWriter().println(request.toString());
    }
}
