package botscrew.com.myTask.responseBuilder;

import botscrew.com.myTask.Testing;
import botscrew.com.myTask.model.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GenericTemplateSender extends Sender {
    List<GenericTemplateData> genericTemplateData;

    public String sendGenericTemplate() {
        SentMessage sentMessage = new SentMessage();
        Recipient recipient = new Recipient();
        recipient.setId(recipientId);
        sentMessage.setRecipient(recipient);
        Message message = new Message();
        Attachment attachment = new Attachment();
        attachment.setType("template");
        Payload payload = new Payload();
        payload.setTemplateType("generic");
        List<Elements> elementsList = new ArrayList<>();
        for (GenericTemplateData x : genericTemplateData) {
            Elements elements = new Elements();
            elements.setTitle(x.getTitle());
            elements.setImageUrl(x.getImageUrl());
            elements.setSubtitle(x.getSubtitle());
            if (x.getFallbackUrl()!=null) {
                DefaultAction defaultAction = new DefaultAction();
                defaultAction.setType("web_url");
                defaultAction.setUrl(x.getDefaultActionUrl());
                defaultAction.setMessengerExtensions("true");
                defaultAction.setWebviewHeightRatio("tall");
                defaultAction.setFallbackUrl(x.getFallbackUrl());
                elements.setDefaultAction(defaultAction);
            }
            elements.setButtons(x.getButtonsList());
            elementsList.add(elements);
        }
        payload.setElements(elementsList);
        attachment.setPayload(payload);
        message.setAttachment(attachment);
        sentMessage.setMessage(message);
        return super.sendObject(sentMessage);
    }
}

