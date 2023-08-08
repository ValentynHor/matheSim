package val.hor.simulator.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import val.hor.simulator.entity.Task;

public interface TaskRepository  extends JpaRepository<Task, Integer> {

}
