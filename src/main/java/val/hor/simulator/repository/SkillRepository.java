package val.hor.simulator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import val.hor.simulator.entity.skill.Skill;

import java.util.List;


public interface SkillRepository extends JpaRepository<Skill, Integer> {

    List<Skill> getSkillByParent(Skill parent);
    List<Skill> findAllByOrderByIdAsc();


}
