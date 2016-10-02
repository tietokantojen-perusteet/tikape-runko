package tikape.runko.domain;

import java.util.ArrayList;
import java.util.List;

public class MessageThread {

    private List<Message> messages;
    private final int subCatId;
    private final int userId;
    private final String title;
    private final String creationDate;

    public MessageThread(int subCatId, int userId, String title, String creationDate) {
        messages = new ArrayList<>();
        this.subCatId = subCatId;
        this.userId = userId;
        this.title = title;
        this.creationDate = creationDate;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void addAllMessages(List<Message> msgs) {
        messages.addAll(msgs);
    }
    
    public List<Message> getMessages(){
        return messages;
    }

    public int getMessageCount() {
        return messages.size();
    }

    public int getLatestPostUserId() {
        if (messages.size() > 0) {
            return messages.get(messages.size() - 1).getUserId();
        }
        return -1;
    }

    public int getSubCategoryId() {
        return subCatId;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }
    
    public String getTimeStamp(){
        return creationDate;
    }

    @Override
    public String toString() {
        return title + " (" + creationDate + ")";
    }

}
