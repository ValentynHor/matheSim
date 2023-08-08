package val.hor.simulator.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import val.hor.simulator.entity.Progress;
import val.hor.simulator.entity.User;
import val.hor.simulator.entity.skill.Skill;
import val.hor.simulator.repository.ProgressRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Transactional
@Service
public class ProgressService {

    private final ProgressRepository progressRepository;
    private final SkillService skillService;
    private final UserService userService;

    public ProgressService(ProgressRepository progressRepository, SkillService skillService, UserService userService) {
        this.progressRepository = progressRepository;
        this.skillService = skillService;
        this.userService = userService;
    }

    public List<Progress> findAllByUser(User user){
        return progressRepository.findAllByUser(user);
    }

    public List<Progress> saveAll(List<Progress> progressList){
        return progressRepository.saveAll(progressList);
    }

    public Progress save(Progress progress){
        return progressRepository.save(progress);
    }

    public Progress findByUserAndSkill(User user, Skill skill){
        return progressRepository.findByUserAndSkill(user,skill);
    }

    public void createProgressForNewStudent(User user) {
        List<Skill> allList = skillService.listAll();
        List<Skill> parentList = findParentSkills(allList);
        List<Skill> subList = findSubSkills(parentList,allList);
        List<Skill> subSubList = findSubSkills(subList,allList);
        List<Progress> progressList = createProgressForStudent(subSubList,user);
        progressRepository.saveAll(progressList);
    }

    public void deleteProgressForStudent(User user){
        progressRepository.deleteAllByUser(user);
    }

    private List<Skill> findParentSkills(List<Skill> allSkills) {
        return allSkills.stream()
                .filter(skill -> skill.getParent() == null)
                .collect(Collectors.toList());
    }

    private List<Skill> findSubSkills(List<Skill> parentList, List<Skill> allList) {
        Set<Skill> parentSet = new HashSet<>(parentList);

        return allList.stream()
                .filter(skill -> parentSet.contains(skill.getParent()))
                .collect(Collectors.toList());
    }

    private List<Progress> createProgressForStudent(List<Skill> subSubList, User user) {
        Set<Skill> uniqueSkills = new HashSet<>();
        return subSubList.stream()
                .filter(uniqueSkills::add)
                .map(skill -> new Progress(1, skill, user))
                .collect(Collectors.toList());
    }
}
