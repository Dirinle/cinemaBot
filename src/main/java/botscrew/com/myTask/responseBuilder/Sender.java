package botscrew.com.myTask.responseBuilder;

import botscrew.com.myTask.Testing;
import lombok.Data;
import org.springframework.web.client.RestTemplate;

@Data
public class Sender {
    private String pageAccessToken="EAAHIk1cVsYQBAI7g87tO9GK9znd2rzqBZADeGlMZBzFoKQ4NBI8wHAiThbK6SAGYitlUn5RYR2bRWuakWQH1WxqSFaWfWOrGmkywtRP25BlGNsTzYyjuWUpi2SBa7BGwZCTpDx0FC0eNHZAFVCQZC5ZB3PELi2bbuNp2FUzvzT2QZDZD";
    protected String recipientId;
    protected   String sendObject(Object object){
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://graph.facebook.com/v2.6/me/messages?access_token="+pageAccessToken;
        System.out.println("\n\nSent message:");
        Testing.outputAsJson(object);
        System.out.println("\n\n");
        return restTemplate.postForObject(url, object, String.class);
    }
}
