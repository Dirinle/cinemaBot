package botscrew.com.myTask.responseBuilder;

import botscrew.com.myTask.Testing;
import botscrew.com.myTask.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ButtonSender extends Sender {
    public String sendButtons(String text, List<Buttons>buttonsList) {
        SentMessage sentMessage = new SentMessage();
        Recipient recipient = new Recipient();
        recipient.setId(recipientId);
        sentMessage.setRecipient(recipient);
        Message message = new Message();
        Attachment attachment = new Attachment();
        attachment.setType("template");
        Payload payload = new Payload();
        payload.setTemplateType("button");
        payload.setText(text);
        payload.setButtons(buttonsList);
        attachment.setPayload(payload);
        message.setAttachment(attachment);
        sentMessage.setMessage(message);
        Testing.outputAsJson(sentMessage);
        return super.sendObject(sentMessage);
    }

}
