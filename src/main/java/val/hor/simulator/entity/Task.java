package val.hor.simulator.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column (length = 30)
    private String name;
    @NotNull
    @Column(columnDefinition = "TEXT")
    private String  question;

    @Column(columnDefinition = "TEXT")
    private String mission;

    @Column (length = 10)
    private String variant;

    @OneToMany(mappedBy = "parent")
    private List<Answer> answers = new ArrayList<>();

    @OneToMany(mappedBy = "task")
    private List<Result> results = new ArrayList<>();

    public Task (){}

    public Task(String name, String question, String mission, String variant) {
        this.name = name;
        this.question = question;
        this.mission = mission;
        this.variant = variant;
    }

    public Task(Integer taskId) {
        this.id = taskId;
    }

    public Task(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String task) {
        this.mission = task;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public List<Answer> getAnswers() {
        return answers;
    }
    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
    public void addAnswer (Answer answer ){
        this.answers.add(answer);
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", answers=" + answers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}