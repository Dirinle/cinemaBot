package botscrew.com.myTask.responseBuilder;

import botscrew.com.myTask.model.Recipient;
import botscrew.com.myTask.model.SentMessage;

public class Typing extends Sender{
    private SentMessage getTypingObject(boolean state){
        SentMessage sentMessage = new SentMessage();
        Recipient recipient = new Recipient();
        recipient.setId(super.getRecipientId());
        sentMessage.setRecipient(recipient);
        if (state)
            sentMessage.setSenderAction("typing_on");
        else
            sentMessage.setSenderAction("typing_off");
        return sentMessage;
    }

    public void typingOn(){
        super.sendObject(getTypingObject(true));
    }

    public void typingOff(){
        super.sendObject(getTypingObject(false));
    }
}
