package botscrew.com.myTask.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class Attachment {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Payload payload;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String type;
}
