package val.hor.simulator.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import val.hor.simulator.entity.Exam;
import val.hor.simulator.entity.User;
import val.hor.simulator.repository.ExamRepository;
import val.hor.simulator.util.exception.ExamNotFoundException;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final ResultService resultService;

    public ExamService(ExamRepository examRepository, ResultService resultService) {
        this.examRepository = examRepository;

        this.resultService = resultService;
    }


    public Exam save(Exam exam){
        return examRepository.save(exam);
    }

    public Exam get(Integer exam_id) throws ExamNotFoundException {
        try{
            return examRepository.findById(exam_id).get();
        } catch (NoSuchElementException ex){
            throw new ExamNotFoundException("Could not find any exam with ID " + exam_id);
        }
    }

    public List<Exam> findAll(){
        return examRepository.findAll();
    }

    public void delete(int id) throws ExamNotFoundException {
        try{
          examRepository.deleteById(id);
        } catch (NoSuchElementException ex){
            throw new ExamNotFoundException("Could not find any exam with ID " + id);
        }
    }
    @Transactional
    public void delete(Exam exam) throws ExamNotFoundException {
        try{
            resultService.deleteResultByExam(exam);
            examRepository.delete(exam);
        } catch (NoSuchElementException ex){
            throw new ExamNotFoundException("Could not find any exam with ID " + exam.getId());
        }
    }

    public List<Exam> findAllExamsByWhoCreated(User user){
        return examRepository.findAllExamsByWhoCreated(user);
    }

    public List<Exam> findAllExamsByUser (User user){
        return examRepository.findAllExamsByUser(user);
    }
}
