package botscrew.com.myTask.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * Created by Admin on 02/11/2017.
 */
@Data
public class AcceptedMessage {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Entry> entry;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String object;
}
