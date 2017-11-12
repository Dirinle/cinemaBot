package botscrew.com.myTask.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by Admin on 02/11/2017.
 */
@Data
public class SentMessage {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Greeting greeting;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<CallToActions> callToActions;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String settingType;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String threadState;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String senderAction;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Message message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Recipient recipient;

    @JsonProperty("sender_action")
    public void setSenderAction(String senderAction) {
        this.senderAction = senderAction;
    }

    @JsonProperty("thread_state")
    public void setThreadState(String threadState) {
        this.threadState = threadState;
    }

    @JsonProperty("setting_type")
    public void setSettingType(String settingType) {
        this.settingType = settingType;
    }

    @JsonProperty("call_to_actions")
    public void setCallToActions(List<CallToActions> callToActions) {
        this.callToActions = callToActions;
    }
}
