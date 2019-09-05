package entity.config;

import java.util.List;

public class Servlet {
    private String servletName;
    private String servletClass;
    private List<Mapping> servletMapping;

    public Servlet() {
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

    public List<Mapping> getServletMapping() {
        return servletMapping;
    }

    public void setServletMapping(List<Mapping> servletMapping) {
        this.servletMapping = servletMapping;
    }
}
