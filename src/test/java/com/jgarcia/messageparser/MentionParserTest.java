package com.jgarcia.messageparser;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class MentionParserTest {

    private MentionParser mentionParser;

    @Before
    public void setUp() {
        mentionParser = new MentionParser();
    }

    @Test
    public void testSingleMention() {
        final String message = "@here is relative to there, except when...";
        final List<String> mentions = mentionParser.parse(message);
        assertThat(mentions.size(), is(1));
        assertThat(mentions.get(0), is("here"));
    }

    @Test
    public void testMidSentenceMention() {
        final String message = "Yeah, if @you could put the cover sheet on your TPS reports, that'd be great.";
        final List<String> mentions = mentionParser.parse(message);
        assertThat(mentions.size(), is(1));
        assertThat(mentions.get(0), is("you"));
    }

    @Test
    public void testOnlyMention() {
        final String message = "@only";
        final List<String> mentions = mentionParser.parse(message);
        assertThat(mentions.size(), is(1));
        assertThat(mentions.get(0), is("only"));
    }

    @Test
    public void testMidWordMentionIgnored() {
        final String message = "You can't just m@ntion anywhere.";
        assertThat(mentionParser.parse(message).size(), is(0));
    }

    @Test
    public void testMentionBeforeNewLine() {
        final String message = " @ping \npong";
        final List<String> mentions = mentionParser.parse(message);
        assertThat(mentions.size(), is(1));
        assertThat(mentions.get(0), is("ping"));
    }

}