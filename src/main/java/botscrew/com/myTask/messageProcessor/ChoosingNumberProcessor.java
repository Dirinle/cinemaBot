package botscrew.com.myTask.messageProcessor;

import botscrew.com.myTask.dataBaseProcessor.cinema.Cinema;
import botscrew.com.myTask.dataBaseProcessor.cinema.CinemaService;
import botscrew.com.myTask.dataBaseProcessor.film.Film;
import botscrew.com.myTask.dataBaseProcessor.film.FilmService;
import botscrew.com.myTask.dataBaseProcessor.progress.Progress;
import botscrew.com.myTask.dataBaseProcessor.progress.ProgressService;
import botscrew.com.myTask.dataBaseProcessor.progress.Step;
import botscrew.com.myTask.model.AcceptedMessage;
import botscrew.com.myTask.model.Buttons;
import botscrew.com.myTask.reader.AcceptedMessageReader;
import botscrew.com.myTask.responseBuilder.GenericTemplateData;
import botscrew.com.myTask.responseBuilder.GenericTemplateSender;
import botscrew.com.myTask.responseBuilder.QuickRepliesSender;
import botscrew.com.myTask.responseBuilder.TextSender;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class ChoosingNumberProcessor implements Processor {
    @Autowired
    private ProgressService progressService;
    @Autowired
    private FilmService filmService;
    @Autowired
    private CinemaService cinemaService;
    private String recipientId;

    public ChoosingNumberProcessor() {

    }

    @Override
    public void run(AcceptedMessage acceptedMessage) {
        AcceptedMessageReader reader = new AcceptedMessageReader();
        recipientId = reader.getId(acceptedMessage);
        TextSender textSender = new TextSender();
        textSender.setRecipientId(recipientId);
        Progress progress = progressService.getProgress(recipientId);
        String message = reader.getText(acceptedMessage);

        if (isBackChosen(message)){
            return;
        }
        switch (chooseNextAction(message)) {
            case sendNumberAgain: {
                sendNumberOfTicketReplies(recipientId);
                break;
            }
            case sendResult: {
                progress.setNumber(Integer.parseInt(message));
                sendResult(progress);
                progressService.save(new Progress(recipientId));
                start();
                break;
            }
            case sendBackChoose: {
                QuickRepliesSender quickRepliesSender = new QuickRepliesSender();
                quickRepliesSender.setRecipientId(recipientId);
                quickRepliesSender.sendQuickReplies(
                        "Unfortunately we can not execute this order\n" +
                                "Please re-select some data",
                        new String[]{"date", "cinema", "technology", "time", "number"}
                );
                break;
            }
        }
    }

    private boolean isBackChosen(String  message) {
        Progress progress = progressService.getProgress(recipientId);
        switch (message){
            case "date":{
                progress.setStep(Step.dateChoosing);
                sendDates("Choose date or enter in yyyy-mm-dd format", recipientId);
                break;
            }
            case "cinema":{
                progress.setStep(Step.cinemaChoosing);
                sendCinemasByFilmIdAndDate(progress);
                break;
            }
            case "technology":{
                sendTechnologyReplies(recipientId);
                progress.setStep(Step.technologyChoosing);
                break;
            }
            case "time":{
                QuickRepliesSender quickRepliesSender = new QuickRepliesSender();
                quickRepliesSender.setRecipientId(recipientId);
                List<Time>times= filmService.getTimeOfSessions();
                List<String>timeAsStringList = getListStringFromTime(times);
                quickRepliesSender.sendQuickReplies("Choose time",timeAsStringList);
                progress.setStep(Step.timeChoosing);
                break;
            }
            case "number":{
                sendNumberOfTicketReplies(recipientId);
                progress.setStep(Step.numberChoosing);
                break;
            }
            default:
                return false;
        }
        progressService.save(progress);
        return true;
    }

    private void sendResult(Progress progress) {
        Film film = filmService.getFilmById(progress.getFilmId());
        String filmName = film.getName();
        String cinemaName = filmService.getCinemaNameById(progress.getCinemaId());
        String messageText = "Great, I have booked for you " + progress.getNumber() +
                " tickets .\n" + filmName +
                " in " + cinemaName +
                " at " + progress.getDate() +
                " at " + progress.getTime().substring(0, progress.getTime().length() - 3) +
                " .Tickets will be waiting for you in the ticket office.";
        GenericTemplateSender genericTemplateSender = new GenericTemplateSender();
        genericTemplateSender.setRecipientId(recipientId);
        List<GenericTemplateData> dataList = new ArrayList<>();
        GenericTemplateData data = new GenericTemplateData();
        data.setTitle(filmName);
        data.setImageUrl(film.getPoster());
        List<Buttons> buttons = new ArrayList<>();
        Buttons button = new Buttons();
        button.setTitle("Watch trailer");
        button.setType("web_url");
        button.setUrl(film.getTrailerUrl());
        buttons.add(button);
        data.setButtonsList(buttons);
        dataList.add(data);
        TextSender textSender = new TextSender();
        textSender.setRecipientId(recipientId);
        textSender.sendText(messageText);
        genericTemplateSender.setGenericTemplateData(dataList);
        genericTemplateSender.sendGenericTemplate();
    }

    public void sendNumberOfTicketReplies(String recipientId) {
        QuickRepliesSender quickRepliesSender = new QuickRepliesSender();
        quickRepliesSender.setRecipientId(recipientId);
        quickRepliesSender.sendQuickReplies("Choose number of tickets from buttons or enter",
                new String[]{"1", "2", "3", "4", "5", "6"});
    }

    private ActionState chooseNextAction(String message) {
        TextSender textSender = new TextSender();
        textSender.setRecipientId(recipientId);
        Progress progress = progressService.getProgress(recipientId);
        Integer number = null;
        try {
            number = Integer.parseInt(message);
        } catch (Exception e) {
            textSender.sendText("Invalid input");
            return ActionState.sendNumberAgain;
        }
        if (number < 1) {
            textSender.sendText("Invalid input");
            return ActionState.sendNumberAgain;
        }
        Integer maxTickets = filmService.getNumberOfTicketsByProgress(progress);
        if (number > maxTickets) {
            textSender.sendText("Not enough tickets");
            return ActionState.sendBackChoose;
        }
        filmService.setTickets(maxTickets-number,progress);
        return ActionState.sendResult;
    }

    private enum ActionState {
        sendResult, sendNumberAgain, sendBackChoose;
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

    public void sendTechnologyReplies(String recipientId) {
        String[] buttons = {"2d", "3d"};
        QuickRepliesSender quickRepliesSender = new QuickRepliesSender();
        quickRepliesSender.setRecipientId(recipientId);
        quickRepliesSender.sendQuickReplies("Choose technology", buttons);
    }

    public void sendCinemasByFilmIdAndDate(Progress progress) {
        List<String> cinemasChoosing = getCinemaNames(progress);
        cinemasChoosing.add("Find closes");
        QuickRepliesSender quickRepliesSender = new QuickRepliesSender();
        quickRepliesSender.setRecipientId(progress.getId());
        quickRepliesSender.sendQuickReplies("Select cinema", cinemasChoosing);
    }

    private List<String> getCinemaNames(Progress progress) {
        Integer filmId = progress.getFilmId();
        String date = progress.getDate();
        List<Cinema> cinemas = cinemaService.getCinemaByIdFilmAndDate(filmId, date);
        List<String> cinemasNames = new ArrayList<>();
        cinemas.stream().forEach(x -> cinemasNames.add(x.getName()));
        return cinemasNames;
    }

    public List<String> getListStringFromTime(List<Time> timeList) {
        List<String> stringList = new ArrayList<>();
        timeList.stream().forEach(x -> stringList.add(x.toString().substring(0, x.toString().length() - 3)));
        return stringList;
    }

    private void start() {
        QuickRepliesSender quickRepliesSender = new QuickRepliesSender();
        quickRepliesSender.setRecipientId(recipientId);
        quickRepliesSender.sendQuickReplies("Please select of above",
                new String[]{"Today`s list", "Find the movie"});
    }
}
