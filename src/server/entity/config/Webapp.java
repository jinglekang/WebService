package server.entity.config;

import server.entity.config.WebServlet;

import java.util.List;

public class Webapp {
    private String appName;
    private String appDomain;
    private String appRootpath;
    private String appServletPath;
    private String appPort;
    private String appIndex;
    private List<WebServlet> appWebServlet;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppDomain() {
        return appDomain;
    }

    public void setAppDomain(String appDomain) {
        this.appDomain = appDomain;
    }

    public String getAppRootpath() {
        return appRootpath;
    }

    public void setAppRootpath(String appRootpath) {
        this.appRootpath = appRootpath;
    }

    public String getAppServletPath() {
        return appServletPath;
    }

    public void setAppServletPath(String appServletPath) {
        this.appServletPath = appServletPath;
    }

    public String getAppPort() {
        return appPort;
    }

    public void setAppPort(String appPort) {
        this.appPort = appPort;
    }

    public String getAppIndex() {
        return appIndex;
    }

    public void setAppIndex(String appIndex) {
        this.appIndex = appIndex;
    }

    public List<WebServlet> getAppWebServlet() {
        return appWebServlet;
    }

    public void setAppWebServlet(List<WebServlet> appWebServlet) {
        this.appWebServlet = appWebServlet;
    }
}
