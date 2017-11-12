package botscrew.com.myTask.messageProcessor;

import botscrew.com.myTask.dataBaseProcessor.film.FilmService;
import botscrew.com.myTask.dataBaseProcessor.progress.Progress;
import botscrew.com.myTask.dataBaseProcessor.progress.ProgressService;
import botscrew.com.myTask.dataBaseProcessor.progress.Step;
import botscrew.com.myTask.model.AcceptedMessage;
import botscrew.com.myTask.reader.AcceptedMessageReader;
import botscrew.com.myTask.responseBuilder.QuickRepliesSender;
import botscrew.com.myTask.responseBuilder.TextSender;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.util.List;

@Component
@Data
public class ChoosingTechnologyProcessor implements Processor {
    private String recipientId;

    @Autowired
    private ProgressService progressService;
    @Autowired
    private FilmService filmService;
    @Autowired
    private ChoosingTimeProcessor choosingTimeProcessor;
    public ChoosingTechnologyProcessor() {

    }

    @Override
    public void run(AcceptedMessage acceptedMessage) {
        AcceptedMessageReader reader = new AcceptedMessageReader();
        recipientId = reader.getId(acceptedMessage);
        Progress progress = progressService.getProgress(recipientId);
        String messageText = reader.getText(acceptedMessage);
        if (messageText.equals("2d")||messageText.equals("3d")){
            progress.setTechnology(messageText);
            progress.setStep(Step.timeChoosing);
            QuickRepliesSender quickRepliesSender = new QuickRepliesSender();
            quickRepliesSender.setRecipientId(recipientId);
            List<Time>times= filmService.getTimeOfSessions();
            List<String>timeAsSrtingList = choosingTimeProcessor.getListStringFromTime(times);
            quickRepliesSender.sendQuickReplies("Choose time",timeAsSrtingList);
        }
        else
            sendTechnologyReplies(recipientId);
        progressService.save(progress);
    }

    public void sendTechnologyReplies(String recipientId) {
        String[] buttons = {"2d", "3d"};
        QuickRepliesSender quickRepliesSender = new QuickRepliesSender();
        quickRepliesSender.setRecipientId(recipientId);
        quickRepliesSender.sendQuickReplies("Choose technology", buttons);
    }
}
