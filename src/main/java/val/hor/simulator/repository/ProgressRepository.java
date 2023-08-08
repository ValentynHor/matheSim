package val.hor.simulator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import val.hor.simulator.entity.Progress;
import val.hor.simulator.entity.User;
import val.hor.simulator.entity.skill.Skill;

import java.util.List;

public interface ProgressRepository extends JpaRepository<Progress,Integer> {

    @Query("SELECT p FROM Progress p WHERE p.user = ?1")
    List<Progress> findAllByUser(User user);
    Progress findByUserAndSkill(User user, Skill skill);

    @Modifying
    void deleteAllByUser(User user);


}
