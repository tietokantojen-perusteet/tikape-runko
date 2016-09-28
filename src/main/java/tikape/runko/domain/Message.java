package tikape.runko.domain;

public class Message {

    private final String body;
    private final int userId;
    private final int messageId;
    private final String timeStamp;

    public Message(int messageId, int userId, String body, String timeStamp) {
        this.messageId = messageId;
        this.userId = userId;
        this.body = body;
        this.timeStamp = timeStamp;
    }

    public String getBody() {
        return body;
    }

    public int getUserId() {
        return userId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
}
