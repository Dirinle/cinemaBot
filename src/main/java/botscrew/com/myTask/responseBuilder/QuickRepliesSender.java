package botscrew.com.myTask.responseBuilder;

import botscrew.com.myTask.Testing;
import botscrew.com.myTask.model.Message;
import botscrew.com.myTask.model.QuickReplies;
import botscrew.com.myTask.model.Recipient;
import botscrew.com.myTask.model.SentMessage;

import java.util.ArrayList;
import java.util.List;

public class QuickRepliesSender extends Sender {
    public String sendQuickReplies(String text, String[] buttonsTitle) {
        SentMessage sentMessage = new SentMessage();
        Recipient recipient = new Recipient();
        recipient.setId(recipientId);
        sentMessage.setRecipient(recipient);
        Message message = new Message();
        if (text == null)
            message.setText("default text, not implemented yet");
        else
            message.setText(text);
        List<QuickReplies> quickReplies = new ArrayList<>();
        for (int i = 0; i < buttonsTitle.length; i++) {
            QuickReplies quickRepliesElement = new QuickReplies();
            quickRepliesElement.setContentType("text");
            quickRepliesElement.setTitle(buttonsTitle[i]);
            quickRepliesElement.setPayload("<POSTBACK_PAYLOAD>");
            quickReplies.add(quickRepliesElement);
        }
        message.setQuickReplies(quickReplies);
        sentMessage.setMessage(message);
        return super.sendObject(sentMessage);
    }

    public String sendQuickReplies(String text, List<String> buttonsTitle) {
        String[] buttonsTitleArray = buttonsTitle.stream().toArray(String[]::new);
        return sendQuickReplies(text, buttonsTitleArray);
    }

    public String sendQuickRepliesSingle(String text, String buttonTitle) {
        return sendQuickReplies(text, new String[]{buttonTitle});
    }

    public String sendLocation(String text) {

        SentMessage sentMessage = new SentMessage();
        Recipient recipient = new Recipient();
        recipient.setId(recipientId);
        sentMessage.setRecipient(recipient);
        Message message = new Message();
        if (text == null)
            message.setText("default text, not implemented yet");
        else
            message.setText(text);
        List<QuickReplies> quickReplies = new ArrayList<>();
        QuickReplies quickRepliesElement = new QuickReplies();
        quickRepliesElement.setContentType("location");
        quickReplies.add(quickRepliesElement);
        message.setQuickReplies(quickReplies);
        sentMessage.setMessage(message);
        return super.sendObject(sentMessage);
    }
}
