import java.io.BufferedReader;
import java.io.IOException;

public class ParseRequest {
    public ParseRequest() {

    }

    public String parseRequest(BufferedReader in) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = in.readLine()) != null) {
            sb.append(line).append("\n");

            if (line.isEmpty()) {
                break;
            }
        }

        return sb.toString();

    }

//    public Request buildRequest
}
