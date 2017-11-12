package botscrew.com.myTask.model.google;

import lombok.Data;

import java.util.List;

@Data
public class GoogleMatrixResponse {
    private String status;

    private List<String> destination_addresses;

    private List<String> origin_addresses;

    private List<Rows> rows;
}
