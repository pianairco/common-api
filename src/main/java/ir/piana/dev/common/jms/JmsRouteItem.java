package ir.piana.dev.common.jms;

import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@NoArgsConstructor
public class JmsRouteItem {
    private String subject;
    private String group;
    private String handlerClass;
    private List<String> roles;
    private String dtoType;
    private String response;

    public String getSubject() {
        return subject;
    }

    public String getGroup() {
        return group;
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

    public String getResponse() {
        return response;
    }
}
