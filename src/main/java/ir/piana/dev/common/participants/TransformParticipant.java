package ir.piana.dev.common.participants;

import ir.piana.dev.common.util.ContextLogger;
import ir.piana.dev.jsonparser.json.JsonParser;
import ir.piana.dev.jsonparser.json.JsonTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class TransformParticipant {
    protected final ContextLogger logger = ContextLogger.getLogger(this.getClass());

    @Autowired
    private JsonParser jsonParser;

    public JsonTarget transform(
            JsonTarget source,
            Resource yamlPropertiesResource) {
        return jsonParser.transform(
                source, yamlPropertiesResource);
    }
}
