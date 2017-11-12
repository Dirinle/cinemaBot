package botscrew.com.myTask.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class CallToActions {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String payload;
}
