package servlet;

import entity.http.Request;
import entity.http.Response;

public interface BaseServlet {
    void doGet(Request request, Response response);

    void doPost(Request request, Response response);

    void doPut(Request request, Response response);

    void doDelete(Request request, Response response);
}
