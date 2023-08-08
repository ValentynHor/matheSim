package val.hor.simulator.entity;


import jakarta.persistence.*;

@Entity
@Table(name="results")
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String name;
    private Integer answerId;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    private boolean isRight;

    private boolean isChecked;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    //Who completed
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int tryCount;

    public Result (){
    }

    public Result(String name, Integer answerId, Exam exam, boolean isRight, boolean isChecked, Task task, User user) {
        this.name = name;
        this.answerId = answerId;
        this.exam = exam;
        this.isRight = isRight;
        this.isChecked = isChecked;
        this.task = task;
        this.user = user;
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

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public boolean getIsRight() {
        return isRight;
    }

    public void setUIsRight(boolean right) {
        isRight = right;
    }

    public boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean checked) {
        isChecked = checked;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    public int getTryCount() {
        return tryCount;
    }

    public void setTryCount(int tryCount) {
        this.tryCount = tryCount;
    }


}
