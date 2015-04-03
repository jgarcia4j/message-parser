package com.jgarcia.messages.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.jgarcia.messageparser.model.Emoticon;

import java.io.IOException;

/**
 * Serializes an {@link Emoticon} to conform to the JSON contract.
 */
public class EmoticonJsonSerializer extends JsonSerializer<Emoticon> {

    @Override
    public Class<Emoticon> handledType() {
        return Emoticon.class;
    }

    /**
     * Solely serializes the keyword from the given {@code emoticon}.
     *
     * @param emoticon the emoticon
     * @param jgen     the JSON generator
     * @param provider the serialization provider
     * @throws IOException if an error occurs during serialization.
     */
    @Override
    public void serialize(Emoticon emoticon, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(emoticon.getKeyword());
    }
}
