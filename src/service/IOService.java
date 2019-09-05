package service;

import entity.config.Servlet;
import entity.config.Webapp;
import entity.exception.ConfigException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class IOService {

    /**
     * 静态方法，直接从根目录读取配置文件
     *
     * @return 返回配置文件实体类对象
     */
    public static List<Webapp> initXmlConfig() {
        try {
            try {
                // Service对象数组，为了可以加载多个服务
                List<Webapp> webapps = new ArrayList<>();

                // 创建Dom解析器
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();

                InputStream is = ClassLoader.getSystemResourceAsStream("web.xml");
                if (is != null) {
                    Document document = builder.parse(is);
                    Element rootElement = document.getDocumentElement();
                    NodeList xmlWebappList = rootElement.getElementsByTagName("webapp");
                    for (int a = 0; a < xmlWebappList.getLength(); a++) {
                        Element xmlWebapp = (Element) xmlWebappList.item(a);
                        Element xmlApp = (Element) xmlWebapp.getElementsByTagName("app").item(0);
                        Element xmlAppName = (Element) xmlApp.getElementsByTagName("name").item(0);
                        Element xmlAppDomain = (Element) xmlApp.getElementsByTagName("domain").item(0);
                        Element xmlPort = (Element) xmlApp.getElementsByTagName("port").item(0);
                        Element xmlAppIndex = (Element) xmlApp.getElementsByTagName("index").item(0);
                        Element xmlRootpath = (Element) xmlApp.getElementsByTagName("rootPath").item(0);
                        Element xmlServletPath = (Element) xmlApp.getElementsByTagName("servletPath").item(0);

                        Webapp webapp = new Webapp();
                        webapp.setAppName(throwException(xmlAppName, "Name is null").getTextContent());
                        webapp.setAppDomain(throwException(xmlAppDomain, "Domain is null").getTextContent());
                        webapp.setAppPort(throwException(xmlPort, "Port is null").getTextContent());
                        webapp.setAppRootpath(throwException(xmlRootpath, "Root Path is null").getTextContent());
                        webapp.setAppServletPath(throwException(xmlServletPath, "Servlet Path is null").getTextContent());
                        webapp.setAppIndex(throwException(xmlAppIndex, "Index is null").getTextContent());

                        List<Servlet> servlets = new ArrayList<>();
                        NodeList xmlServlets = (xmlWebapp.getElementsByTagName("servlet"));
                        for (int s = 0; s < xmlServlets.getLength(); s++) {
                            Element xmlServlet = (Element) xmlServlets.item(s);
                            Element xmlServletName = (Element) xmlServlet.getElementsByTagName("name").item(0);
                            Element xmlServletClass = (Element) xmlServlet.getElementsByTagName("class").item(0);

                            Servlet servlet = new Servlet();
                            servlet.setServletName(throwException(xmlServletName, "ServletName is null").getTextContent());
                            servlet.setServletClass(throwException(xmlServletClass, "ServletClass is null").getTextContent());

                            // 多映射实体对象
                            List<String> mappings = new ArrayList<>();
                            Element xmlMappings = (Element) xmlServlet.getElementsByTagName("mapping").item(0);
                            // 开始解析一对多映射
                            NodeList xmlUrls = xmlMappings.getElementsByTagName("url");
                            for (int m = 0; m < xmlUrls.getLength(); m++) {
                                Element xmlUrl = (Element) xmlUrls.item(m);
                                mappings.add(throwException(xmlUrl, "Url is null").getTextContent());
                            }
                            servlet.setServletUrl(mappings);
                            servlets.add(servlet);
                        }
                        webapp.setAppServlet(servlets);
                        webapps.add(webapp);
                    }
                    return webapps;
                } else {
                    System.out.println("配置文件未找到，程序退出");
                    return null;
                }
            } catch (ConfigException e) {
                e.printStackTrace();
                System.out.println("读取配置文件异常，程序退出");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("未处理异常，程序退出");
            return null;
        }
    }

    private static Element throwException(Element ele, String exc) throws ConfigException {
        if (ele == null || ele.getTextContent().length() == 0) {
            throw new ConfigException(exc);
        } else {
            return ele;
        }
    }

    public static String readStaticSource(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                StringBuilder builder = new StringBuilder();
                byte[] bytes = new byte[1024];
                int len;
                while ((len = fis.read(bytes)) != -1) {
                    builder.append(new String(bytes, 0, len));
                }
                return builder.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }
}
