import entity.config.Webapp;
import service.IOService;
import service.SocketService;

import java.util.List;

public class Main implements Runnable {
    private Webapp webapp;

    public static void main(String[] args) {
        List<Webapp> webapps = IOService.initXmlConfig();
        if (webapps != null) {
            for (Webapp webapp : webapps) {
                Main main = new Main(webapp);
                Thread t = new Thread(main);
                t.start();
            }
        }
    }

    public Main(Webapp webapp) {
        this.webapp = webapp;
    }

    @Override
    public void run() {
        System.out.println("正在启动服务：" + this.webapp.getAppName());
        SocketService service = new SocketService();
        service.start(this.webapp);
    }
}
