package com.jgarcia.messages.service;

import com.jgarcia.messageparser.MessageContentParser;
import com.jgarcia.messageparser.model.MessageContent;
import com.jgarcia.messageparser.model.User;
import com.jgarcia.messageparser.service.JsoupLinkService;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

@Path("/messages")
public class MessageService {

    private final MessageContentParser messageContentParser;

    public MessageService() {
        this.messageContentParser = new MessageContentParser(
                (handle -> Optional.of(new User(0, handle))),
                (keyword -> Optional.of("/images/" + keyword + ".png")),
                new JsoupLinkService()
        );
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public MessageContent addMessage(final String message) {
        return messageContentParser.parse(message);
    }
}
