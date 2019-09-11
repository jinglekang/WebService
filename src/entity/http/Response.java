package entity.http;


import java.io.*;
import java.util.Date;

public class Response {
    private String contentType = "text/html";
    private static final String CRLF = "\r\n";
    private static final String BLANK = " ";
    private PrintWriter writer;
    private PrintStream stream;
    private OutputStream outputStream;
    private boolean controlOrigin = false;

    public Response(OutputStream writer) {
        this.outputStream = writer;
        this.writer = new PrintWriter(writer, true);
        this.stream = new PrintStream(writer, true);
    }

    public void pushImage(File file, String suffix) {
        switch (suffix) {
            case "jpg":
            case "jpeg":
                this.contentType = "image/jpeg";
                break;
            case "png":
                this.contentType = "image/png";
                break;
            case "gif":
                this.contentType = "image/gif";
                break;
            case "bmp":
                this.contentType = "image/bmp";
                break;
            case "tif":
                this.contentType = "image/tiff";
                break;
            case "ico":
                this.contentType = "image/x-icon";
                break;
            case "webp":
                this.contentType = "image/webp";
                break;
            default:
                this.contentType = "";
                break;
        }
        try {
            stream.print(pushHeader());
            InputStream inputStream = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                stream.write(bytes, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PrintWriter getWriter() {
        writer.print(pushHeader());
        return writer;
    }

    public boolean isControlOrigin() {
        return controlOrigin;
    }

    public void setControlOrigin(boolean controlOrigin) {
        this.controlOrigin = controlOrigin;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    private String pushHeader() {
        String accessControlAllowOrigin;
        if (this.controlOrigin) {
            accessControlAllowOrigin = "";
        } else {
            accessControlAllowOrigin = "Access-Control-Allow-Origin:*" + CRLF;
        }
        return "HTTP/1.1" + BLANK + "200" + BLANK + "OK" + CRLF +
                "Date:" + new Date() + CRLF +
                "Server:WebService" + CRLF +
                accessControlAllowOrigin +
                "Connection:close" + CRLF +
                "Content-Type:" + this.contentType + ";charset=UTF-8" + CRLF + CRLF;
    }
}
