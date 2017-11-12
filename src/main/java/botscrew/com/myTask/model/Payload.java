package botscrew.com.myTask.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Payload {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Elements> elements;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String text;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Buttons> buttons;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String templateType;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Coordinates coordinates;

    @JsonProperty("template_type")
    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }
}
