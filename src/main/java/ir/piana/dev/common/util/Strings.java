package ir.piana.dev.common.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Strings related utility methods.
 *
 * @author Alireza Pourtaghi
 */
public final class Strings {

    /**
     * Returns the base64 representation of content.
     *
     * @param content the string content that should be converted into its base64 representation
     * @return base64 representation of content
     */
    public static String base64(String content) {
        return Base64.getUrlEncoder().encodeToString(content.getBytes(StandardCharsets.UTF_8));
    }
}
