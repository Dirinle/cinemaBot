package botscrew.com.myTask.messageProcessor;

import botscrew.com.myTask.model.AcceptedMessage;

public interface Processor {
    public void run(AcceptedMessage acceptedMessage);
}
