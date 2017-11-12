package botscrew.com.myTask.dataBaseProcessor.cinema;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CinemaRepository extends CrudRepository<Cinema, Integer> {
    @Query(value = "select distinct cinema.* from cinema " +
            "join session on session.id_cinema_session=cinema.id " +
            "where date(session.date_and_time)= ?2 " +
            "and session.id_film_session= ?1 ;",
            nativeQuery = true)
    public List<Cinema> getCinemaByIdFilmAndDate(Integer id, String date);

    @Query(value = "select id from cinema where name = ?1 ;",
    nativeQuery = true)
    public Integer getCinemaIdByName(String cinemaName);
}
