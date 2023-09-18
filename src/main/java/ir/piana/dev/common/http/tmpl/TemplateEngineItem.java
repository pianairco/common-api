package ir.piana.dev.common.http.tmpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TemplateEngineItem {
    private String name;
    private String dir;
    private String postfix;
    private boolean cacheable;
}
