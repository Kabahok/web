public class Request {

    private String method;
    private String headers;
    private String body;

    public Request(String method, String headers, String body) {
        this.method = method;
        this.headers = headers;
        this.body = body;
    }

    public Request(String method, String headers) {
        this.method = method;
        this.headers = headers;
    }

    public String getMethod() {
        return method;
    }

    public String getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }
}
