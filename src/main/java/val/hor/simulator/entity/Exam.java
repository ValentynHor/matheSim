package val.hor.simulator.entity;


import jakarta.persistence.*;

import java.util.*;


@Entity
@Table(name="exams")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToMany
    @JoinTable(
            name = "exam_tasks",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private List<Task> tasks = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "who_created")
    private User whoCreated;

    @ManyToMany
    @JoinTable(
            name = "exam_users",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> examUsers = new HashSet<>();

    public Exam(){}

    public Exam(List<Task> tasks, User whoCreated) {
        this.tasks = tasks;
        this.whoCreated = whoCreated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public User getWhoCreated() {
        return whoCreated;
    }

    public void setWhoCreated(User whoCreated) {
        this.whoCreated = whoCreated;
    }

    public Set<User> getExamUsers() {
        return examUsers;
    }

    public void setExamUsers(Set<User> examUsers) {
        this.examUsers = examUsers;
    }

    public void addExamUser(User user){
        this.examUsers.add(user);
    }

    public void removeExamUser(User user){
        this.examUsers.removeIf(u -> u.getId().equals(user.getId()));
    }

    @Override
    public String toString() {
        return "Exam{" +
                "id=" + id +
                '}';
    }
}

