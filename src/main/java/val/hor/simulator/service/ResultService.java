package val.hor.simulator.service;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import val.hor.simulator.entity.Exam;
import val.hor.simulator.entity.Result;
import val.hor.simulator.entity.Task;
import val.hor.simulator.entity.User;
import val.hor.simulator.repository.ResultRepository;

import java.util.List;


@Service

public class ResultService {

    private final ResultRepository resultRepository;

    public ResultService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }



    public void save (Result result){resultRepository.save(result);}

    public void save (List<Result> resultList){
        resultRepository.saveAll(resultList);
    }

    public int countByExamAndUser(Exam exam, User user){
        return resultRepository.countByExamAndUser(exam,user);
    }
    @Transactional
    public void deleteResultByExam(Exam exam){
        resultRepository.deleteResultByExam(exam);
    }
    public List<Exam> findDistinctExamsByUser(User user){
        return resultRepository.findDistinctExamsByUser(user);
    }

    public List<Integer> findDistinctTryCountsByExamAndUser(Exam exam, User user){
        return resultRepository.findDistinctTryCountsByExamAndUser(exam,user);
    }

    public List<Result> findResultsByUserAndExamAndTryCount(User user, Exam exam, int tryCount){
        return resultRepository.findResultsByUserAndExamAndTryCount(user,exam,tryCount);
    }

    public List<Task> findDistinctTaskIdsByUserAndExamAndTryCount (User user, Exam exam, int tryCount){
        return resultRepository.findDistinctTaskIdsByUserAndExamAndTryCount(user,exam,tryCount);
    }










}
