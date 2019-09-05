package servlet;

import entity.config.Webapp;
import entity.server.Request;
import entity.server.Response;
import service.IOService;

import java.util.HashMap;
import java.util.Map;

public class HtmlServlet {

    private static Map<String, String> htmlMap = new HashMap<>();

    public void service(Request request, Response response, Webapp webapp) {
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
            html = IOService.readHtml(htmlPath);
            htmlMap.put(htmlPath, html);
            System.out.println("加载文件：" + url);
        }

        if (suffix == null) {
            if (html != null) {
                String content = response.initContent(200, html);
                service.write(response.getOutputStream(), content);
            } else {
                String content = response.initContent(404);
                service.write(response.getOutputStream(), content);
            }
        } else {
            switch (suffix) {
                case "css":
                    response.setContentType("text/css");
                    String cssContent = response.initContent(200, html);
                    service.write(response.getOutputStream(), cssContent);
                    break;
                case "js":
                    response.setContentType("application/x-javascript");
                    String jsContent = response.initContent(200, html);
                    service.write(response.getOutputStream(), jsContent);
                    break;
                case "ico":
                    response.setContentType("image/x-icon");
                    String icoContent = response.initContent(200, html);
                    service.write(response.getOutputStream(), icoContent);
                    break;
                default:
                    String defaultContent = response.initContent(200, html);
                    service.write(response.getOutputStream(), defaultContent);
                    break;
            }
        }
    }
}
