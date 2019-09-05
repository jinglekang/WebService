package servlet;

import entity.server.Request;
import entity.server.Response;

public class LoginServlet implements BaseServlet {
    public void service(Request request, Response response) {
        String string = request.getContent();
        response.printAll(200, "Hello,World!");
    }
}