package ir.piana.dev.common.http.server;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
public class HttpRouteItem {
    private String method;
    private String path;
    private String handlerClass;
    private List<String> roles;
    private String dtoType;
    private String responseType;
    private String response;
    private Map<String, String> configs;
}
