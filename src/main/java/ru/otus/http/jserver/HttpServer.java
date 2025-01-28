package ru.otus.http.jserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    private int port;
    private Dispatcher dispatcher;

    public HttpServer(int port) {
        this.port = port;
        this.dispatcher = new Dispatcher();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту: " + port);
            try (Socket socket = serverSocket.accept()) {
                System.out.println("Подключился новый клиент");
                byte[] buffer = new byte[8192];
                int n = socket.getInputStream().read(buffer);
                HttpRequest request = new HttpRequest(new String(buffer, 0, n));
                request.info(true);
                dispatcher.execute(request, socket.getOutputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
