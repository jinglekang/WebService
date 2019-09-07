package entity.server;


import java.io.*;

public class Response {
    // private String contentType = "";
    private String contentType = "text/html";
    private static final String CRLF = "\r\n";
    private static final String BLANK = " ";
    private PrintWriter writer;
    private OutputStream outputStream;

    public Response(OutputStream writer) {
        this.outputStream = writer;
        this.writer = new PrintWriter(writer, true);
    }

    public void pushFile(File file){
        try {
            InputStream is = new FileInputStream(file);
            int len;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) > 0) {
                this.outputStream.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public OutputStream getOutputStream() {
        return outputStream;
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
