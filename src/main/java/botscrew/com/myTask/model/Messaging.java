package botscrew.com.myTask.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class Messaging {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Message message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Sender sender;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Recipient recipient;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Postback postback;
}
