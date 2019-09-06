package servlet;

import entity.config.Webapp;
import entity.server.Request;
import entity.server.Response;
import service.IOService;

import java.util.HashMap;
import java.util.Map;

public class StaticServlet implements BaseServlet {

    private static Map<String, String> htmlMap = new HashMap<>();

    @Override
    public void service(Request request, Response response) {
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
                html = IOService.readStaticSource(htmlPath);
                if (html == null) {
                    html = IOService.readStaticSource(appRootpath + "/404.html");
                }
                htmlMap.put(htmlPath, html);
                System.out.println("加载文件：" + url);
            }

            if (suffix == null) {
                response.getPrintWrite().write(html);
            } else {
                switch (suffix) {
                    case "css":
                        response.setContentType("text/css");
                        response.getPrintWrite().write(html);
                        break;
                    case "js":
                        response.setContentType("application/x-javascript");
                        response.getPrintWrite().write(html);
                        break;
                    case "ico":
                        response.setContentType("image/x-icon");
                        response.getPrintWrite().write(html);
                        break;
                    default:
                        response.getPrintWrite().write(html);
                        break;
                }
            }
        } else {
            response.getPrintWrite().write(IOService.readStaticSource(appRootpath + "/404.html"));
        }
    }
}
