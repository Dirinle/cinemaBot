package botscrew.com.myTask.responseBuilder;

import botscrew.com.myTask.model.Message;
import botscrew.com.myTask.model.Recipient;
import botscrew.com.myTask.model.SentMessage;

public class TextSender extends Sender {
    public void sendText(String text){
        SentMessage sentMessage = new SentMessage();
        Recipient recipient = new Recipient();
        recipient.setId(recipientId);
        sentMessage.setRecipient(recipient);

        Message message = new Message();
        message.setText(text);
        sentMessage.setMessage(message);
        super.sendObject(sentMessage);
    }
}
