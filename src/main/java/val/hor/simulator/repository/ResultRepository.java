package val.hor.simulator.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import val.hor.simulator.entity.Exam;
import val.hor.simulator.entity.Result;
import val.hor.simulator.entity.Task;
import val.hor.simulator.entity.User;

import java.util.List;


public interface ResultRepository extends JpaRepository<Result, Integer> {


    @Query("SELECT COUNT (DISTINCT r.tryCount) FROM Result r WHERE r.exam = ?1 AND r.user = ?2")
    int countByExamAndUser(Exam exam, User user);

    @Modifying
    @Query("DELETE FROM Result r WHERE r.exam = ?1")
    void deleteResultByExam(Exam exam);

    @Query("SELECT DISTINCT r.exam FROM Result r WHERE r.user = ?1")
    List<Exam> findDistinctExamsByUser(User user);

    @Query("SELECT DISTINCT r.tryCount FROM Result r WHERE r.exam = ?1 AND r.user = ?2")
    List<Integer> findDistinctTryCountsByExamAndUser(Exam exam, User user);

    @Query("SELECT r FROM Result r WHERE r.user = ?1 AND r.exam = ?2 AND r.tryCount = ?3")
    List<Result> findResultsByUserAndExamAndTryCount(User user, Exam exam, int tryCount);

    @Query("SELECT DISTINCT r.task FROM Result r WHERE r.user = ?1 AND r.exam = ?2 AND r.tryCount = ?3")
    List<Task> findDistinctTaskIdsByUserAndExamAndTryCount(User user, Exam exam, int tryCount);
}






