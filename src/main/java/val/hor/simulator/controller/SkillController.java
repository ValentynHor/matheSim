package val.hor.simulator.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import val.hor.simulator.entity.skill.Skill;
import val.hor.simulator.entity.skill.SkillNode;
import val.hor.simulator.service.SkillService;
import val.hor.simulator.util.exception.SkillNotFoundException;

import java.util.List;

@Controller
public class SkillController {

    private final SkillService skillService;


    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping("/skills")
    public String listAllSkills(Model model, HttpServletRequest request){

        List<Skill> skillList = skillService.listAll();
        List<Skill> parentSkills = skillService.getSkillByParent(null);

        List<SkillNode> skillTree = SkillNode.buildSkillTree(skillList, parentSkills);
        model.addAttribute("skillTree",skillTree);

        return "skills/skills";

    }

    @GetMapping("/skills/edit/{id}")
    public String editSkill(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes){
        try{
            Skill skill = skillService.get(id);
            model.addAttribute("skills",skill);
            model.addAttribute("pageTitle","Edit Skill (ID: " + id + ")");
            return "skills/skill_form";
        } catch (SkillNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/skills";
        }
    }

    @PostMapping("/skills/save")
    public String saveSkill(Skill editSkill, RedirectAttributes redirectAttributes) throws SkillNotFoundException {

        Skill skill = skillService.get(editSkill.getId());
        skill.setDescription(editSkill.getDescription());
        skillService.save(skill);
        redirectAttributes.addFlashAttribute("message","The user has been saved successfully.");
        return "redirect:/skills";
    }











}
