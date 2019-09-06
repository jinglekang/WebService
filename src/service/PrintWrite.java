package service;

import entity.server.Response;

import java.io.OutputStream;
import java.io.PrintWriter;

public class PrintWrite extends PrintWriter {
    private Response response;

    public PrintWrite(OutputStream out) {
        super(out);
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public void write(String s) {
        String content = response.initContent( s);
        super.write(content);
        this.flush();
        this.close();
    }
}
