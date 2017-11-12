package botscrew.com.myTask.messageProcessor;

import botscrew.com.myTask.dataBaseProcessor.film.FilmService;
import botscrew.com.myTask.dataBaseProcessor.progress.Progress;
import botscrew.com.myTask.dataBaseProcessor.progress.ProgressService;
import botscrew.com.myTask.dataBaseProcessor.progress.Step;
import botscrew.com.myTask.model.AcceptedMessage;
import botscrew.com.myTask.reader.AcceptedMessageReader;
import botscrew.com.myTask.responseBuilder.QuickRepliesSender;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class ChoosingTimeProcessor implements Processor {
    @Autowired
    private ProgressService progressService;
    @Autowired
    private FilmService filmService;
    @Autowired
    private ChoosingNumberProcessor choosingNumberProcessor;
    private String recipientId;

    public ChoosingTimeProcessor() {
    }

    @Override
    public void run(AcceptedMessage acceptedMessage) {
        AcceptedMessageReader reader = new AcceptedMessageReader();
        recipientId = reader.getId(acceptedMessage);
        Progress progress = progressService.getProgress(recipientId);
        String messageText = reader.getText(acceptedMessage);
        List<Time> presentTimes = filmService.getTimeOfSessions();//list of possible time to choose
        List<String> presentTimesAsStringList = getListStringFromTime(presentTimes);
        if (presentTimesAsStringList.stream().filter(x -> x.equals(messageText)).count() > 0) {
            progress.setTime(messageText+":00");
            progress.setStep(Step.numberChoosing);
            choosingNumberProcessor.sendNumberOfTicketReplies(recipientId);
        } else {
            QuickRepliesSender quickRepliesSender = new QuickRepliesSender();
            quickRepliesSender.setRecipientId(recipientId);
            quickRepliesSender.sendQuickReplies("Please choose again",presentTimesAsStringList);
        }
        progressService.save(progress);
    }

    public List<String> getListStringFromTime(List<Time> timeList) {
        List<String> stringList = new ArrayList<>();
        timeList.stream().forEach(x -> stringList.add(x.toString().substring(0, x.toString().length() - 3)));
        return stringList;
    }

}
