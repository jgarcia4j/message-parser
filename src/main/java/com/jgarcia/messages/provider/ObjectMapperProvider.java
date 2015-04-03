package com.jgarcia.messages.provider;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.jgarcia.messages.serializer.EmoticonJsonSerializer;
import com.jgarcia.messages.serializer.UserMentionJsonSerializer;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * Provides a singleton {@link ObjectMapper} for use by the Jersey framework.
 * The {@code ObjectMapper} is responsible for mapping POJOs to/from JSON.
 */
@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

    private final ObjectMapper objectMapper;

    public ObjectMapperProvider() {
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        // Register custom serializers to format JSON as required.
        final SimpleModule module = new SimpleModule();
        module.addSerializer(new UserMentionJsonSerializer());
        module.addSerializer(new EmoticonJsonSerializer());
        objectMapper.registerModule(module);
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return objectMapper;
    }
}
