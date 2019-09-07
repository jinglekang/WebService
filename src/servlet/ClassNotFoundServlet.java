package servlet;

import entity.server.Request;
import entity.server.Response;
import service.IOService;

public class ClassNotFoundServlet implements BaseServlet {
    @Override
    public void service(Request request, Response response) {
        response.getWriter().println(IOService.getHtmlErrorPage("Servlet Class Not Found"));
    }
}