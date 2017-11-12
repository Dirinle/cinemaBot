package botscrew.com.myTask.responseBuilder;

import botscrew.com.myTask.model.Buttons;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GenericTemplateData {
    private String title;
    private String imageUrl;
    private String subtitle;
    private String url;
    private String defaultActionUrl;
    private String fallbackUrl;
    private List<Buttons> buttonsList;
}
