package val.hor.simulator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import val.hor.simulator.entity.Answer;
import val.hor.simulator.entity.Task;

import java.util.List;


public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    @Query("SELECT a FROM Answer a WHERE a.parent = ?1")
    List<Answer> getAnswerByParentId(Task task);

    Long countById(Integer id);

}
