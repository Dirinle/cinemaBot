package botscrew.com.myTask.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Coordinates {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lng;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lat;

    public Coordinates() {
    }

    public Coordinates(String lng, String lat) {

        this.lng = lng;
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    @JsonProperty("long")
    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
