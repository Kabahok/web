import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ParseRequest {



    public Request parseRequest(Socket socket) throws IOException {
        final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String requestLine = in.readLine();

        if (requestLine == null || requestLine.isEmpty()) {
            return null;
        }


        String parts[] = requestLine.split(" ");
        if (parts.length != 3) {
            return null;
        }

        String method = parts[0];
        String path = parts[1];

        Map<String, String> headers = new HashMap<>();
        String headerLine;

        while ((headerLine = in.readLine()) != null && !headerLine.isEmpty()) {
            String headerParts[] = headerLine.split(": ");
            if (headerParts.length == 2) {
                headers.put(headerParts[0], headerParts[1]);
            }
        }

        var body = new StringBuilder();
        int contentLength = 0;
        if (headers.containsKey("Content-Length")) {
            contentLength = Integer.parseInt(headers.get("Content-Length"));
            char[] buffer = new char[contentLength];
            int bytesRead = in.read(buffer, 0, contentLength);
            if (bytesRead != -1) {
                body.append(buffer, 0, contentLength);
            }
        }

        return new RequestBuilder().addMethod(method)
                .addBody(body.toString())
                .addHeaders(headers)
                .addPath(path)
                .build();
    }
}
