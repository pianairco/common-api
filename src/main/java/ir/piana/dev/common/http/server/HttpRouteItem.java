package ir.piana.dev.common.http.server;

import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@NoArgsConstructor
public class HttpRouteItem {
    private String method;
    private String path;
    private String handlerClass;
    private List<String> roles;
    private String dtoType;
    private String responseType;
    private String response;

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getHandlerClass() {
        return handlerClass;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getDtoType() {
        return dtoType;
    }

    public String getResponseType() {
        return responseType;
    }

    public String getResponse() {
        return response;
    }
}
