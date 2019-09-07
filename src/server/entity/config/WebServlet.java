package server.entity.config;

import java.util.List;

public class WebServlet {
    private String servletName;
    private String servletClass;
    private List<String> servletUrl;

    public WebServlet() {
    }

    public String getServletName() {
        return servletName;
    }

    public void setServletName(String servletName) {
        this.servletName = servletName;
    }

    public String getServletClass() {
        return servletClass;
    }

    public void setServletClass(String servletClass) {
        this.servletClass = servletClass;
    }

    public List<String> getServletUrl() {
        return servletUrl;
    }

    public void setServletUrl(List<String> servletUrl) {
        this.servletUrl = servletUrl;
    }
}
