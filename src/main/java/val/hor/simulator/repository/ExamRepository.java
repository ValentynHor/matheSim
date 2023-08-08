package val.hor.simulator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import val.hor.simulator.entity.Exam;
import val.hor.simulator.entity.User;

import java.util.List;


public interface ExamRepository extends JpaRepository<Exam, Integer> {


    @Query("SELECT e FROM Exam e WHERE e.whoCreated = :user")
    List<Exam> findAllExamsByWhoCreated(User user);

    @Query("SELECT e FROM Exam e JOIN e.examUsers u WHERE u = :user")
    List<Exam> findAllExamsByUser(User user);



}
