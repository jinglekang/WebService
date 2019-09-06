package servlet;

import entity.server.Request;
import entity.server.Response;
import service.PrintWrite;

public class RegisterServlet implements BaseServlet {
    public void service(Request request, Response response) {
        PrintWrite printWrite = response.getPrintWrite();
        printWrite.write(request.toString());
    }
}