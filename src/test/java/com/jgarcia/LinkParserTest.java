package com.jgarcia;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class LinkParserTest {

    private LinkParser linkParser;

    @Before
    public void setUp() {
        linkParser = new LinkParser();
    }

    @Test
    public void testSingleLink() {
        final String message = "http://www.google.com/";
        final List<String> links = linkParser.parse(message);
        assertThat(links.size(), is(1));
        assertThat(links.get(0), is("http://www.google.com/"));
    }

    @Test
    public void testSingleLinkWithPath() {
        final String message = "http://www.google.com/account/settings";
        final List<String> links = linkParser.parse(message);
        assertThat(links.size(), is(1));
        assertThat(links.get(0), is("http://www.google.com/account/settings"));
    }

    @Test
    public void testSubdomain() {
        final String message = "http://account.google.com";
        final List<String> links = linkParser.parse(message);
        assertThat(links.size(), is(1));
        assertThat(links.get(0), is("http://account.google.com"));
    }

    @Test
    public void testHttps() {
        final String message = "https://www.google.com";
        final List<String> links = linkParser.parse(message);
        assertThat(links.size(), is(1));
        assertThat(links.get(0), is("https://www.google.com"));
    }

    @Test
    public void testNoExtensionValid() {
        final String message = "http://localhost:8080/message";
        final List<String> links = linkParser.parse(message);
        assertThat(links.size(), is(1));
        assertThat(links.get(0), is("http://localhost:8080/message"));
    }

    @Test
    public void testMultipleLinks() {
        final String message = "http://www.msn.com http://www.google.com http://yahoo.com";
        final List<String> links = linkParser.parse(message);
        assertThat(links.size(), is(3));
        assertThat(links.get(0), is("http://www.msn.com"));
        assertThat(links.get(1), is("http://www.google.com"));
        assertThat(links.get(2), is("http://yahoo.com"));
    }

    @Test
    public void testNoHostnameIgnored() {
        final String message = "Check out this cool site: http://";
        assertThat(linkParser.parse(message).size(), is(0));
    }
}