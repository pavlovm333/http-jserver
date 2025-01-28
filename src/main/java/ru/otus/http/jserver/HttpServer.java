package ru.otus.http.jserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private int port;
    private Dispatcher dispatcher;

    public HttpServer(int port) {
        this.port = port;
        this.dispatcher = new Dispatcher();
    }

    public void start() {
        ExecutorService serv = Executors.newFixedThreadPool(5);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту: " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                serv.execute(() -> {
                    try {
                        System.out.println("Подключился новый клиент в потоке " + Thread.currentThread().getName());
                        byte[] buffer = new byte[8192];
                        int n = socket.getInputStream().read(buffer);
                        HttpRequest request = new HttpRequest(new String(buffer, 0, n));
                        request.info(true);
                        dispatcher.execute(request, socket.getOutputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            serv.shutdown();
        }
    }
}
