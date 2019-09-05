package entity.server;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Request {

    private String method;
    private String url = "";
    private Map<String, List<String>> paramMap = new HashMap<>();

    public Request(Socket socket) {
        try {
            String request = this.readRequest(socket.getInputStream(), 1024 * 1024 * 10);
            if (request != null) {
                this.method = request.substring(0, request.indexOf(' '));
                int start = request.indexOf("/");
                int end = request.indexOf("HTTP") - 1;
                String path = request.substring(start, end);
                String requestMethod = method.toUpperCase();
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
                            String decode = this.decode(params);
                            if (decode != null) {
                                this.setParamMap(decode);
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

    public String getContent() {
        return "Request{" +
                "method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", param='" + paramMap + '\'' +
                '}';
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    private void setParamMap(String params) {
        Map<String, List<String>> paramMap = new HashMap<>();
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
        this.paramMap = paramMap;
    }

    private String decode(String param) {
        try {
            return URLDecoder.decode(param, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String readRequest(InputStream is, int len) {
        try {
            byte[] buf = new byte[len];
            StringBuilder builder = new StringBuilder();
            int read = is.read(buf);
            builder.append(new String(buf, 0, read));
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}