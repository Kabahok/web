package request;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
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

    public String getPathOfQuery() throws URISyntaxException {
        URI uri = new URI(this.path);
        return uri.getPath();
    }

    public String getQueryParam(String name) throws URISyntaxException {
        List<NameValuePair> params = URLEncodedUtils.parse(new URI(this.path), StandardCharsets.UTF_8);
        for (NameValuePair param : params) {
            if (param.getName().equals(name)) {
                return param.getValue();
            }
        }

        return null;
    }

    public List<NameValuePair> getQueryParams() throws URISyntaxException {
        return URLEncodedUtils.parse(new URI(this.path), StandardCharsets.UTF_8);
    }
}
