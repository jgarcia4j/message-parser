package com.jgarcia.messages.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.jgarcia.messageparser.model.UserMention;

import java.io.IOException;

/**
 * Serializes a {@link UserMention} to conform to JSON contract.
 */
public class UserMentionJsonSerializer extends JsonSerializer<UserMention> {

    @Override
    public Class<UserMention> handledType() {
        return UserMention.class;
    }

    /**
     * Solely serializes the user's handle from the given {@code userMention}.
     *
     * @param userMention        the mentioned user
     * @param jsonGenerator      the JSON generator
     * @param serializerProvider the serialization provider
     * @throws IOException if an error occurs during serialization.
     */
    @Override
    public void serialize(UserMention userMention, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String handle = null;
        if (userMention != null && userMention.getUser() != null && userMention.getUser().getHandle() != null) {
            handle = userMention.getUser().getHandle();
        }
        jsonGenerator.writeString(handle);
    }
}
