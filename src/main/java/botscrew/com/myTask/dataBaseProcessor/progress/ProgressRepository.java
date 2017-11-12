package botscrew.com.myTask.dataBaseProcessor.progress;

import botscrew.com.myTask.dataBaseProcessor.progress.Progress;
import org.springframework.data.repository.CrudRepository;

public interface ProgressRepository extends CrudRepository<Progress, String> {
}
