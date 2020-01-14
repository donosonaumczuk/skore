package ar.edu.itba.paw.webapp.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;

public class JsonResourceConfig extends ResourceConfig {

    public JsonResourceConfig() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.ACCEPT_FLOAT_AS_INT);
        objectMapper.enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);//TODO:CHECK validate
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);//TODO:CHECK validate
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); //TODO: To look better in browser, remove for prod
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        register(new JacksonJaxbJsonProvider(objectMapper, JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS));
    }
}
