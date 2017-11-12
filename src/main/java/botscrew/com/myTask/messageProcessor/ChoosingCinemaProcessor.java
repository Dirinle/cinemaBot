package botscrew.com.myTask.messageProcessor;

import botscrew.com.myTask.dataBaseProcessor.cinema.Cinema;
import botscrew.com.myTask.dataBaseProcessor.cinema.CinemaService;
import botscrew.com.myTask.dataBaseProcessor.progress.Progress;
import botscrew.com.myTask.dataBaseProcessor.progress.ProgressService;
import botscrew.com.myTask.dataBaseProcessor.progress.Step;
import botscrew.com.myTask.model.AcceptedMessage;
import botscrew.com.myTask.model.Buttons;
import botscrew.com.myTask.model.Coordinates;
import botscrew.com.myTask.model.google.GoogleMatrixResponse;
import botscrew.com.myTask.reader.AcceptedMessageReader;
import botscrew.com.myTask.responseBuilder.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
@Data
public class ChoosingCinemaProcessor implements Processor {
    @Autowired
    private ProgressService progressService;
    @Autowired
    private CinemaService cinemaService;
    @Autowired
    private ChoosingTechnologyProcessor choosingTechnologyProcessor;
    private String recipientId;

    public ChoosingCinemaProcessor() {
    }

    @Override
    public void run(AcceptedMessage acceptedMessage) {
        AcceptedMessageReader reader = new AcceptedMessageReader();
        recipientId = reader.getId(acceptedMessage);
        Progress progress = progressService.getProgress(recipientId);
        String messageText;
        List<String> cinemaNameList = getCinemaNames(progress);
        TextSender textSender = new TextSender();
        textSender.setRecipientId(recipientId);
        Coordinates coordinates = reader.getCoordinates(acceptedMessage);
        if (reader.getPostbackPayload(acceptedMessage)!=null){
            messageText=reader.getPostbackPayload(acceptedMessage);
        }
        else {
            messageText=reader.getText(acceptedMessage);
        }
        if (coordinates != null) {
            sendFilmsByDistance(progress.getFilmId(),progress.getDate(),coordinates);
        } else if (messageText.equals("Find closest")) {
            QuickRepliesSender quickRepliesSender = new QuickRepliesSender();
            quickRepliesSender.setRecipientId(recipientId);
            quickRepliesSender.sendLocation("Please send your location");
        } else if (cinemaNameList.stream().filter(x -> x.equals(messageText)).count() > 0) {//chosen cinema that contains in list of cinemas
            Integer cinemaId = cinemaService.getCinemaIdByName(messageText);
            progress.setCinemaId(cinemaId);
            choosingTechnologyProcessor.sendTechnologyReplies(recipientId);
            progress.setStep(Step.technologyChoosing);
        } else {
            textSender.sendText("Please try again");
            sendCinemasByFilmIdAndDate(progress);
        }
        progressService.save(progress);
    }

    private void sendFilmsByDistance(Integer idFilm, String date, Coordinates origins) {
        List<Cinema> cinemas = cinemaService.getCinemaByIdFilmAndDate(idFilm, date);
        List<CinemaWithDistance>cinemasWithDistance =  new ArrayList<>();
        cinemas.stream().forEach(x->cinemasWithDistance.add(new CinemaWithDistance(x,
                getDistance(origins, new Coordinates(x.getLongitude(),x.getLatitude())))));
        Comparator<CinemaWithDistance>comparator =
                Comparator.comparing(c -> c.distance);
        Collections.sort(cinemasWithDistance, comparator);
        GenericTemplateSender genericTemplateSender = new GenericTemplateSender();
        genericTemplateSender.setRecipientId(recipientId);
        List<GenericTemplateData> dataList = new ArrayList<>();
        for (CinemaWithDistance cinemaWD :
                cinemasWithDistance) {
            GenericTemplateData data = new GenericTemplateData();
            data.setImageUrl(cinemaWD.cinema.getImageUrl());
            data.setTitle(cinemaWD.cinema.getName());
            List<Buttons> buttonsList = new ArrayList<>();
            Buttons button1 = new Buttons();
            button1.setTitle("Check location");
            button1.setUrl(cinemaWD.cinema.getLocationUrl());
            button1.setType("web_url");
            Buttons button2 = new Buttons();
            button2.setType("postback");
            button2.setTitle("Choose");
            button2.setPayload(cinemaWD.cinema.getName());
            buttonsList.add(button1);
            buttonsList.add(button2);
            data.setButtonsList(buttonsList);
            dataList.add(data);
        }
        genericTemplateSender.setGenericTemplateData(dataList);
        genericTemplateSender.sendGenericTemplate();
    }

    private Integer getDistance(Coordinates origins, Coordinates destination) {
        GoogleRequestSender googleRequestSender = new GoogleRequestSender();
        String responseJson = googleRequestSender.getDistanceResponce(origins, destination);
        GoogleMatrixResponse response = null;
        try {
            response = new ObjectMapper().readValue(responseJson, GoogleMatrixResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response != null)
            return Integer.parseInt(response.getRows().get(0).getElements().get(0).getDistance().getValue());
        else
            return null;
    }

    public void sendCinemasByFilmIdAndDate(Progress progress) {
        List<String> cinemasChoosing = getCinemaNames(progress);
        cinemasChoosing.add("Find closest");
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

    private class CinemaWithDistance {
        Cinema cinema;
        Integer distance;

        public CinemaWithDistance(Cinema cinema, Integer distance) {
            this.cinema = cinema;
            this.distance = distance;
        }
    }
}
