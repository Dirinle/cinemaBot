package botscrew.com.myTask.dataBaseProcessor.film;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Film {
    @Id
    private Integer id;
    private String name;
    private String poster;
    private String trailerUrl;
}
