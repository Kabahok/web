import java.util.Map;

public class RequestBuilder {
    private String method;
    private Map<String, String> headers;
    private String path;
    private String body;

    public Request build() throws IllegalArgumentException {
        if (method != null && headers != null && path != null) {
            if (body.equals("")) {
                return new Request(method, headers, path);
            } else {
                return new Request(method, headers, path, body);
            }
        }

        throw new IllegalArgumentException("Недостаточно данных для парсинга запроса");
    }

    public RequestBuilder addMethod(String method) {
        this.method = method;
        return this;
    }

    public RequestBuilder addHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public RequestBuilder addPath(String path) {
        this.path = path;
        return this;
    }

    public RequestBuilder addBody(String body) {
        this.body = body;
        return this;
    }
}
