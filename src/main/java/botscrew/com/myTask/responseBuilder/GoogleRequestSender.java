package botscrew.com.myTask.responseBuilder;

import botscrew.com.myTask.model.Coordinates;
import org.springframework.web.client.RestTemplate;

public class GoogleRequestSender {
    private String apiKey="AIzaSyBS3enykKRY21W3jv1i1hAkK37_UjE4pq4";
    public String getDistanceResponce(Coordinates origins, Coordinates destination){
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="+
                origins.getLat()+","+origins.getLng()+
                "&destinations="+
                destination.getLat()+","+destination.getLng()+
                "&key="+
                apiKey;
        return restTemplate.getForObject(url, String.class);
    }
}
