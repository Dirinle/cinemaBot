package botscrew.com.myTask.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DefaultAction {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String webviewHeightRatio;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fallbackUrl;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String url;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String messengerExtensions;

    @JsonProperty("webview_height_ratio")
    public void setWebviewHeightRatio(String webviewHeightRatio) {
        this.webviewHeightRatio = webviewHeightRatio;
    }
    @JsonProperty("fallback_url")
    public void setFallbackUrl (String fallbackUrl){
        this.fallbackUrl=fallbackUrl;
    }
    @JsonProperty("messenger_extensions")
    public void setMessengerExtensions(String messengerExtensions) {
        this.messengerExtensions = messengerExtensions;
    }
}
