
package val.hor.simulator.entity.skill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class    SkillNode {
    private Skill skill;
    private List<SkillNode> children;
    private SkillNode parent;

    public SkillNode(Skill skill) {
        this.skill = skill;
        this.children = new ArrayList<>();
    }

    public SkillNode getParent() {
        return parent;
    }

    public void setParent(SkillNode parent) {
        this.parent = parent;
    }

    public Skill getSkill() {
        return skill;
    }

    public List<SkillNode> getChildren() {
        return children;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public void setChildren(List<SkillNode> children) {
        this.children = children;
    }
    @Override
    public String toString() {
        return "SkillNode{" +
                "skill=" + skill +
                ", children=" + children +
                '}';
    }


    public static List<SkillNode> buildSkillTree(List<Skill> skillList, List<Skill> parentSkills) {
        Map<Integer, SkillNode> skillNodeMap = new HashMap<>();
        SkillNode root = new SkillNode(null);
        for (Skill parentSkill : parentSkills) {
            SkillNode parentSkillNode = new SkillNode(parentSkill);
            skillNodeMap.put(parentSkill.getId(), parentSkillNode);
            root.getChildren().add(parentSkillNode);
        }

        for (Skill skill : skillList) {
            if (skill.getParent() != null && skillNodeMap.containsKey(skill.getParent().getId())) {
                SkillNode parentSkillNode = skillNodeMap.get(skill.getParent().getId());
                SkillNode skillNode = new SkillNode(skill);
                parentSkillNode.getChildren().add(skillNode);
                skillNodeMap.put(skill.getId(), skillNode);
                skillNode.setParent(parentSkillNode);
            }
        }

        for (SkillNode parentNode : root.getChildren()) {
            setProgressForSkillNode(parentNode);
        }

        return root.getChildren();
    }
    private static void setProgressForSkillNode(SkillNode skillNode) {
        int totalProgress = skillNode.getSkill().getProgress();
        int totalSkills = 1; // Beginne mit 1, um den aktuellen Skill-Knoten zu zählen

        for (SkillNode childNode : skillNode.getChildren()) {
            setProgressForSkillNode(childNode);
            totalProgress += childNode.getSkill().getProgress();
            totalSkills++;
        }

        // Berechne den Durchschnitt nur für Parent-Skills und Sub-Skills
        if (totalSkills > 1) {
            int averageProgress = Math.round((float) totalProgress / totalSkills);
            skillNode.getSkill().setProgress(averageProgress);
        }
    }

}
