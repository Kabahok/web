package handlers;

import request.Request;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DefaultHandler implements Handler{

    @Override
    public void handle(Request request, BufferedOutputStream responseStream) throws IOException, URISyntaxException {
        final var path = request.getPathOfQuery();
        final var filePath = Path.of(".", "public", path);
        final var mimeType = Files.probeContentType(filePath);
        final var size = Files.size(filePath);

        responseStream.write((
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type " + mimeType + "\r\n" +
                        "Content-Length" + size + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        Files.copy(filePath, responseStream);
        responseStream.flush();
    }
}
