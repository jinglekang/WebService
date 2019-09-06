package servlet;

import entity.server.Request;
import entity.server.Response;
import service.PrintWrite;

public class LoginServlet implements BaseServlet {
    @Override
    public void service(Request request, Response response) {
        PrintWrite printWrite = response.getPrintWrite();
        printWrite.write(request.toString());
    }
}
