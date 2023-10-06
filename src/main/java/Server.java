import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    final List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");
    Map<String, ConcurrentHashMap<String, Handler>> handlers = new ConcurrentHashMap<>();
    private int port;
    private ExecutorService pool;

    public Server(int port, int poolSize) throws IOException {
        this.port = port;
        this.pool = Executors.newFixedThreadPool(poolSize);
    }

    public void start() {
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

    public void connectionProcessing(Socket socket)  {

            try {
                if (!socket.isClosed()) {
                    final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    final var out = new BufferedOutputStream(socket.getOutputStream());
                    final var requestLine = in.readLine();

                    final var parts = requestLine.split(" ");

                    if (parts.length != 3) {
                        return;
                    }

                    final var path = parts[1];
                    if (!validPaths.contains(path)) {
                        out.write((
                                "HTTP/1.1 404 Not Found\r\n" +
                                        "Content-Length: 0\r\n" +
                                        "Connection: close\r\n" +
                                        "\r\n"
                        ).getBytes());
                        out.flush();
                        return;
                    }

                    final var filePath = Path.of(".", "public", path);
                    final var mimeType = Files.probeContentType(filePath);

                    if (path.equals("/classic.html")) {
                        final var template = Files.readString(filePath);
                        final var content = template.replace("{time}", LocalDateTime.now().toString()).getBytes();

                        out.write((
                                "HTTP/1.1 200 OK\r\n" +
                                        "Content-Type: " + mimeType + "\r\n" +
                                        "Content-Length: " + content.length + "\r\n" +
                                        "Connection: close\r\n" +
                                        "\r\n"
                        ).getBytes());
                        out.write(content);
                        out.flush();

                        return;
                    }

                    final var size = Files.size(filePath);

                    out.write((
                            "HTTP/1.1 200 OK\r\n" +
                                    "Content-Type " + mimeType + "\r\n" +
                                    "Content-Length" + size + "\r\n" +
                                    "Connection: close\r\n" +
                                    "\r\n"
                    ).getBytes());
                    Files.copy(filePath, out);
                    out.flush();

                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

}