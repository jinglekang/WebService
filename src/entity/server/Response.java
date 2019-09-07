package entity.server;


import java.io.OutputStream;
import java.io.PrintWriter;

public class Response {
    // private String contentType = "";
    private String contentType = "text/html";
    private static final String CRLF = "\r\n";
    private static final String BLANK = " ";
    private PrintWriter writer;

    public Response(OutputStream writer) {
        this.writer = new PrintWriter(writer, true);
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String initContent(String body) {
        return "HTTP/1.1" + BLANK + "200" + BLANK + "OK" + CRLF + "Content-Type:" + this.contentType + ";charset=UTF-8" + CRLF + CRLF + body;
    }
}
