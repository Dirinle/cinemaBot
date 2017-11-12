package botscrew.com.myTask.dataBaseProcessor.progress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProgressService {
    @Autowired
    private ProgressRepository progressRepository;
    public Progress getProgress(String userId){
        List<Progress>progresses = new ArrayList<>();
        progressRepository.findAll().forEach(progresses::add);
        Optional<Progress>progress = progresses.stream().filter(x->x.getId().equals(userId)).findFirst();
        if (progress.isPresent())
            return progress.get();
        else
            return new Progress(userId);//there must be constructor with false fields
    }

    public void update(Progress progress){
        progressRepository.save(progress);
    }

    public void save (Progress progress){
        progressRepository.save(progress);
    }

    public void delete (Progress progress){
        progressRepository.delete(progress);
    }
}
