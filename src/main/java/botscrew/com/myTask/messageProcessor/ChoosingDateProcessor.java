package botscrew.com.myTask.messageProcessor;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@Data
public class ChoosingDateProcessor implements Processor {
    @Autowired
    private ProgressService progressService;
    @Autowired
    private ChoosingCinemaProcessor choosingCinemaProcessor;
    private String recipientId;

    public ChoosingDateProcessor() {

    }

    @Override

    public void run(AcceptedMessage acceptedMessage) {
        AcceptedMessageReader reader = new AcceptedMessageReader();
        String recipientId = reader.getId(acceptedMessage);
        Progress progress = progressService.getProgress(recipientId);
        String date = reader.getText(acceptedMessage);
        LocalDate today = LocalDate.now();
        if (date==null){
            TextSender textSender = new TextSender();
            textSender.setRecipientId(recipientId);
            textSender.sendText("null");
            return;
        }
        if (date.equals("Today"))
            date = today.toString();
        else if (date.equals("Tomorrow"))
            date = today.plus(1, ChronoUnit.DAYS).toString();
        if (isValidDate(date)) {
            progress.setDate(date);
            progress.setStep(Step.cinemaChoosing);
            progressService.save(progress);
            choosingCinemaProcessor.sendCinemasByFilmIdAndDate(progress);
        }
        else {
            sendDates("Please enter valid date", recipientId);
        }
    }

    public void sendDates(String text, String recipientId) {
        LocalDate today = LocalDate.now();
        LocalDate replyDate1 = today.plus(2, ChronoUnit.DAYS);
        LocalDate replyDate2 = today.plus(3, ChronoUnit.DAYS);
        LocalDate replyDate3 = today.plus(4, ChronoUnit.DAYS);
        String[] replies = new String[5];
        replies[0] = "Today";
        replies[1] = "Tomorrow";
        replies[2] = replyDate1.toString();
        replies[3] = replyDate2.toString();
        replies[4] = replyDate3.toString();
        QuickRepliesSender sender = new QuickRepliesSender();
        sender.setRecipientId(recipientId);
        sender.sendQuickReplies(text, replies);
    }

    private boolean isValidDate(String dateString) {
        Date date = null;
        Date todayDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            todayDate = sdf.parse(LocalDate.now().toString());
            date = sdf.parse(dateString);
            if (!dateString.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        if (date == null) {
            return false;
        } else {
            if (date.compareTo(todayDate) == -1)
                return false;
            return true;
        }
    }

}
