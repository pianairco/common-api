package ir.piana.dev.common.http.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
public class AuthPhraseItem {
    private String name;
    private String providerClass;
    private Map<String, String> configs;
}
