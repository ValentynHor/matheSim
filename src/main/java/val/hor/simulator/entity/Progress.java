package val.hor.simulator.entity;


import jakarta.persistence.*;
import val.hor.simulator.entity.skill.Skill;

@Entity
@Table(name="progress")
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    Integer progress;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Progress(){}

    public Progress(Integer progress, Skill skill, User user) {
        this.progress = progress;
        this.skill = skill;
        this.user = user;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Progress{" +
                "id=" + id +
                ", progress=" + progress +
                ", skill=" + skill +
                ", user=" + user +
                '}';
    }
}
