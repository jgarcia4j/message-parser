package com.jgarcia.messageparser;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class EmoticonParserTest {

    private EmoticonParser emoticonParser;

    @Before
    public void setUp() {
        emoticonParser = new EmoticonParser();
    }

    @Test
    public void testSingleEmoticon() {
        final String message = "(wave) Hello, world!";
        final List<String> emoticons = emoticonParser.parse(message);
        assertThat(emoticons.size(), is(1));
        assertThat(emoticons.get(0), is("wave"));
    }

    @Test
    public void testMidSentenceEmoticon() {
        final String message = "It's not unusual for computer scientists to plant (trees) upside down.";
        final List<String> emoticons = emoticonParser.parse(message);
        assertThat(emoticons.size(), is(1));
        assertThat(emoticons.get(0), is("trees"));
    }

    @Test
    public void testMultipleEmoticons() {
        final String message = "Self (control) is (inversely) proportional to the frequency of (emoji) use.";
        final List<String> emoticons = emoticonParser.parse(message);
        assertThat(emoticons.size(), is(3));
        assertThat(emoticons.get(0), is("control"));
        assertThat(emoticons.get(1), is("inversely"));
        assertThat(emoticons.get(2), is("emoji"));
    }

    @Test
    public void testMidWordEmoticon() {
        final String message = "You can mix your (emo)ji and your words!";
        final List<String> emoticons = emoticonParser.parse(message);
        assertThat(emoticons.size(), is(1));
        assertThat(emoticons.get(0), is("emo"));
    }

    @Test
    public void testEmoticonMaxLength() {
        StringBuilder sb = new StringBuilder(15);
        for (int i = 0; i < EmoticonParser.MAX_LENGTH; ++i) {
            sb = sb.append("e");
        }
        final String message = String.format("I hope this (%s) can be autocompleted.", sb.toString());
        final List<String> emoticons = emoticonParser.parse(message);
        assertThat(emoticons.size(), is(1));
        assertThat(emoticons.get(0), is(sb.toString()));
    }

    @Test
    public void testLongEmoticonIgnored() {
        final String message = "Emoticons are limited to 15 characters excluding the wrapping parenthesis. (IAmRelativelySureThisStringIsMuchTooMuch)";
        assertThat(emoticonParser.parse(message).size(), is(0));
    }

    @Test
    public void testNestedEmoticons() {
        final String message = "Nesting isn't technically disallowed ((a)(b)(c))";
        final List<String> emoticons = emoticonParser.parse(message);
        assertThat(emoticons.size(), is(3));
        assertThat(emoticons.get(0), is("a"));
        assertThat(emoticons.get(1), is("b"));
        assertThat(emoticons.get(2), is("c"));
    }
}