package botscrew.com.myTask.responseBuilder;

import botscrew.com.myTask.model.CallToActions;
import botscrew.com.myTask.model.Greeting;
import botscrew.com.myTask.model.SentMessage;

import java.util.ArrayList;
import java.util.List;

public class Starter extends Sender{
    private  void setStartButton(){
        SentMessage sentMessage = new SentMessage();
        sentMessage.setSettingType("callToActions");
        sentMessage.setThreadState("new_thread");
        List<CallToActions> call_to_actions = new ArrayList<>();
        CallToActions callToActions_element = new CallToActions();
        callToActions_element.setPayload("USER_DEFINED_PAYLOAD");
        call_to_actions.add(callToActions_element);
        sentMessage.setCallToActions(call_to_actions);
        super.sendObject(sentMessage);
    }
    private  void setGreeting(){
        String text = "Hello, it is my test bot";
        SentMessage sentMessage = new SentMessage();
        sentMessage.setSettingType("greeting");
        Greeting greeting = new Greeting();
        greeting.setText(text);
        sentMessage.setGreeting(greeting);
        super.sendObject(sentMessage);
    }
    public  void setGetStarted(){
        setStartButton();
        setGreeting();
    }
}
