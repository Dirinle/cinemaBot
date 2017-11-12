package botscrew.com.myTask.dataBaseProcessor.progress;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

@Data
@Entity
public class Progress {
    @Id
    private String id;
    @Enumerated(EnumType.STRING)
    private Step step;
    private Integer filmId;
    private String date;
    private String genre;
    private String time;
    private Integer cinemaId;
    private Integer number;
    private String technology;

    public Progress (){}

    public Progress (String id){
        this.id=id;
        step=Step.filmChoosing;
    }
}
