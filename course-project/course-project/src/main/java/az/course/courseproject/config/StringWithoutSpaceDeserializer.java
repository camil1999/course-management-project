package az.course.courseproject.config;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class StringWithoutSpaceDeserializer extends StdDeserializer<String> {
    /**
     * 
     */
    private static final long serialVersionUID = -6972065572263950443L;

    protected StringWithoutSpaceDeserializer(Class<String> vc) {
        super(vc);
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return p.getText() != null ? p.getText().trim() : null;
    }
}