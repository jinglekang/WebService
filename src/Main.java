import server.entity.config.Webapp;
import server.service.HandleService;
import server.util.Utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Main implements Runnable {
    private Webapp webapp;

    public static void main(String[] args) {
        List<Webapp> webapps = Utils.initXmlConfig();
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
        String appPort = webapp.getAppPort();
        try {
            int port = Integer.parseInt(webapp.getAppPort());
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                System.out.println("启动成功，开始监听端口：" + port);
                while (true) {
                    try {
                        Socket socket = serverSocket.accept();
                        socket.setKeepAlive(true);
                        System.out.println("监听到请求：" + socket);
                        new Thread(new HandleService(socket, webapp)).start();
                    } catch (IOException e) {
                        System.out.println("监听失败，原因：" + e.toString());
                        return;
                    }
                }
            } catch (IOException e) {
                System.out.println("启动失败，可能是端口被占用");
            }

        } catch (NumberFormatException e) {
            System.out.println("启动失败，端口填写不正确：'" + appPort + "'不是数字格式：" + e.toString());
        }
    }
}
