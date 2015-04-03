package com.jgarcia.messageparser;

import com.jgarcia.messageparser.constant.EmoticonConst;
import com.jgarcia.messageparser.model.*;
import com.jgarcia.messageparser.service.EmoticonService;
import com.jgarcia.messageparser.service.LinkService;
import com.jgarcia.messageparser.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MessageContentParserTest {

    private MessageContentParser messageContentParser;

    @Mock private UserService userService;

    @Mock private EmoticonService emoticonService;

    @Mock private LinkService linkService;

    @Before
    public void setUp() {
        messageContentParser = new MessageContentParser(userService, emoticonService, linkService);
    }

    @Test
    public void testSingleLink() {
        final String url = "http://www.google.com/";
        final String title = "Google";
        when(linkService.getDocumentTitle(eq(url))).thenReturn(Optional.of(title));
        final List<Link> links = messageContentParser.parse(url).getLinks();
        assertThat(links.size(), is(1));
        final Link link = links.get(0);
        assertThat(link.getUrl(), is("http://www.google.com/"));
        assertThat(link.getTitle(), is("Google"));
        assertThat(link.getMessageTag().getType(), is(MessageTagType.LINK));
        assertThat(link.getMessageTag().getStart(), is(0));
        assertThat(link.getMessageTag().getEnd(), is(url.length()));
    }

    @Test
    public void testSingleLinkWithPath() {
        final String url = "http://www.google.com/intl/en/policies/privacy/";
        final String title = "Privacy Policies";
        when(linkService.getDocumentTitle(eq(url))).thenReturn(Optional.of(title));
        final List<Link> links = messageContentParser.parse(url).getLinks();
        assertThat(links.size(), is(1));
        final Link link = links.get(0);
        assertThat(link.getUrl(), is(url));
        assertThat(link.getTitle(), is(title));
        assertThat(link.getMessageTag().getType(), is(MessageTagType.LINK));
        assertThat(link.getMessageTag().getStart(), is(0));
        assertThat(link.getMessageTag().getEnd(), is(url.length()));
    }

    @Test
    public void testSubdomain() {
        final String url = "http://support.google.com";
        final String title = "Support";
        when(linkService.getDocumentTitle(eq(url))).thenReturn(Optional.of(title));
        final List<Link> links = messageContentParser.parse(url).getLinks();
        assertThat(links.size(), is(1));
        final Link link = links.get(0);
        assertThat(link.getUrl(), is(url));
        assertThat(link.getTitle(), is(title));
        assertThat(link.getMessageTag().getType(), is(MessageTagType.LINK));
        assertThat(link.getMessageTag().getStart(), is(0));
        assertThat(link.getMessageTag().getEnd(), is(url.length()));
    }

    @Test
    public void testHttps() {
        final String url = "https://support.google.com";
        final String title = "Support";
        when(linkService.getDocumentTitle(eq(url))).thenReturn(Optional.of(title));
        final List<Link> links = messageContentParser.parse(url).getLinks();
        assertThat(links.size(), is(1));
        final Link link = links.get(0);
        assertThat(link.getUrl(), is(url));
        assertThat(link.getTitle(), is(title));
        assertThat(link.getMessageTag().getType(), is(MessageTagType.LINK));
        assertThat(link.getMessageTag().getStart(), is(0));
        assertThat(link.getMessageTag().getEnd(), is(url.length()));
    }

    @Test
    public void testNoExtensionIsValid() {
        final String url = "http://localhost:8080/messages";
        when(linkService.getDocumentTitle(eq(url))).thenReturn(Optional.empty());
        final List<Link> links = messageContentParser.parse(url).getLinks();
        assertThat(links.size(), is(1));
        final Link link = links.get(0);
        assertThat(link.getUrl(), is(url));
        assertThat(link.getTitle(), is(nullValue()));
        assertThat(link.getMessageTag().getType(), is(MessageTagType.LINK));
        assertThat(link.getMessageTag().getStart(), is(0));
        assertThat(link.getMessageTag().getEnd(), is(url.length()));
    }

    @Test
    public void testMultipleLinks() {
        final List<String> urls = Arrays.asList("http://www.msn.com", "http://www.google.com", "http://yahoo.com");
        final List<String> titles = Arrays.asList("MSN", "Google", "Yahoo");
        final StringBuilder sb = new StringBuilder();
        urls.forEach(url -> sb.append(url).append(" "));
        final String message = sb.toString();
        for (int i = 0; i < urls.size(); ++i) {
            when(linkService.getDocumentTitle(eq(urls.get(i)))).thenReturn(Optional.of(titles.get(i)));
        }
        final List<Link> links = messageContentParser.parse(message).getLinks();
        assertThat(links.size(), is(urls.size()));
        for (int i = 0; i < urls.size(); ++i) {
            final String linkId = "Link " + i;
            final Link link = links.get(i);
            assertThat(linkId, link.getUrl(), is(urls.get(i)));
            assertThat(linkId, link.getTitle(), is(titles.get(i)));
            assertThat(linkId, link.getMessageTag().getType(), is(MessageTagType.LINK));
            assertThat(linkId, link.getMessageTag().getStart(), is(message.indexOf(link.getUrl())));
            assertThat(linkId, link.getMessageTag().getEnd(), is(message.indexOf(link.getUrl()) + link.getUrl().length()));
        }
    }

    @Test
    public void testNoHostnameIgnored() {
        final String message = "Check out this cool site: http://";
        assertThat(messageContentParser.parse(message).getLinks(), is(nullValue()));
    }

    @Test
    public void testSingleMention() throws Exception {
        final String handle = "here";
        final String message = String.format("@%s is relative to there, except when...", handle);
        final User user = new User(1, handle);
        when(userService.findUserByHandle(eq(handle))).thenReturn(Optional.of(user));
        final List<UserMention> mentions = messageContentParser.parse(message).getMentions();
        assertThat(mentions.size(), is(1));
        assertThat(mentions.get(0).getUser(), is(user));
        assertThat(mentions.get(0).getTag().getType(), is(MessageTagType.USER_MENTION));
        assertThat(mentions.get(0).getTag().getStart(), is(0));
        assertThat(mentions.get(0).getTag().getEnd(), is(handle.length() + 1));
    }

    @Test
    public void testMidSentenceMention() throws Exception {
        final String handle = "you";
        final String message = String.format(
                "Yeah, if @%s could put the cover sheet on your TPS reports, that'd be great.", handle);
        final User user = new User(2, handle);
        when(userService.findUserByHandle(eq(handle))).thenReturn(Optional.of(user));
        final List<UserMention> mentions = messageContentParser.parse(message).getMentions();
        assertThat(mentions.size(), is(1));
        assertThat(mentions.get(0).getUser(), is(user));
        assertThat(mentions.get(0).getTag().getType(), is(MessageTagType.USER_MENTION));
        final int mentionStart = message.indexOf("@");
        assertThat(mentions.get(0).getTag().getStart(), is(mentionStart));
        assertThat(mentions.get(0).getTag().getEnd(), is(mentionStart + handle.length() + 1));
    }

    @Test
    public void testOnlyMention() throws Exception {
        final String handle = "wakeup";
        final User user = new User(3, handle);
        when(userService.findUserByHandle(eq(handle))).thenReturn(Optional.of(user));
        final List<UserMention> mentions = messageContentParser.parse("@" + handle).getMentions();
        assertThat(mentions.size(), is(1));
        assertThat(mentions.get(0).getUser(), is(user));
        assertThat(mentions.get(0).getTag().getType(), is(MessageTagType.USER_MENTION));
        assertThat(mentions.get(0).getTag().getStart(), is(0));
        assertThat(mentions.get(0).getTag().getEnd(), is(handle.length() + 1));
    }

    @Test
    public void testMidWordMentionIgnored() {
        final String message = "You can't just m@ntion anywhere.";
        assertThat(messageContentParser.parse(message).getMentions(), is(nullValue()));
    }

    @Test
    public void testMentionBeforeNewLine() throws Exception {
        final String handle = "ping";
        final String message = String.format("@%s \npong", handle);
        final User user = new User(4, handle);
        when(userService.findUserByHandle(eq(handle))).thenReturn(Optional.of(user));
        final List<UserMention> mentions = messageContentParser.parse("@" + handle).getMentions();
        assertThat(mentions.size(), is(1));
        assertThat(mentions.get(0).getUser(), is(user));
        assertThat(mentions.get(0).getTag().getType(), is(MessageTagType.USER_MENTION));
        assertThat(mentions.get(0).getTag().getStart(), is(0));
        assertThat(mentions.get(0).getTag().getEnd(), is(handle.length() + 1));
    }

    @Test
    public void testSingleEmoticon() {
        final String keyword = "wave";
        final String imageUrl = generateImageUrl(keyword);
        final String message = String.format("(%s) Hello, world!", keyword);
        when(emoticonService.findEmoticonByKeyword(keyword)).thenReturn(Optional.of(imageUrl));
        final List<Emoticon> emoticons = messageContentParser.parse(message).getEmoticons();
        assertThat(emoticons.size(), is(1));
        assertThat(emoticons.get(0).getKeyword(), is(keyword));
        assertThat(emoticons.get(0).getImageUrl(), is(imageUrl));
        assertThat(emoticons.get(0).getMessageTag().getType(), is(MessageTagType.EMOTICON));
        assertThat(emoticons.get(0).getMessageTag().getStart(), is(message.indexOf("(")));
        assertThat(emoticons.get(0).getMessageTag().getEnd(), is(message.indexOf(")") + 1));
    }

    @Test
    public void testMidSentenceEmoticon() {
        final String keyword = "trees";
        final String imageUrl = generateImageUrl(keyword);
        final String message = String.format("Why do CS majors draw their (trees) upside down?", keyword);
        when(emoticonService.findEmoticonByKeyword(keyword)).thenReturn(Optional.of(imageUrl));
        final List<Emoticon> emoticons = messageContentParser.parse(message).getEmoticons();
        assertThat(emoticons.size(), is(1));
        assertThat(emoticons.get(0).getKeyword(), is(keyword));
        assertThat(emoticons.get(0).getImageUrl(), is(imageUrl));
        assertThat(emoticons.get(0).getMessageTag().getType(), is(MessageTagType.EMOTICON));
        assertThat(emoticons.get(0).getMessageTag().getStart(), is(message.indexOf("(")));
        assertThat(emoticons.get(0).getMessageTag().getEnd(), is(message.indexOf(")") + 1));
    }

    @Test
    public void testMultipleEmoticons() {
        final String[] keywords = {"control", "inversely", "emoji"};
        final String[] imageUrls = Arrays.asList(keywords)
                .stream()
                .map(MessageContentParserTest::generateImageUrl)
                .toArray(String[]::new);
        final String message = String.format("Self (%s) is (%s) proportional to the frequency of (%s) use.", keywords);
        for (int i = 0; i < keywords.length; ++i) {
            final String keyword = keywords[i];
            final String imageUrl = imageUrls[i];
            when(emoticonService.findEmoticonByKeyword(keyword)).thenReturn(Optional.of(imageUrl));
        }
        final List<Emoticon> emoticons = messageContentParser.parse(message).getEmoticons();
        assertThat(emoticons.size(), is(imageUrls.length));
        for (int i = 0; i < emoticons.size(); ++i) {
            final Emoticon emoticon = emoticons.get(i);
            assertThat(emoticon.getKeyword(), is(keywords[i]));
            assertThat(emoticon.getImageUrl(), is(imageUrls[i]));
            assertThat(emoticon.getMessageTag().getType(), is(MessageTagType.EMOTICON));
            final String tag = "(" + keywords[i] + ")";
            final int startPos = message.indexOf(tag);
            assertThat(emoticon.getMessageTag().getStart(), is(startPos));
            assertThat(emoticon.getMessageTag().getEnd(), is(startPos + tag.length()));
        }
    }

    @Test
    public void testMidWordEmoticon() {
        final String keyword = "emo";
        final String imageUrl = generateImageUrl(keyword);
        final String message = String.format("You can mix your (%s)ji and your words!", keyword);
        when(emoticonService.findEmoticonByKeyword(keyword)).thenReturn(Optional.of(imageUrl));
        final List<Emoticon> emoticons = messageContentParser.parse(message).getEmoticons();
        assertThat(emoticons.size(), is(1));
        assertThat(emoticons.get(0).getKeyword(), is(keyword));
        assertThat(emoticons.get(0).getImageUrl(), is(imageUrl));
        assertThat(emoticons.get(0).getMessageTag().getType(), is(MessageTagType.EMOTICON));
        assertThat(emoticons.get(0).getMessageTag().getStart(), is(message.indexOf("(")));
        assertThat(emoticons.get(0).getMessageTag().getEnd(), is(message.indexOf(")") + 1));
    }

    @Test
    public void testEmoticonMaxLength() {
        StringBuilder sb = new StringBuilder(EmoticonConst.MAX_LENGTH).append("(");
        for (int i = 0; i < EmoticonConst.MAX_LENGTH; ++i) {
            sb = sb.append("e");
        }
        sb.append(")");
        final String keyword = sb.toString().substring(1, sb.length() -1 );
        final String imageUrl = generateImageUrl(keyword);
        final String message = String.format("I hope this (%s) can be autocompleted.", keyword);
        when(emoticonService.findEmoticonByKeyword(keyword)).thenReturn(Optional.of(imageUrl));
        final List<Emoticon> emoticons = messageContentParser.parse(message).getEmoticons();
        assertThat(emoticons.size(), is(1));
        assertThat(emoticons.get(0).getKeyword(), is(keyword));
        assertThat(emoticons.get(0).getImageUrl(), is(imageUrl));
        assertThat(emoticons.get(0).getMessageTag().getType(), is(MessageTagType.EMOTICON));
        assertThat(emoticons.get(0).getMessageTag().getStart(), is(message.indexOf("(")));
        assertThat(emoticons.get(0).getMessageTag().getEnd(), is(message.indexOf(")") + 1));
    }

    @Test
    public void testLongEmoticonIgnored() {
        final String keyword = "IAmRelativelySureThisStringIsMuchTooMuch";
        final String message = String.format(
                "Emoticons are limited to 15 characters excluding the wrapping parenthesis. (%s)", keyword);
        assertThat(messageContentParser.parse(message).getEmoticons(), is(nullValue()));
        verify(emoticonService, never()).findEmoticonByKeyword(eq(keyword));
    }

    @Test
    public void testNestedEmoticons() {
        final String[] keywords = {"a", "b", "c"};
        final String[] imageUrls = Arrays.asList(keywords)
                .stream()
                .map(MessageContentParserTest::generateImageUrl)
                .toArray(String[]::new);
        for (int i = 0; i < keywords.length; ++i) {
            final String keyword = keywords[i];
            final String imageUrl = imageUrls[i];
            when(emoticonService.findEmoticonByKeyword(keyword)).thenReturn(Optional.of(imageUrl));
        }
        final String message = "Nesting isn't technically disallowed ((a)(b)(c))";
        final List<Emoticon> emoticons = messageContentParser.parse(message).getEmoticons();
        assertThat(emoticons.size(), is(3));
        assertThat(emoticons.get(0).getKeyword(), is("a"));
        assertThat(emoticons.get(0).getImageUrl(), is(generateImageUrl("a")));
        assertThat(emoticons.get(0).getMessageTag(), equalTo(new MessageTag(MessageTagType.EMOTICON, 38, 41)));
        assertThat(emoticons.get(1).getKeyword(), is("b"));
        assertThat(emoticons.get(1).getImageUrl(), is(generateImageUrl("b")));
        assertThat(emoticons.get(1).getMessageTag(), equalTo(new MessageTag(MessageTagType.EMOTICON, 41, 44)));
        assertThat(emoticons.get(2).getKeyword(), is("c"));
        assertThat(emoticons.get(2).getImageUrl(), is(generateImageUrl("c")));
        assertThat(emoticons.get(2).getMessageTag(), equalTo(new MessageTag(MessageTagType.EMOTICON, 44, 47)));
    }

    @Test
    public void testParseSingleMention() throws Exception {
        final String message = "@chris you around?";
        final User user = new User(1, "chris");
        when(userService.findUserByHandle(eq("chris"))).thenReturn(Optional.of(user));
        final MessageContent messageContent = messageContentParser.parse(message);
        assertThat(messageContent.getEmoticons(), is(nullValue()));
        assertThat(messageContent.getLinks(), is(nullValue()));
        final List<UserMention> userMentions = messageContent.getMentions();
        assertThat(userMentions.size(), is(1));
        final UserMention userMention = userMentions.get(0);
        assertThat(userMention.getUser(), is(user));
    }

    @Test
    public void testParseMultipleEmoticons() {
        final String message = "Good morning! (megusta) (coffee)";
        when(emoticonService.findEmoticonByKeyword(eq("megusta"))).thenReturn(Optional.of(generateImageUrl("megusta")));
        when(emoticonService.findEmoticonByKeyword(eq("coffee"))).thenReturn(Optional.of(generateImageUrl("coffee")));
        final MessageContent messageContent = messageContentParser.parse(message);
        assertThat(messageContent.getMentions(), is(nullValue()));
        assertThat(messageContent.getLinks(), is(nullValue()));
        final List<Emoticon> emoticons = messageContent.getEmoticons();
        assertThat(emoticons.size(), is(2));
        final Emoticon e1 = emoticons.get(0);
        assertThat(e1.getKeyword(), is("megusta"));
        assertThat(e1.getImageUrl(), is(generateImageUrl("megusta")));
        final Emoticon e2 = emoticons.get(1);
        assertThat(e2.getKeyword(), is("coffee"));
        assertThat(e2.getImageUrl(), is(generateImageUrl("coffee")));
    }

    @Test
    public void testParseSingleLink() {
        final String message = "Olympics are starting soon; http://www.nbcolympics.com";
        when(linkService.getDocumentTitle("http://www.nbcolympics.com")).thenReturn(Optional.of("NBC Olympics"));
        final MessageContent messageContent = messageContentParser.parse(message);
        assertThat(messageContent.getEmoticons(), is(nullValue()));
        assertThat(messageContent.getMentions(), is(nullValue()));
        assertThat(messageContent.getLinks().size(), is(1));
        assertThat(messageContent.getLinks().get(0).getUrl(), is("http://www.nbcolympics.com"));
        assertThat(messageContent.getLinks().get(0).getTitle(), is("NBC Olympics"));
    }

    @Test
    public void testParseAllTheThings() throws Exception {
        final String url = "https://twitter.com/jdorfman/status/430511497475670016";
        final String message = "@bob @john (success) such a cool feature; " + url;
        when(emoticonService.findEmoticonByKeyword(eq("success"))).thenReturn(Optional.of(generateImageUrl("success")));
        final User bobUser = new User(1, "bob");
        final User johnUser = new User(2, "john");
        when(userService.findUserByHandle(eq("bob"))).thenReturn(Optional.of(bobUser));
        when(userService.findUserByHandle(eq("john"))).thenReturn(Optional.of(johnUser));
        when(linkService.getDocumentTitle(url)).thenReturn(Optional.of("Twitter"));
        final MessageContent messageContent = messageContentParser.parse(message);
        assertThat(messageContent.getEmoticons().size(), is(1));
        assertThat(messageContent.getEmoticons().get(0).getKeyword(), is("success"));
        assertThat(messageContent.getEmoticons().get(0).getImageUrl(), is(generateImageUrl("success")));
        assertThat(messageContent.getMentions().size(), is(2));
        assertThat(messageContent.getMentions().get(0).getUser(), is(bobUser));
        assertThat(messageContent.getMentions().get(1).getUser(), is(johnUser));
        assertThat(messageContent.getLinks().size(), is(1));
        assertThat(messageContent.getLinks().get(0).getUrl(), is(url));
        assertThat(messageContent.getLinks().get(0).getTitle(), is("Twitter"));
    }

    private static String generateImageUrl(final String keyword) {
        return "http://imagehost/" + keyword + ".png";
    }
}