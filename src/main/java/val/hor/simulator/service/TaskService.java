package val.hor.simulator.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import val.hor.simulator.entity.Task;
import val.hor.simulator.repository.TaskRepository;
import val.hor.simulator.util.exception.TaskNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAll(){
        return taskRepository.findAll();
    }

    public Task get(Integer id) throws TaskNotFoundException {
        try{
            return taskRepository.findById(id).get();
        } catch (NoSuchElementException ex){
            throw new TaskNotFoundException("Could not find any user with ID " + id);
        }
    }

    public Task save (Task task){
        return taskRepository.save(task);
    }

    public void delete(Integer id) throws TaskNotFoundException {
        taskRepository.deleteById(id);
    }




}
