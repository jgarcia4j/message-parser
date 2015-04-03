package com.jgarcia.messages;

import com.jgarcia.messages.provider.ObjectMapperProvider;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * The web application configuration.
 */
@ApplicationPath("/")
public class MessagesAppConfig extends ResourceConfig {
    public MessagesAppConfig() {
        packages("com.jgarcia.messages.service");
        register(ObjectMapperProvider.class);
        register(JacksonFeature.class);
    }
}
