package val.hor.simulator.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

@Transactional
@Controller
public class TeacherController {

    private final TeacherService teacherService;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final ProgressService progressService;

    public TeacherController(TeacherService teacherService, UserService userService, RoleRepository roleRepository, ProgressService progressService) {
        this.teacherService = teacherService;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.progressService = progressService;
    }

    @PostMapping("/students/request-teacher")
    public String requestToBeTeacher(RedirectAttributes redirectAttributes,
                                     HttpServletRequest request){


        User user = userService.getByEmail(Utility.getEmailOfAuthenticatedUser(request));

        Teacher teacher = teacherService.findUserByTeacherAndStudent(user);
        if (teacher == null){
            teacherService.save(new Teacher(user,user));
            redirectAttributes.addFlashAttribute("message","Your request has been sent successfully.");
        } else {
            redirectAttributes.addFlashAttribute("message","You have already sent a request. It is being processed. Please be patient.");
        }
        return "redirect:/account";
    }

    @GetMapping("/teachers")
    public String allTeacher(Model model){

        //List<User> studentList = userService.findByRoles(roleRepository.findByName(String.valueOf(RoleType.Student)));



        List<User> teachersList = userService.findByRoles(roleRepository.findByName(String.valueOf(RoleType.Teacher)));

        model.addAttribute("teachersList",teachersList);

        //List<Teacher> newTeachersList = teacherService.findAllByTeacherIdEqualsStudentId();

        List<User> newTeachersList = teacherService.findAllByTeacherIdEqualsStudentId()
                .stream()
                .map(Teacher::getTeacher)
                .collect(Collectors.toList());

        model.addAttribute("newTeachersList",newTeachersList);
        return "teachers/teachers";
    }

    @GetMapping("/teachers/add/{teacherId}")
    public String addTeacher(@PathVariable int teacherId, RedirectAttributes redirectAttributes) throws UserNotFoundException {

        User user = userService.get(teacherId);
        userService.changeRole(user, RoleType.Teacher);
        progressService.deleteProgressForStudent(user);
        teacherService.deleteByTeacherAndStudent(user);
        redirectAttributes.addFlashAttribute("message","Teacher rights have been successfully added for user id " + teacherId);
        return "redirect:/teachers";
    }

    @GetMapping("/teachers/revoke/{teacherId}")
    public String revokeTeacherRights(@PathVariable int teacherId, RedirectAttributes redirectAttributes) throws UserNotFoundException {
        User user = userService.get(teacherId);
        userService.changeRole(user, RoleType.Student);
        progressService.createProgressForNewStudent(user);
        redirectAttributes.addFlashAttribute("message", "Teacher rights have been successfully revoked for user id " + teacherId);
        return "redirect:/teachers";
    }

    private void setProgress(List<User> userList){
        for (User user : userList){
            progressService.createProgressForNewStudent(user);
        }
    }







}
