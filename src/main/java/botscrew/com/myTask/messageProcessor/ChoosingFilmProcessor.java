package botscrew.com.myTask.messageProcessor;

import botscrew.com.myTask.Testing;
import botscrew.com.myTask.dataBaseProcessor.film.Film;
import botscrew.com.myTask.dataBaseProcessor.film.FilmService;
import botscrew.com.myTask.dataBaseProcessor.progress.Progress;
import botscrew.com.myTask.dataBaseProcessor.progress.ProgressService;
import botscrew.com.myTask.dataBaseProcessor.progress.Step;
import botscrew.com.myTask.model.AcceptedMessage;
import botscrew.com.myTask.model.Buttons;
import botscrew.com.myTask.reader.AcceptedMessageReader;
import botscrew.com.myTask.responseBuilder.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/*
* "willBeChosen" genre means that genre will be chosen in next step
* OR film with chosen genre does not exist
* if genre !=null and genre!="willBeChosen" it means that user already get list of films
* */
@Component
@Data
public class ChoosingFilmProcessor implements Processor {
    @Autowired
    private ProgressService progressService;
    @Autowired
    private FilmService filmService;
    @Autowired
    private ChoosingCinemaProcessor choosingCinemaProcessor;
    @Autowired
    private ChoosingDateProcessor choosingDateProcessor;
    private String recipientId;
    private Progress progress;

    public ChoosingFilmProcessor() {
    }

    @Override
    public void run(AcceptedMessage acceptedMessage) {
        AcceptedMessageReader reader = new AcceptedMessageReader();
        recipientId = reader.getId(acceptedMessage);
        progress = progressService.getProgress(recipientId);
        String today = LocalDate.now().toString();
        if (progress.getDate() == null && progress.getGenre() == null) {
            if (reader.getText(acceptedMessage) != null)
                startProcessor(acceptedMessage);
        } else if (("willBeChosen").equals(progress.getGenre())) {
            if (sendFilmsByGenre(reader.getText(acceptedMessage)))
                progress.setGenre(reader.getText(acceptedMessage));
        } else if (progress.getDate() != null ||
                (!("willBeChosen").equals(progress.getGenre())
                        && progress.getGenre() != null)) {
            Integer id = Integer.parseInt(reader.getPostbackPayload(acceptedMessage));
            progress.setFilmId(id);
            if (progress.getDate() != null) {
                progress.setStep(Step.cinemaChoosing);
                choosingCinemaProcessor.sendCinemasByFilmIdAndDate(progress);
            } else {
                choosingDateProcessor.sendDates("Choose date or enter in yyyy-mm-dd format", recipientId);
                progress.setStep(Step.dateChoosing);
            }
        }

        progressService.save(progress);
    }


    private void startProcessor(AcceptedMessage acceptedMessage) {
        String messageText = new AcceptedMessageReader().getText(acceptedMessage);
        switch (messageText) {
            case "Today`s list": {
                sendTodayFilms();
                String today = LocalDate.now().toString();
                progress.setDate(today);
                break;
            }
            case "Find the movie": {
                QuickRepliesSender sender = new QuickRepliesSender();
                sender.setRecipientId(recipientId);
                List<String> genres = filmService.getGenres();
                String[] genresArray = genres.stream().toArray(String[]::new);
                sender.sendQuickReplies("What genre do you want to watch?", genresArray);
                progress.setGenre("willBeChosen");
                break;
            }
            default: {
                QuickRepliesSender quickRepliesSender = new QuickRepliesSender();
                quickRepliesSender.setRecipientId(recipientId.toString());
                quickRepliesSender.sendQuickReplies("Please choose between this",
                        new String[]{"Today`s list", "Find the movie"});
            }
        }
    }

    private boolean sendFilmsByGenre(String genre) {
        List<Film> films = filmService.getFilmsByGenre(genre);
        if (films.isEmpty()) {
            QuickRepliesSender sender = new QuickRepliesSender();
            sender.setRecipientId(recipientId);
            List<String> genres = filmService.getGenres();
            sender.sendQuickReplies("Unfortunately here are not films with this genre. Please choose another", genres);
            return false;
        }
        sendFilms(films);
        return true;
    }

    private void sendTodayFilms() {
        List<Film> films = filmService.getTodayFilms();
        if (films.isEmpty()) {
            TextSender textSender = new TextSender();
            textSender.setRecipientId(recipientId);
            textSender.sendText("Unfortunately are not films today");
            return;
        }
        sendFilms(films);
    }

    private void sendFilms(List<Film> films) {
        GenericTemplateSender sender = new GenericTemplateSender();
        sender.setRecipientId(recipientId);
        List<GenericTemplateData> dataList = new ArrayList<>();
        for (Film film :
                films) {
            GenericTemplateData data = new GenericTemplateData();
            data.setImageUrl(film.getPoster());
            data.setTitle(film.getName());
            List<Buttons> buttonsList = new ArrayList<>();
            Buttons button1 = new Buttons();
            button1.setTitle("Watch trailer");
            button1.setUrl(film.getTrailerUrl());
            button1.setType("web_url");
            Buttons button2 = new Buttons();
            button2.setType("postback");
            button2.setTitle("Buy ticket");
            button2.setPayload(film.getId().toString());
            buttonsList.add(button1);
            buttonsList.add(button2);
            data.setButtonsList(buttonsList);
            dataList.add(data);
        }
        sender.setGenericTemplateData(dataList);
        sender.sendGenericTemplate();
    }

}
