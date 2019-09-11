package entity.http;

import entity.config.Webapp;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {

    private String url;
    private Webapp webapp;
    private String method;
    private InputStream inputStream;
    private Map<String, List<String>> paramMap;

    public Request(InputStream inputStream) {
        this.inputStream = inputStream;
        this.initRequest();
    }

    @Override
    public String toString() {
        return "Request{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", paramMap=" + paramMap +
                '}';
    }

    private void initRequest() {
        String request = this.readRequest(this.inputStream);
        System.out.println(request);
        if (request != null) {
            String[] header = request.split("\n");
            String[] firstLine = header[0].split(" ");
            this.parseMethod(firstLine);
            this.parseUrl(firstLine);
            this.parseParams(header, firstLine);
        }
    }

    private void parseMethod(String[] firstLine) {
        this.method = firstLine[0].toUpperCase();
    }

    private void parseUrl(String[] firstLine) {
        String urlPath = firstLine[1];
        if (urlPath.contains("?")) {
            this.url = this.decode(urlPath.split("\\?")[0]);
        } else {
            this.url = this.decode(urlPath);
        }
    }

    private void parseParams(String[] header, String[] firstLine) {
        switch (this.method) {
            case "GET":
                String urlPath = firstLine[1];
                if (urlPath.contains("?")) {
                    String params = this.decode(urlPath.split("\\?")[1]);
                    this.parseParamMap(params);
                } else {
                    this.paramMap = new HashMap<>();
                }
                break;
            case "POST":
                String lastLine = header[header.length - 1];
                if (lastLine.contains("=")) {
                    this.parseParamMap(this.decode(lastLine));
                } else {
                    this.paramMap = new HashMap<>();
                }
                break;
            case "PUT":
                System.out.println("PUT请求暂未处理");
                break;
            case "DELETE":
                System.out.println("DELETE请求暂未处理");
                break;
            default:
                System.out.println("不支持的请求方法：" + this.method);
                break;
        }

    }

    public Map<String, List<String>> getParamMap() {
        return paramMap;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getMethod() {
        return method;
    }

    public Webapp getWebapp() {
        return webapp;
    }

    public void setWebapp(Webapp webapp) {
        this.webapp = webapp;
    }

    public String getUrl() {
        return url;
    }

    private void parseParamMap(String params) {

        if (params != null) {

            this.paramMap = new HashMap<>();
            if (params.contains("&")) {
                String[] split = params.split("&");
                for (String param : split) {
                    String[] keyValue = param.split("=");
                    String key = keyValue[0];
                    String value = keyValue[1];
                    if (!paramMap.containsKey(key)) {
                        ArrayList<String> values = new ArrayList<>();
                        values.add(value);
                        paramMap.put(key, values);
                    } else {
                        paramMap.get(key).add(value);
                    }
                }
            } else {
                String[] keyValue = params.split("=");
                String key = keyValue[0];
                String value = keyValue[1];
                ArrayList<String> values = new ArrayList<>();
                values.add(value);
                paramMap.put(key, values);
            }
        }
    }

    private String decode(String param) {
        try {
            return URLDecoder.decode(param, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String readRequest(InputStream is) {
        try {
            byte[] buf = new byte[10485760];
            int read = is.read(buf);
            return new String(buf, 0, read);
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }
}
