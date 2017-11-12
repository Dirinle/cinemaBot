package botscrew.com.myTask.dataBaseProcessor.film;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.sql.Time;
import java.util.List;


public interface FilmRepository extends CrudRepository<Film, Integer> {

    @Query(value = "select distinct film.* from film " +
            "join session on session.id_film_session=film.id " +
            "where day(cast(session.date_and_time as date))=day(now());",
            nativeQuery = true)
    List<Film> getTodayFilms();

    @Query(value = "select name from genre;",
            nativeQuery = true)
    List<String> getGenres();

    @Query(value = "select film.* from film " +
            "join film_genre on film_genre.id_film=film.id " +
            "join genre on film_genre.id_genre=genre.id " +
            "where genre.name =?1",
            nativeQuery = true)
    List<Film> getFilmsByGenre(String genre);

    @Query(value = "select distinct cast(date_and_time as time) from session;",
            nativeQuery = true)
    List<Time> getTimeOfSessions();

    @Query(value = "select tickets from session where " +
            "id_film_session= ?1 and " +
            "id_cinema_session = ?2 and  " +
            "date_and_time = ?3 and " +
            "technology = ?4 " +
            ";",
    nativeQuery = true)
    Integer getNumberOfTicketsByProgress(Integer filmId, Integer cinemaId,
                                         String dateTime, String technology);

    @Query(value = "select name from cinema where id = ?1 ;",
    nativeQuery = true)
    String getCinemaNameById(Integer id);

    @Modifying
    @Transactional
    @Query(value = "update session set tickets = ?1 where\n" +
            "id_film_session = ?2 " +
            "and id_cinema_session = ?3 " +
            "and date_and_time = ?4 " +
            "and technology = ?5 " +
            ";",
    nativeQuery = true)
    void setTickets(Integer number, Integer filmId, Integer cinemaId,
                    String dateTime, String technology);
}
