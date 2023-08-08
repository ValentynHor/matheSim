package val.hor.simulator.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name="answers")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String name;

    private boolean isRight;

    @Column (length = 15)
    private String formula;

    @ManyToOne
    private Task parent;

    public Answer(){
    }

    public Answer(Integer id, String name, boolean isRight, String formula, Task parent) {
        this.id = id;
        this.name = name;
        this.isRight = isRight;
        this.formula = formula;
        this.parent = parent;
    }
    public Answer(Integer id,String name, boolean isRight, Task parent) {
        this.id = id;
        this.name = name;
        this.isRight = isRight;
        this.parent = parent;
    }

    public Answer(String name, boolean isRight, String formula, Task parent) {
        this.name = name;
        this.isRight = isRight;
        this.formula = formula;
        this.parent = parent;
    }

    public Answer(String name, boolean isRight, Task parent) {
        this.name = name;
        this.isRight = isRight;
        this.parent = parent;
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
    public boolean getIsRight() {
        return isRight;
    }

    public void setIsRight(boolean right) {
        isRight = right;
    }

    public Task getParent() {
        return parent;
    }

    public void setParent(Task parent) {
        this.parent = parent;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Answer)) return false;
        Answer answer = (Answer) o;
        return Objects.equals(id, answer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Answer[" + id +"; name: " + name + "]";
    }
}
