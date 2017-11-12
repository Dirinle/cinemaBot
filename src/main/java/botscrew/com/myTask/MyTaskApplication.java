package botscrew.com.myTask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@ComponentScan({"botscrew.com.myTask.dataBaseProcessor.film"})
//@EntityScan({"botscrew.com.myTask.dataBaseProcessor.film"})
//@EnableJpaRepositories("botscrew.com.myTask.dataBaseProcessor.film")
@SpringBootApplication
public class MyTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyTaskApplication.class, args);
    }
}
