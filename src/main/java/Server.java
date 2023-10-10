import handlers.Handler;
import request.ParseRequest;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.List;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ConcurrentHashMap<String, ConcurrentHashMap<String, Handler>> handlers = new ConcurrentHashMap<>();
    final private List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");

    private int port;
    private ExecutorService pool;

    public Server(int port, int poolSize) throws IOException {
        this.port = port;
        this.pool = Executors.newFixedThreadPool(poolSize);
    }

    public void start() {

        while (true) {
            try {
                final var serverSocket = new ServerSocket(port);
                System.out.println("Server started");
                while (!serverSocket.isClosed()) {
                    try {
                        final var socket = serverSocket.accept();
                        pool.execute(() -> {
                            connectionProcessing(socket);
                        });
                        System.out.println("New connection");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                pool.shutdown();
            }
        }
    }

    public void connectionProcessing(Socket socket) {
        try {
//          Проверка некорректного пути или неполноценного requestLine
            final var out = new BufferedOutputStream(socket.getOutputStream());
            final var request = new ParseRequest().parseRequest(socket);

            if (request == null) {
                return;
            }

            final var path = request.getPath();
            if (!validPaths.contains(path)) {
                out.write((
                        "HTTP/1.1 404 Not Found\r\n" +
                                "Content-Length: 0\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());
                out.flush();
                return;
            } else {

// --------------------------------------------------------------------------------
// ---------Обработка запроса по определенному пути--------------------------------
// --------------------------------------------------------------------------------
// ---------Данный запрос будет обрабатывать ClassicHandler------------------------
// --------------------------------------------------------------------------------
                if (path.equals("/classic.html")) {
                    handlers.get(request.getMethod())
                            .get(request.getPath())
                            .handle(request, out);
                    return;
                } else {

// --------------------------------------------------------------------------------
// ---------Остальные запросы будет обрабатывать DefaultHandler--------------------
// --------------------------------------------------------------------------------
                    handlers.get(request.getMethod())
                            .get(request.getPath())
                            .handle(request, out);
                    return;
                }
            }
// --------------------------------------------------------------------------------
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//  Добавления handler
    public void addHandler(String method, String path, Handler handler) {
        if (!handlers.containsKey(method))
            handlers.computeIfAbsent(method, k -> new ConcurrentHashMap<>()).put(path, handler);

        handlers.get(method).put(path, handler);
    }

}