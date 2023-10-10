import handlers.ClassicHandler;
import handlers.DefaultHandler;

import java.io.IOException;

public class Main {
    private final static int PORT = 8080;
    private final static int POOL_SIZE = 64;
    public static void main(String[] args) {
        try {
            Server server = new Server(PORT, POOL_SIZE);

            server.addHandler("GET", "/messages", new DefaultHandler());
            server.addHandler("POST", "/messages", new DefaultHandler());
            server.addHandler("GET", "/index.html", new DefaultHandler());
            server.addHandler("GET", "/classic.html", new ClassicHandler());

            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
