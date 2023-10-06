import java.io.IOException;

public class Main {
    private final static int PORT = 8080;
    private final static int POOL_SIZE = 64;
    public static void main(String[] args) {
        try {
            Server server = new Server(PORT, POOL_SIZE);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
