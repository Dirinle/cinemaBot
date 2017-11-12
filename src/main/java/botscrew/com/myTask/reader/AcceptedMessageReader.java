package botscrew.com.myTask.reader;

import botscrew.com.myTask.model.AcceptedMessage;
import botscrew.com.myTask.model.Coordinates;
import botscrew.com.myTask.model.Recipient;

public class AcceptedMessageReader {
    public String getId(AcceptedMessage acceptedMessage) {
        return acceptedMessage.getEntry().get(0).getMessaging().get(0).getSender().getId();
    }

    public String getText(AcceptedMessage acceptedMessage) {
        if (acceptedMessage.getEntry() == null)
            return null;
        else if (acceptedMessage.getEntry().get(0).getMessaging() == null)
            return null;
        else if (acceptedMessage.getEntry().get(0).getMessaging().get(0).getMessage() == null)
            return null;
        else
            return acceptedMessage.getEntry().get(0).getMessaging().get(0).getMessage().getText();
    }

    public boolean isRestarted(AcceptedMessage acceptedMessage) {
        if (acceptedMessage.getEntry() == null)
            return false;
        else if (acceptedMessage.getEntry().get(0).getMessaging() == null)
            return false;
        else if (acceptedMessage.getEntry().get(0).getMessaging().get(0).getPostback() == null)
            return false;
        else
            return ("restart").equals(acceptedMessage.getEntry().get(0).getMessaging().get(0).getPostback().getPayload());
    }

    public boolean isGetStarted(AcceptedMessage acceptedMessage) {
        if (acceptedMessage.getEntry() == null)
            return false;
        else if (acceptedMessage.getEntry().get(0).getMessaging() == null)
            return false;
        else if (acceptedMessage.getEntry().get(0).getMessaging().get(0).getPostback() == null)
            return false;
        else
            return ("get_started").equals(acceptedMessage.getEntry().get(0).getMessaging().get(0).getPostback().getPayload());
    }

    public String getPostbackPayload(AcceptedMessage message){
        if (message.getEntry().get(0).getMessaging()==null)
            return null;
        else if (message.getEntry().get(0).getMessaging().get(0).getPostback()==null)
            return null;
        else
            return message.getEntry().get(0).getMessaging().get(0).getPostback().getPayload();
    }

    public Coordinates getCoordinates(AcceptedMessage message){
        if (message.getEntry().get(0).getMessaging()==null)
            return null;
        else if (message.getEntry().get(0).getMessaging().get(0).getMessage()==null)
            return null;
        else if (message.getEntry().get(0).getMessaging().get(0).getMessage().getAttachments()==null)
            return null;
        else
        return message.getEntry().get(0).getMessaging().get(0).getMessage().getAttachments().get(0).getPayload().getCoordinates();
    }
}
