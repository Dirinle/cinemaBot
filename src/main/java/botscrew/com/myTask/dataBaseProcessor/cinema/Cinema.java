package botscrew.com.myTask.dataBaseProcessor.cinema;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Cinema {
    @Id
    Integer id;
    String name;
    String locationUrl;
    String address;
    String longitude;
    String latitude;
    String imageUrl;
}
