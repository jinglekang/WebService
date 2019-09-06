package servlet;

import entity.server.Request;
import entity.server.Response;
import service.IOService;
import service.PrintWrite;

public class ClassNotFoundServlet implements BaseServlet {
    @Override
    public void service(Request request, Response response) {
        PrintWrite printWrite = response.getPrintWrite();
        printWrite.write(IOService.getHtmlErrorPage("Servlet Class Not Found"));
    }
}