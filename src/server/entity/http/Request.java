package server.entity.http;

import server.entity.config.Webapp;

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
        this.paramMap = new HashMap<>();
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
        try {
            String request = this.readRequest(this.inputStream);
            if (request != null) {
                this.method = request.substring(0, request.indexOf(' ')).toUpperCase();
                int start = request.indexOf("/");
                int end = request.indexOf("HTTP") - 1;
                String path = request.substring(start, end);
                String requestMethod = method;
                switch (requestMethod) {
                    case "GET":
                        if (path.contains("?")) {
                            this.url = this.decode(path.substring(0, path.indexOf("?")));
                            String[] split = path.split("\\?");
                            String decode = this.decode(split[1]);
                            if (decode != null) {
                                this.setParamMap(decode);
                            }
                            break;
                        } else {
                            this.url = this.decode(path);
                        }
                        break;
                    case "POST":
                        if (path.contains("?")) {
                            this.url = path.substring(0, path.indexOf("?"));
                        } else {
                            this.url = path;
                        }
                        int last = request.lastIndexOf("\n");
                        if (last != -1) {
                            String params = request.substring(request.lastIndexOf("\n") + 1);
                            if (params.length() > 0 && params.contains("=")) {
                                String decode = this.decode(params);
                                if (decode != null) {
                                    this.setParamMap(decode);
                                }
                            }
                        }
                        break;
                    case "PUT":
                        System.out.println("PUT");
                        break;
                    case "DELETE":
                        System.out.println("DELETE");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Map<String, List<String>> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, List<String>> paramMap) {
        this.paramMap = paramMap;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
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

    private void setParamMap(String params) {
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
            StringBuilder builder = new StringBuilder();
            byte[] buf = new byte[10485760];
            int read = is.read(buf);
            builder.append(new String(buf, 0, read));

            // StringBuilder builder = new StringBuilder();
            // byte[] buffer = new byte[2048];
            // int i;
            // i = inputStream.read(buffer);
            // for (int j = 0; j < i; j++) {
            //     builder.append((char) buffer[j]);
            // }
            return builder.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
