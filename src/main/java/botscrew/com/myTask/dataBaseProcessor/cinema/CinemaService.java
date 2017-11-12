package botscrew.com.myTask.dataBaseProcessor.cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CinemaService {
    @Autowired
    private CinemaRepository cinemaRepository;

    public List<Cinema> getCinemaByIdFilmAndDate (Integer id, String date){
        return cinemaRepository.getCinemaByIdFilmAndDate(id, date);
    }

    public Integer getCinemaIdByName (String cinameName){
        return cinemaRepository.getCinemaIdByName(cinameName);
    }
}
