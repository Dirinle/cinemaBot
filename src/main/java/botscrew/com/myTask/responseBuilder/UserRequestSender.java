package botscrew.com.myTask.responseBuilder;

import org.springframework.web.client.RestTemplate;

public class UserRequestSender {
    private String pageAccessToken = "EAAHIk1cVsYQBAI7g87tO9GK9znd2rzqBZADeGlMZBzFoKQ4NBI8wHAiThbK6SAGYitlUn5RYR2bRWuakWQH1WxqSFaWfWOrGmkywtRP25BlGNsTzYyjuWUpi2SBa7BGwZCTpDx0FC0eNHZAFVCQZC5ZB3PELi2bbuNp2FUzvzT2QZDZD";

    public String getUserName(String userId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://graph.facebook.com/v2.6/" +
                userId + "?fields=first_name,last_name,profile_pic&access_token=" +
                pageAccessToken;
        return restTemplate.getForObject(url, String.class);
    }
}
