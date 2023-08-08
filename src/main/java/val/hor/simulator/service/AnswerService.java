package val.hor.simulator.service;

import org.springframework.stereotype.Service;
import val.hor.simulator.entity.Answer;
import val.hor.simulator.entity.Task;
import val.hor.simulator.repository.AnswerRepository;
import val.hor.simulator.util.exception.AnswerNotFoundException;
import val.hor.simulator.util.exception.TaskNotFoundException;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AnswerService {
    private final AnswerRepository answerRepository;

    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public List<Answer> getAnswerByParent(Task task){
        return answerRepository.getAnswerByParentId(task);
    }

    public Answer save (Answer answer){
        return answerRepository.save(answer);
    }

    public Answer get(Integer id) throws AnswerNotFoundException {
        try{
            return answerRepository.findById(id).get();
        } catch (NoSuchElementException ex){
            throw new AnswerNotFoundException("Could not find any answer with ID " + id);
        }
    }

    public void delete(Integer id) throws AnswerNotFoundException {
        Long countById = answerRepository.countById(id);
        if (countById == null || countById == 0)
            throw new AnswerNotFoundException("Could not find any user with ID " + id);
        answerRepository.deleteById(id);
    }



}
