# message-parser
Parses a message for content such as mentions, emoji, and links.

## Challenge

Text-based communication services must reason about message content to optimize the utility of the data. For instance, a user may want to *mention* the people his or her message is primarily targeting. Additionally, such services are expected to format certain data such as *links* by adding anchor tags so that users may simply click on the text to see the referenced resource. Finally, for fun, it was decided that support for custom *emoticons* was imperative for the success of any novel chat service.

To support these features, we must parse the user's input using the following guidelines:
* Mentions
  * Denoted by an "@" symbol directly proceeding the targeted user or group's *handle*
  * Consist of a single _word_, where _word_ is defined as a sequence of non-whitespace characters.
  * **Examples:** @john, @emily, @all, @here
* Links
  * Denoted by any sequence of characters starting with "http://" or "https://"
  * Consist of a single _word_ (as previously defined). Note that the link is not strictly required at the start of the _word_.
  * **Examples:** http://www.msn.com/, https://www.google.com/, http://localhost:8080/messages
* Emoticons
  * Denoted by any sequence of ASCII characters (up to 15 characters max) wrapped around open and closed parenthesis.
  * **Examples:** (cat), (dog), (cactus)
  
## Approach

As defined, the challenge simply requires that the application return an ordered collection of **mentions**, **emoticons**, and **links** in JSON format as follows:
```
{
  "mentions": [
    "bob",
    "john"
  ],
  "emoticons": [
    "success"
  ],
  "links": [
    {
      "url": "https://twitter.com/jdorfman/status/430511497475670016",
      "title": "Twitter / jdorfman: nice @littlebigdetail from ..."
    }
  ]
}
```

However, in practice, this format isn't very conducive to chat client utility. That is, a chat client must be able to identify the **substrings** of importance within the **user-input message**, and then use **well-structured valid data** to present the message in a useful manner.

I accomplished this by breaking the problem down:

1. Identify **Patterns** to find occurrences of important character sequences within a message (see: [MessageTagType.java](MessageTagType.java))
2. Create a `MessageTag` containing the `messageTagType` (mention, emoticon, link) and the `start/end` indicies of the character sequence within the **message**. (see: [MessageContentParser.java](MessageContentParser.java))
3. For each `MessageTag`, ensure the tag references a valid resource (existing user, defined emoticon), and if so, add the structured data to the `MessageContent` [model](MessageContent.java) to be consumed by clients.
4. Format the `MessageContent` to comply with the provided JSON contract. (see: [ObjectMapperProvider.java](ObjectMapperProvider.java))

## Demo

I exposed this functionality as a web service using **Jetty**, **Jersey**, and **Jackson**.

### Requirements
* JDK 8
* Maven 3
* cURL

### Sending Requests
```
mvn clean jetty:run
curl -X POST -H "Accept: application/json" -H "Content-Type: text/plain" -d "\\@all Please read the following memo (important): http://www.wsj.com/" http://localhost:8080/messages
```

### Unit/Integration Tests
```
mvn clean test
```
