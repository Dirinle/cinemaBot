package botscrew.com.myTask.messageProcessor;

import botscrew.com.myTask.Testing;
import botscrew.com.myTask.dataBaseProcessor.progress.Progress;
import botscrew.com.myTask.dataBaseProcessor.progress.ProgressService;
import botscrew.com.myTask.dataBaseProcessor.progress.Step;
import botscrew.com.myTask.model.AcceptedMessage;
import botscrew.com.myTask.model.User;
import botscrew.com.myTask.reader.AcceptedMessageReader;
import botscrew.com.myTask.responseBuilder.QuickRepliesSender;
import botscrew.com.myTask.responseBuilder.TextSender;
import botscrew.com.myTask.responseBuilder.Typing;
import botscrew.com.myTask.responseBuilder.UserRequestSender;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MainProcessor implements Processor {
    @Autowired
    private ProgressService progressService;
    @Autowired
    private ChoosingFilmProcessor choosingFilmProcessor;
    @Autowired
    private ChoosingCinemaProcessor choosingCinemaProcessor;
    @Autowired
    private ChoosingNumberProcessor choosingNumberProcessor;
    @Autowired
    private ChoosingTechnologyProcessor choosingTechnologyProcessor;
    @Autowired
    private ChoosingDateProcessor choosingDateProcessor;
    @Autowired
    private ChoosingTimeProcessor choosingTimeProcessor;
    private Progress progress;
    private String recipientId;

    public MainProcessor() {
    }

    @Override
    public void run(AcceptedMessage acceptedMessage) {
        AcceptedMessageReader reader = new AcceptedMessageReader();
        recipientId = reader.getId(acceptedMessage);
        Typing typing = new Typing();
        typing.setRecipientId(recipientId);
        typing.typingOn();
        if (reader.isRestarted(acceptedMessage)
                || reader.isGetStarted(acceptedMessage)) {
            String name  = getNameById(recipientId);
            greeting(name);
            progressService.save(new Progress(recipientId));
            typing.typingOff();
            return;
        } else
            progress = progressService.getProgress(recipientId);
        switch (progress.getStep()) {
            case filmChoosing: {
                choosingFilmProcessor.run(acceptedMessage);
                break;
            }
            case dateChoosing: {
                choosingDateProcessor.run(acceptedMessage);
                break;
            }
            case cinemaChoosing: {
                choosingCinemaProcessor.run(acceptedMessage);
                break;
            }
            case technologyChoosing: {
                choosingTechnologyProcessor.run(acceptedMessage);
                break;
            }
            case timeChoosing: {
                choosingTimeProcessor.run(acceptedMessage);
                break;
            }
            case numberChoosing: {
                choosingNumberProcessor.run(acceptedMessage);
                break;
            }
            default: {
                TextSender textSender = new TextSender();
                textSender.setRecipientId(recipientId);
                textSender.sendText("some mistake");
            }
        }
        typing.typingOff();
    }

    private String getNameById(String recipientId) {
        String resultJson = new UserRequestSender().getUserName(recipientId);
        User user = null;
        try {
            user = new ObjectMapper().readValue(resultJson,User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (user!=null)
            return user.getFirst_name();
        return null;
    }


    private void greeting(String name) {
        QuickRepliesSender quickRepliesSender = new QuickRepliesSender();
        quickRepliesSender.setRecipientId(recipientId);
        quickRepliesSender.sendQuickReplies("Hi, " + name + " my name is Isaac The Cinema Bot. \n" +
                        "I will help you choose a movie and book tickets",
                new String[]{"Today`s list", "Find the movie"});
    }
}
