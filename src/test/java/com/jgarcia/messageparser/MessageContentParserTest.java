package com.jgarcia.messageparser;

import com.jgarcia.messageparser.model.MessageContent;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class MessageContentParserTest {

    private MessageContentParser messageContentParser;

    @Before
    public void setUp() {
        messageContentParser = new MessageContentParser();
    }

    @Test
    public void testParseSingleMention() {
        final String message = "@chris you around?";
        final MessageContent messageContent = messageContentParser.parse(message);
        assertThat(messageContent.getEmoticons(), is(nullValue()));
        assertThat(messageContent.getLinks(), is(nullValue()));
        assertThat(messageContent.getMentions().size(), is(1));
        assertThat(messageContent.getMentions().get(0), is("chris"));
    }

    @Test
    public void testParseMultipleEmoticons() {
        final String message = "Good morning! (megusta) (coffee)";
        final MessageContent messageContent = messageContentParser.parse(message);
        assertThat(messageContent.getMentions(), is(nullValue()));
        assertThat(messageContent.getLinks(), is(nullValue()));
        assertThat(messageContent.getEmoticons().size(), is(2));
        assertThat(messageContent.getEmoticons().get(0), is("megusta"));
        assertThat(messageContent.getEmoticons().get(1), is("coffee"));
    }

    @Test
    public void testParseSingleLink() {
        final String message = "Olympics are starting soon; http://www.nbcolympics.com";
        final MessageContent messageContent = messageContentParser.parse(message);
        assertThat(messageContent.getEmoticons(), is(nullValue()));
        assertThat(messageContent.getMentions(), is(nullValue()));
        assertThat(messageContent.getLinks().size(), is(1));
        assertThat(messageContent.getLinks().get(0).getUrl(), is("http://www.nbcolympics.com"));
        assertThat(messageContent.getLinks().get(1).getTitle(), is("TBD"));
    }

    @Test
    public void testParseAllTheThings() {
        final String message = "@bob @john (success) such a cool feature; https://twitter.com/jdorfman/status/430511497475670016";
        final MessageContent messageContent = messageContentParser.parse(message);
        assertThat(messageContent.getEmoticons().size(), is(1));
        assertThat(messageContent.getEmoticons().get(0), is("success"));
        assertThat(messageContent.getMentions().size(), is(2));
        assertThat(messageContent.getMentions().get(0), is("bob"));
        assertThat(messageContent.getMentions().get(1), is("john"));
        assertThat(messageContent.getLinks().size(), is(1));
        assertThat(messageContent.getLinks().get(0).getUrl(), is("https://twitter.com/jdorfman/status/430511497475670016"));
        assertThat(messageContent.getLinks().get(0).getTitle(), is("TBD"));
    }
}