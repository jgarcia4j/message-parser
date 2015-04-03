package com.jgarcia.messages.service;

import com.jgarcia.messages.MessagesAppConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MessageServiceIntegrationTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new MessagesAppConfig();
    }

    @Test
    public void testParseSingleMention() {
        final String message = "@chris you around?";
        final String response = target("/messages")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(message, MediaType.TEXT_PLAIN_TYPE))
                .readEntity(String.class);
        assertThat(response, is("{\"mentions\":[\"chris\"]}"));
    }

    @Test
    public void testParseMultipleEmoticons() {
        final String message = "Good morning! (megusta) (coffee)";
        final String response = target("/messages")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(message, MediaType.TEXT_PLAIN_TYPE))
                .readEntity(String.class);
        assertThat(response, is("{\"emoticons\":[\"megusta\",\"coffee\"]}"));
    }

    @Test
    public void testParseSingleLink() {
        final String message = "Olympics are starting soon; http://www.nbcolympics.com";
        final String response = target("/messages")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(message, MediaType.TEXT_PLAIN_TYPE))
                .readEntity(String.class);
        assertThat(response, is("{\"links\":[{\"url\":\"http://www.nbcolympics.com\",\"title\":\"NBC Olympics | Home of the 2016 Olympic Games in Rio\"}]}"));
    }

    @Test
    public void testParseAllTheThings() throws Exception {
        final String message = "@bob @john (success) such a cool feature; "
                + "https://twitter.com/jdorfman/status/430511497475670016";
        final String response = target("/messages")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(message, MediaType.TEXT_PLAIN_TYPE))
                .readEntity(String.class);
        System.out.println(response);
        assertThat(response, is("{\"mentions\":[\"bob\",\"john\"],\"emoticons\":[\"success\"],\"links\":[{\"url\":\"https://twitter.com/jdorfman/status/430511497475670016\",\"title\":\"Justin Dorfman on Twitter: \\\"nice @littlebigdetail from @HipChat (shows hex colors when pasted in chat). http://t.co/7cI6Gjy5pq\\\"\"}]}"));
    }
}