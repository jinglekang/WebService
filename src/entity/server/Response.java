package entity.server;

import java.io.PrintWriter;
import java.net.Socket;

@SuppressWarnings("unused")
public class Response {
    private final String CRLF = "\r\n";
    private final String BLANK = " ";
    private int code;
    private String state;
    private String body = "";
    private String contentType = "text/html";
    private String charset = "UTF-8";
    private Socket socket;

    public Response(Socket socket) {
        this.socket = socket;
    }

    /**
     * 打印给客户端数据
     */
    public void print(String content) {
        try {
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            pw.write(content);
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printAll(int code) {
        this.code = code;
        this.updateState(code);
        this.print(this.getContent());
    }

    public void printAll(int code, String body) {
        this.body = body;
        this.printAll(code);
    }

    public void printAll(int code, String body, String contentType) {
        this.contentType = contentType;
        this.printAll(code, body);
    }

    public void printAll(int code, String body, String contentType, String charset) {
        this.charset = charset;
        this.printAll(code, body, contentType);
    }

    public String getContent() {
        return "HTTP/1.1" + this.BLANK + this.code + this.BLANK + this.state + this.CRLF + "Content-Type:" + this.contentType + ";charset=" + this.charset + this.CRLF + this.CRLF + this.body;
    }

    private void updateState(int code) {
        switch (code) {
            case 200:
                this.state = "OK";
                break;
            case 301:
                this.state = "Moved Permanently";
                break;
            case 404:
                this.state = "Not Found";
                break;
            case 503:
                this.state = "Service Unavailable";
                break;
            case 504:
                this.state = "Gateway Timeout";
                break;
            case 505:
                this.state = "HTTP Version Not Supported";
                break;
        }
    }
}
