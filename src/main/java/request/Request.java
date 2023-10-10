package request;

import java.util.Map;

public class Request {

    private String method;
    private Map<String, String> headers;
    private String body;
    private String path;

    public Request() {
    }

    public Request(String method, Map<String, String> headers, String path, String body) {
        this.method = method;
        this.headers = headers;
        this.path = path;
        this.body = body;
    }

    public Request(String method, Map<String, String> headers, String path) {
        this.method = method;
        this.headers = headers;
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public String getPath() {
        return path;
    }
}
