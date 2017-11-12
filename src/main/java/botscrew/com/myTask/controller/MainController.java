package botscrew.com.myTask.controller;

import botscrew.com.myTask.Testing;
import botscrew.com.myTask.dataBaseProcessor.film.FilmService;
import botscrew.com.myTask.messageProcessor.MainProcessor;
import botscrew.com.myTask.model.*;
import botscrew.com.myTask.responseBuilder.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 02/11/2017.
 */
@RestController
public class MainController {
    @Autowired
    private MainProcessor mainProcessor;

    @GetMapping(value = "/message")
    public @ResponseBody
    String verifyWebhook(@RequestParam("hub.mode") String mode,
                         @RequestParam("hub.verify_token") String verifyToken,
                         @RequestParam("hub.challenge") String challenge) {
        return challenge;
    }

    @PostMapping(value = "/message")
    public void requestProcessor(@RequestBody(required = false) AcceptedMessage acceptedMessage) {
        System.out.println("\n\nacceptedMessage:");
        Testing.outputAsJson(acceptedMessage);
        System.out.println("\n\n");
        mainProcessor.run(acceptedMessage);

    }

}
