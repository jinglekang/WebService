package servlet;

import entity.server.Request;
import entity.server.Response;

import java.io.PrintWriter;

public class RegisterServlet implements BaseServlet {
    public void service(Request request, Response response) {
        PrintWriter writer = response.getWriter();
        writer.println(request.toString());
    }
}