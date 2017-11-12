package botscrew.com.myTask.dataBaseProcessor.film;

import botscrew.com.myTask.dataBaseProcessor.progress.Progress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Service
public class FilmService {
    @Autowired
    private FilmRepository filmRepository;

    public List<String> getGenres() {
        return filmRepository.getGenres();
    }

    public List<Film> getTodayFilms() {
        return filmRepository.getTodayFilms();
    }

    public List<Film> getAllFilms() {
        List<Film> films = new ArrayList<>();
        filmRepository.findAll().forEach(films::add);
        return films;
    }

    public List<Film> getFilmsByGenre(String genre) {
        return filmRepository.getFilmsByGenre(genre);
    }

    public Film getFilmById(Integer id) {
        List<Film> films = getAllFilms();
        return films.stream().filter(x -> x.getId() == id).findFirst().get();
    }

    public List<Time> getTimeOfSessions() {
        return filmRepository.getTimeOfSessions();
    }

    public Integer getNumberOfTicketsByProgress(Progress progress) {
        Integer filmId = progress.getFilmId();
        Integer cinemaId = progress.getCinemaId();
        String date = progress.getDate();
        String time = progress.getTime();
        String technology = progress.getTechnology();
        return filmRepository.getNumberOfTicketsByProgress(filmId, cinemaId, date + " " + time, technology);
    }

    public String getCinemaNameById(Integer id) {
        return filmRepository.getCinemaNameById(id);
    }

    public void setTickets(Integer number, Progress progress) {
        Integer filmId = progress.getFilmId();
        Integer cinemaId = progress.getCinemaId();
        String date = progress.getDate();
        String time = progress.getTime();
        String technology = progress.getTechnology();
        filmRepository.setTickets(number, filmId, cinemaId, date + " " + time, technology);
    }
}
