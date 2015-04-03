package com.jgarcia.messageparser;

import com.jgarcia.messageparser.model.Link;
import com.jgarcia.messageparser.model.MessageContent;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class MessageContentParserTest {

    private MessageContentParser messageContentParser;

    @Before
    public void setUp() {
        messageContentParser = new MessageContentParser();
    }

    @Test
    public void testSingleLink() {
        final String message = "http://www.google.com/";
        final List<Link> links = messageContentParser.parse(message).getLinks();
        assertThat(links.size(), is(1));
        assertThat(links.get(0).getUrl(), is("http://www.google.com/"));
    }

    @Test
    public void testSingleLinkWithPath() {
        final String message = "http://www.google.com/intl/en/policies/privacy/";
        final List<Link> links = messageContentParser.parse(message).getLinks();
        assertThat(links.size(), is(1));
        assertThat(links.get(0).getUrl(), is("http://www.google.com/intl/en/policies/privacy/"));
    }

    @Test
    public void testSubdomain() {
        final String message = "http://support.google.com";
        final List<Link> links = messageContentParser.parse(message).getLinks();
        assertThat(links.size(), is(1));
        assertThat(links.get(0).getUrl(), is("http://support.google.com"));
    }

    @Test
    public void testHttps() {
        final String message = "https://www.wellsfargo.com";
        final List<Link> links = messageContentParser.parse(message).getLinks();
        assertThat(links.size(), is(1));
        assertThat(links.get(0).getUrl(), is("https://www.wellsfargo.com"));
    }

    @Test
    public void testNoExtensionIsValid() {
        final String message = "http://localhost:8080/message";
        final List<Link> links = messageContentParser.parse(message).getLinks();
        assertThat(links.size(), is(1));
        assertThat(links.get(0).getUrl(), is("http://localhost:8080/message"));
    }

    @Test
    public void testMultipleLinks() {
        final String message = "http://www.msn.com http://www.google.com http://yahoo.com";
        final List<Link> links = messageContentParser.parse(message).getLinks();
        assertThat(links.size(), is(3));
        assertThat(links.get(0).getUrl(), is("http://www.msn.com"));
        assertThat(links.get(1).getUrl(), is("http://www.google.com"));
        assertThat(links.get(2).getUrl(), is("http://yahoo.com"));
    }

    @Test
    public void testNoHostnameIgnored() {
        final String message = "Check out this cool site: http://";
        assertThat(messageContentParser.parse(message).getLinks(), is(nullValue()));
    }

    @Test
    public void testSingleMention() {
        final String message = "@here is relative to there, except when...";
        final List<String> mentions = messageContentParser.parse(message).getMentions();
        assertThat(mentions.size(), is(1));
        assertThat(mentions.get(0), is("@here"));
    }

    @Test
    public void testMidSentenceMention() {
        final String message = "Yeah, if @you could put the cover sheet on your TPS reports, that'd be great.";
        final List<String> mentions = messageContentParser.parse(message).getMentions();
        assertThat(mentions.size(), is(1));
        assertThat(mentions.get(0), is("@you"));
    }

    @Test
    public void testOnlyMention() {
        final String message = "@only";
        final List<String> mentions = messageContentParser.parse(message).getMentions();
        assertThat(mentions.size(), is(1));
        assertThat(mentions.get(0), is("@only"));
    }

    @Test
    public void testMidWordMentionIgnored() {
        final String message = "You can't just m@ntion anywhere.";
        assertThat(messageContentParser.parse(message).getMentions(), is(nullValue()));
    }

    @Test
    public void testMentionBeforeNewLine() {
        final String message = " @ping \npong";
        final List<String> mentions = messageContentParser.parse(message).getMentions();
        assertThat(mentions.size(), is(1));
        assertThat(mentions.get(0), is("@ping"));
    }

    @Test
    public void testSingleEmoticon() {
        final String message = "(wave) Hello, world!";
        final List<String> emoticons = messageContentParser.parse(message).getEmoticons();
        assertThat(emoticons.size(), is(1));
        assertThat(emoticons.get(0), is("(wave)"));
    }

    @Test
    public void testMidSentenceEmoticon() {
        final String message = "It's not unusual for computer scientists to plant (trees) upside down.";
        final List<String> emoticons = messageContentParser.parse(message).getEmoticons();
        assertThat(emoticons.size(), is(1));
        assertThat(emoticons.get(0), is("(trees)"));
    }

    @Test
    public void testMultipleEmoticons() {
        final String message = "Self (control) is (inversely) proportional to the frequency of (emoji) use.";
        final List<String> emoticons = messageContentParser.parse(message).getEmoticons();
        assertThat(emoticons.size(), is(3));
        assertThat(emoticons.get(0), is("(control)"));
        assertThat(emoticons.get(1), is("(inversely)"));
        assertThat(emoticons.get(2), is("(emoji)"));
    }

    @Test
    public void testMidWordEmoticon() {
        final String message = "You can mix your (emo)ji and your words!";
        final List<String> emoticons = messageContentParser.parse(message).getEmoticons();
        assertThat(emoticons.size(), is(1));
        assertThat(emoticons.get(0), is("(emo)"));
    }

    @Test
    public void testEmoticonMaxLength() {
        StringBuilder sb = new StringBuilder(15).append("(");
        for (int i = 0; i < 15; ++i) {
            sb = sb.append("e");
        }
        sb.append(")");
        final String message = String.format("I hope this (%s) can be autocompleted.", sb.toString());
        final List<String> emoticons = messageContentParser.parse(message).getEmoticons();
        assertThat(emoticons.size(), is(1));
        assertThat(emoticons.get(0), is(sb.toString()));
    }

    @Test
    public void testLongEmoticonIgnored() {
        final String message = "Emoticons are limited to 15 characters excluding the wrapping parenthesis. (IAmRelativelySureThisStringIsMuchTooMuch)";
        assertThat(messageContentParser.parse(message).getEmoticons(), is(nullValue()));
    }

    @Test
    public void testNestedEmoticons() {
        final String message = "Nesting isn't technically disallowed ((a)(b)(c))";
        final List<String> emoticons = messageContentParser.parse(message).getEmoticons();
        assertThat(emoticons.size(), is(3));
        assertThat(emoticons.get(0), is("(a)"));
        assertThat(emoticons.get(1), is("(b)"));
        assertThat(emoticons.get(2), is("(c)"));
    }

    @Test
    public void testParseSingleMention() {
        final String message = "@chris you around?";
        final MessageContent messageContent = messageContentParser.parse(message);
        assertThat(messageContent.getEmoticons(), is(nullValue()));
        assertThat(messageContent.getLinks(), is(nullValue()));
        assertThat(messageContent.getMentions().size(), is(1));
        assertThat(messageContent.getMentions().get(0), is("@chris"));
    }

    @Test
    public void testParseMultipleEmoticons() {
        final String message = "Good morning! (megusta) (coffee)";
        final MessageContent messageContent = messageContentParser.parse(message);
        assertThat(messageContent.getMentions(), is(nullValue()));
        assertThat(messageContent.getLinks(), is(nullValue()));
        assertThat(messageContent.getEmoticons().size(), is(2));
        assertThat(messageContent.getEmoticons().get(0), is("(megusta)"));
        assertThat(messageContent.getEmoticons().get(1), is("(coffee)"));
    }

    @Test
    public void testParseSingleLink() {
        final String message = "Olympics are starting soon; http://www.nbcolympics.com";
        final MessageContent messageContent = messageContentParser.parse(message);
        assertThat(messageContent.getEmoticons(), is(nullValue()));
        assertThat(messageContent.getMentions(), is(nullValue()));
        assertThat(messageContent.getLinks().size(), is(1));
        assertThat(messageContent.getLinks().get(0).getUrl(), is("http://www.nbcolympics.com"));
        // TODO: Mock Jsoup call.
        assertThat(messageContent.getLinks().get(0).getTitle(), is("NBC Olympics | Home of the 2016 Olympic Games in Rio"));
    }

    @Test
    public void testParseAllTheThings() {
        final String message = "@bob @john (success) such a cool feature; https://twitter.com/jdorfman/status/430511497475670016";
        final MessageContent messageContent = messageContentParser.parse(message);
        assertThat(messageContent.getEmoticons().size(), is(1));
        assertThat(messageContent.getEmoticons().get(0), is("(success)"));
        assertThat(messageContent.getMentions().size(), is(2));
        assertThat(messageContent.getMentions().get(0), is("@bob"));
        assertThat(messageContent.getMentions().get(1), is("@john"));
        assertThat(messageContent.getLinks().size(), is(1));
        assertThat(messageContent.getLinks().get(0).getUrl(), is("https://twitter.com/jdorfman/status/430511497475670016"));
        // TODO: Mock Jsoup call.
        assertThat(messageContent.getLinks().get(0).getTitle(),
                is("Justin Dorfman on Twitter: \"nice @littlebigdetail from @HipChat"
                        + " (shows hex colors when pasted in chat). http://t.co/7cI6Gjy5pq\""));
    }
}