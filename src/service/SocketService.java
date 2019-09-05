package service;

import entity.config.Webapp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketService {
    private ServerSocket serverSocket;

    private void receive(Webapp webapp) {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                socket.setKeepAlive(true);
                System.out.println("监听到请求：" + socket);
                HandleService service = new HandleService(socket, webapp);
                Thread t = new Thread(service);
                t.start();
            } catch (IOException e) {
                System.out.println("监听失败，可能是端口被占用");
                return;
            }
        }
    }

    public void start(Webapp webapp) {
        try {
            String appPort = webapp.getAppPort();
            try {
                int port = Integer.parseInt(webapp.getAppPort());
                serverSocket = new ServerSocket(port);
                System.out.println("启动成功，开始监听端口：" + port);
                this.receive(webapp);
            } catch (NumberFormatException e) {
                System.out.println("启动失败，端口填写不正确：'" + appPort + "'不是数字格式：" + e.toString());
            }
        } catch (IOException e) {
            System.out.println("启动失败，原因：" + e.toString());
        }
    }
}
