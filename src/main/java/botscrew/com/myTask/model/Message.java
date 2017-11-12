package botscrew.com.myTask.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Message {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<QuickReplies> quickReplies;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String text;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Attachment attachment;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Attachments> attachments;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String seq;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String mid;

    @JsonProperty("quick_replies")
    public void setQuickReplies(List<QuickReplies> quickReplies) {
        this.quickReplies = quickReplies;
    }
}
