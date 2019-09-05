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
        IOService service = new IOService();
        String url = request.getUrl();
        String suffix = null;
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
        String htmlPath = webapp.getAppRootpath() + url;

        String html;
        if (htmlMap.containsKey(htmlPath)) {
            html = htmlMap.get(htmlPath);
            System.out.println("缓存文件：" + url);
        } else {
            html = IOService.readStaticSource(htmlPath);
            htmlMap.put(htmlPath, html);
            System.out.println("加载文件：" + url);
        }

        if (suffix == null) {
            if (html != null) {
                response.getPrintWrite().write(200, html);
            } else {
                response.getPrintWrite().write(404);
            }
        } else {
            switch (suffix) {
                case "css":
                    response.setContentType("text/css");
                    response.getPrintWrite().write(200, html);
                    break;
                case "js":
                    response.setContentType("application/x-javascript");
                    response.getPrintWrite().write(200, html);
                    break;
                case "ico":
                    response.setContentType("image/x-icon");
                    response.getPrintWrite().write(200, html);
                    break;
                default:
                    response.getPrintWrite().write(200, html);
                    break;
            }
        }
    }
}
