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

/**
 * A web service that deals with chat messages.
 */
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

    /**
     * Returns structured {@link MessageContent} which was extracted from the given {@code message}.
     *
     * @param message the non-null message
     * @return structured {@link MessageContent} which was extracted from the given {@code message}.
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public MessageContent post(final String message) {
        return messageContentParser.parse(message);
    }
}
