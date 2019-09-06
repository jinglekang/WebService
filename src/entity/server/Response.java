package entity.server;

import service.PrintWrite;

import java.io.OutputStream;

public class Response {
    // private String contentType = "";
    private String contentType = "text/html";
    private static final String CRLF = "\r\n";
    private static final String BLANK = " ";
    private PrintWrite printWrite;

    public Response(OutputStream printWrite) {
        this.printWrite = new PrintWrite(printWrite);
        this.printWrite.setResponse(this);
    }

    public PrintWrite getPrintWrite() {
        return printWrite;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String initContent(String body) {
        return "HTTP/1.1" + BLANK + "200" + BLANK + "OK" + CRLF + "Content-Type:" + this.contentType + ";charset=UTF-8" + CRLF + CRLF + body;
    }
}
