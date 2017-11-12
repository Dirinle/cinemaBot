package botscrew.com.myTask.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Elements {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String title;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String imageUrl;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String subtitle;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Buttons> buttons;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DefaultAction defaultAction;

    @JsonProperty("image_url")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @JsonProperty("default_action")
    public void setDefaultAction(DefaultAction defaultAction) {
        this.defaultAction = defaultAction;
    }
}
