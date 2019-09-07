package server.servlet;

import server.entity.config.Webapp;
import server.entity.http.Request;
import server.entity.http.Response;
import server.util.Utils;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class StaticServlet implements BaseServlet {

    private static Map<String, String> htmlMap = new HashMap<>();

    @Override
    public void doGet(Request request, Response response) {
        Webapp webapp = request.getWebapp();
        String appRootpath = webapp.getAppRootpath();
        String url = request.getUrl();
        String suffix = null;
        if (url != null) {
            if (url.endsWith("/")) {
                url = url + webapp.getAppIndex();
            } else {
                if (url.contains("/")) {
                    String[] split = url.split("/");
                    String last = split[split.length - 1];
                    if (last.contains(".")) {
                        suffix = last.split("\\.")[1];
                    } else {
                        url = url + "/" + webapp.getAppIndex();
                    }
                }
            }
            String htmlPath = appRootpath + url;

            String html;
            if (htmlMap.containsKey(htmlPath)) {
                html = htmlMap.get(htmlPath);
                System.out.println("缓存文件：" + url);
            } else {
                html = Utils.readStaticSource(htmlPath);
                if (html == null) {
                    html = Utils.readStaticSource(appRootpath + "/404.html");
                }
                htmlMap.put(htmlPath, html);
                System.out.println("加载文件：" + url);
            }

            if (suffix == null || suffix.equals("html")) {
                response.getWriter().println(response.initContent(html));
            } else if (suffix.equals("css")) {
                response.setContentType("text/css");
                PrintWriter writer = response.getWriter();
                writer.println(response.initContent(html));
            } else if (suffix.equals("js")) {
                response.setContentType("application/x-javascript");
                response.getWriter().println(response.initContent(html));
            } else if (Utils.isFile(suffix)) {
                response.pushFile(new File(htmlPath));
            } else {
                response.getWriter().println(html);
            }
        } else {
            response.getWriter().println(Utils.readStaticSource(appRootpath + "/404.html"));
        }
    }

    @Override
    public void doPost(Request request, Response response) {
        this.doGet(request, response);
    }

    @Override
    public void doPut(Request request, Response response) {
        this.doGet(request,response);
    }

    @Override
    public void doDelete(Request request, Response response) {
        this.doGet(request,response);
    }
}
