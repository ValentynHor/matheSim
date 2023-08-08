package val.hor.simulator.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import val.hor.simulator.entity.RoleType;
import val.hor.simulator.entity.Teacher;
import val.hor.simulator.entity.User;
import val.hor.simulator.repository.RoleRepository;
import val.hor.simulator.service.ProgressService;
import val.hor.simulator.service.TeacherService;
import val.hor.simulator.service.UserService;
import val.hor.simulator.util.Utility;
import val.hor.simulator.util.exception.UserNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class StudentController {

    private final TeacherService teacherService;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final ProgressService progressService;

    public StudentController(TeacherService teacherService, UserService userService, RoleRepository roleRepository, ProgressService progressService) {
        this.teacherService = teacherService;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.progressService = progressService;
    }

    @GetMapping("/students")
    public String listAllStudents(Model model, HttpServletRequest request){

        List<User> allStudentsList = userService.findByRoles(roleRepository.findByName(String.valueOf(RoleType.Student)));

        User user = userService.getByEmail(Utility.getEmailOfAuthenticatedUser(request));
        List<Teacher> teachersList = teacherService.findUserByTeacher(user);
        List<User> myStudentList = teachersList.stream()
                .map(Teacher::getStudent)
                .collect(Collectors.toList());
        model.addAttribute("myStudentsList", myStudentList);

        List<User> remainingStudentsList = allStudentsList.stream()
                .filter(student -> !myStudentList.contains(student))
                .collect(Collectors.toList());
        model.addAttribute("allStudentsList", remainingStudentsList);

        return "students/students";
    }

    @GetMapping("/students/add/{studentId}")
    public String addStudent(@PathVariable int studentId, RedirectAttributes redirectAttributes,
                             HttpServletRequest request) throws UserNotFoundException {
        User student = userService.get(studentId);
        User teacher = userService.getByEmail(Utility.getEmailOfAuthenticatedUser(request));
        teacherService.save(new Teacher(teacher,student));

        redirectAttributes.addFlashAttribute("message","Student have been successfully added for you");
        return "redirect:/students";
    }

    @GetMapping("/students/remove/{studentId}")
    public String removeStudent(@PathVariable int studentId, RedirectAttributes redirectAttributes,
                                      HttpServletRequest request) throws UserNotFoundException {
        User student = userService.get(studentId);
        User teacher = userService.getByEmail(Utility.getEmailOfAuthenticatedUser(request));
        teacherService.deleteByTeacherAndStudent(teacher,student);

        redirectAttributes.addFlashAttribute("message", "Student have been successfully removed for you");
        return "redirect:/students";
    }
}
