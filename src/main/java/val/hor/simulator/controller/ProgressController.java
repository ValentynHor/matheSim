package val.hor.simulator.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import val.hor.simulator.entity.Progress;
import val.hor.simulator.entity.Role;
import val.hor.simulator.entity.Teacher;
import val.hor.simulator.entity.User;
import val.hor.simulator.entity.skill.Skill;
import val.hor.simulator.entity.skill.SkillNode;
import val.hor.simulator.repository.RoleRepository;
import val.hor.simulator.service.ProgressService;
import val.hor.simulator.service.SkillService;
import val.hor.simulator.service.TeacherService;
import val.hor.simulator.service.UserService;
import val.hor.simulator.util.Utility;
import val.hor.simulator.util.exception.SkillNotFoundException;
import val.hor.simulator.util.exception.UserNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ProgressController {
    private final SkillService skillService;
    private final ProgressService progressService;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final TeacherService teacherService;


    public ProgressController(SkillService skillService, ProgressService progressService, UserService userService, RoleRepository roleRepository, TeacherService teacherService) {
        this.skillService = skillService;
        this.progressService = progressService;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.teacherService = teacherService;
    }

    @GetMapping("/progress")
    public String listAllSkillsAndProgress(Model model, HttpServletRequest request){

        Role studentRole = roleRepository.findByName("Student");
        User user = userService.getByEmail(Utility.getEmailOfAuthenticatedUser(request));

        if(user.getRoles().contains(studentRole)){

            List<Progress> progressList = progressService.findAllByUser(user);
            if(progressList.isEmpty()){
                progressService.createProgressForNewStudent(user);
                progressList = progressService.findAllByUser(user);
            }

            List<Skill> skillList = skillService.listAll();
            List<Skill> parentSkills = skillService.getSkillByParent(null);

            Map<Skill, Integer> progressMap = new HashMap<>();

            for (Progress progress : progressList) {
                progressMap.put(progress.getSkill(), progress.getProgress());
            }
            for (Skill skill : skillList) {
                Integer progress = progressMap.get(skill);
                if (progress != null) {
                    skill.setProgress(progress);
                }
            }

            List<SkillNode> skillTree = SkillNode.buildSkillTree(skillList, parentSkills);

            model.addAttribute("skillTree",skillTree);

        } else {
            List<User> allStudent = teacherService.findUserByTeacher(user).stream()
                    .map(Teacher::getStudent)
                    .collect(Collectors.toList());
            model.addAttribute("allStudent",allStudent);
        }
        return "progress/progress";

    }
    @GetMapping("/progress/progressForUser/{userId}")
    public String getSkillProgressForUser(@PathVariable(name = "userId") Integer userId,
                                    Model model) throws UserNotFoundException {

        User user = userService.get(userId);

        List<Skill> skillList = skillService.listAll();
        List<Skill> parentSkills = skillService.getSkillByParent(null);

        List<Progress> progressList = progressService.findAllByUser(user);
        if(progressList.isEmpty()){
            progressService.createProgressForNewStudent(user);
            progressList = progressService.findAllByUser(user);
        }

        Map<Skill, Integer> progressMap = new HashMap<>();

        for (Progress progress : progressList) {
            progressMap.put(progress.getSkill(), progress.getProgress());
        }
        for (Skill skill : skillList) {
            Integer progress = progressMap.get(skill);
            if (progress != null) {
                skill.setProgress(progress);
            }
        }

        List<SkillNode> skillTree = SkillNode.buildSkillTree(skillList, parentSkills);

        model.addAttribute("skillTree",skillTree);
        model.addAttribute("userId",userId);

        return "/progress/progressForUser";

    }
    @GetMapping("/progress/{userId}/edit/{skillId}/progress/{progress}")
    public String editSkillProgress(@PathVariable(name = "skillId") Integer skillId,
                                    @PathVariable(name = "progress") Integer progress,
                                    @PathVariable(name = "userId") Integer userId,
                                    HttpServletRequest httpServletRequest
                                    ) throws SkillNotFoundException, UserNotFoundException {
        User user = userService.get(userId);
        Skill skill = skillService.get(skillId);
        Progress progressByUser = progressService.findByUserAndSkill(user, skill);
        progressByUser.setProgress(progress);
        progressService.save(progressByUser);
        return "redirect:/progress/progressForUser/{userId}";
    }


}


