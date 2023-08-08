package val.hor.simulator.service;


import org.springframework.stereotype.Service;
import val.hor.simulator.entity.skill.Skill;
import val.hor.simulator.repository.SkillRepository;
import val.hor.simulator.util.exception.SkillNotFoundException;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public List<Skill> listAll(){
        return skillRepository.findAllByOrderByIdAsc();
    }

    public List<Skill> getSkillByParent(Skill parent){
        return skillRepository.getSkillByParent(parent);
    }

    public Skill save(Skill skill){
        return skillRepository.save(skill);
    }

    public Skill get(int id) throws SkillNotFoundException {
        try{
            return skillRepository.findById(id).get();
        } catch (NoSuchElementException e){
            throw new SkillNotFoundException("Could not find any skill with ID " + id);
        }

    }

    public List<Skill> findParentSkills(List<Skill> allSkills) {
        return allSkills.stream()
                .filter(skill -> skill.getParent() == null)
                .collect(Collectors.toList());
    }


    public List<Skill> findSubSkills(List<Skill> parentList, List<Skill> allList) {
        Set<Skill> parentSet = new HashSet<>(parentList);

        return allList.stream()
                .filter(skill -> parentSet.contains(skill.getParent()))
                .collect(Collectors.toList());
    }










}
