package val.hor.simulator.entity;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import val.hor.simulator.entity.skill.Skill;
import val.hor.simulator.repository.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class SkillProgressTest {

    @Autowired
    private SkillRepository skillRepo;
    @Autowired
    private ProgressRepository progressRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private ExamRepository examRepo;

    @Test
    public void showSkills() {

        Optional<User> user = userRepo.findById(19);


        Role studentRole = roleRepo.findByName("Student");
        System.out.println("********************************");
        System.out.println(studentRole);
        System.out.println(user.get().getRoles().contains(studentRole));
        System.out.println("********************************");



        List<Exam> exams = examRepo.findAllExamsByUser(user.get());

        System.out.println("********************************");
        System.out.println(exams);
        System.out.println("********************************");


//        List<Skill> allList = skillRepo.findAllByOrderByIdAsc();
//        List<Skill> parentList = findParentSkills(allList);
//        List<Skill> subList = findSubSkills(parentList,allList);
//        List<Skill> subSubList = findSubSkills(subList,allList);
        /*User user = userRepo.getUserByEmail("valentyn.hordiychuk@gmail.com");
        System.out.println("********************************");
        System.out.println("1)"+user.getRoles());
        System.out.println("********************************");
        Role admin = roleRepo.findByName("Admin");
        Role teacher = roleRepo.findByName("Teacher");
        Role student = roleRepo.findByName("Student");

        Set<Role> teacherRoleSet = new HashSet<>();
        teacherRoleSet.add(student);
        user.updateRoles(teacherRoleSet);
        System.out.println("********************************");
        System.out.println(user.getRoles());
        System.out.println("********************************");*/

        //List<Progress> progressList = createProgressForStudent(subSubList,user);
        //progressRepo.saveAll(progressList);


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

    /*public List<Progress> getProgressForUser(List<Skill> subSubList, User user) {
        Set<Skill> uniqueSkills = new HashSet<>();
        return subSubList.stream()
                .filter(uniqueSkills::add)
                .map(skill -> new Progress(skill.getProgress(), skill, user))
                .collect(Collectors.toList());
    }*/




















}
