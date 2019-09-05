package entity.server;

import java.io.OutputStream;

public class Response {
    private String contentType = "text/html";
    private String charset = "UTF-8";
    private static final String CRLF = "\r\n";
    private static final String BLANK = " ";
    private OutputStream outputStream;

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String initContent(int code, String body) {
        String state = this.getState(code);
        return "HTTP/1.1" + BLANK + code + BLANK + state + CRLF + "Content-Type:" + this.contentType + ";charset=" + this.charset + CRLF + CRLF + body;
    }

    public String initContent(int code) {
        String state = this.getState(code);
        return "HTTP/1.1" + BLANK + code + BLANK + state + CRLF + "Content-Type:" + this.contentType + ";charset=" + this.charset;
    }

    private String getState(int code) {
        switch (code) {
            case 200:
                return "OK";
            case 301:
                return "Moved Permanently";
            case 404:
                return "Not Found";
            case 503:
                return "Service Unavailable";
            case 504:
                return "Gateway Timeout";
            case 505:
                return "HTTP Version Not Supported";
            default:
                return "";
        }
    }
}
