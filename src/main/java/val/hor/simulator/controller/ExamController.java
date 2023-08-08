package val.hor.simulator.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;
import val.hor.simulator.entity.*;
import val.hor.simulator.repository.RoleRepository;
import val.hor.simulator.service.*;
import val.hor.simulator.util.Utility;
import val.hor.simulator.util.exception.ExamNotFoundException;
import val.hor.simulator.util.exception.UserNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

import static val.hor.simulator.util.AnswerUtils.randomlySelectAndShuffleAnswersList;
import static val.hor.simulator.util.AnswerUtils.replacePlaceholderWithFormula;

@Controller
public class ExamController {

    private final TaskService taskService;
    private final ExamService examService;
    private final AnswerService answerService;
    private final UserService userService;
    private final TeacherService teacherService;
    private final RoleRepository roleRepository;

    public ExamController(TaskService taskService, ExamService examService, AnswerService answerService, UserService userService, TemplateEngine templateEngine, TeacherService teacherService, RoleRepository roleRepository) {
        this.examService = examService;
        this.taskService = taskService;
        this.answerService = answerService;
        this.userService = userService;
        this.teacherService = teacherService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/exams")
    public String getExam(Model model, HttpServletRequest request) {
        Role studentRole = roleRepository.findByName("Student");
        User user = userService.getByEmail(Utility.getEmailOfAuthenticatedUser(request));

        List<Exam> exams;
        if(user.getRoles().contains(studentRole)){
            exams = examService.findAllExamsByUser(user);


        }else{
            exams = examService.findAllExamsByWhoCreated(user);

            List<User> allStudentList = teacherService.findUserByTeacher(user).stream()
                    .map(Teacher::getStudent)
                    .collect(Collectors.toList());

            Map<Integer, List<User>> allStudentLists = new HashMap<>();

            for (Exam exam : exams) {
                Set<User> examUsers = exam.getExamUsers();
                List<User> examStudentList = new ArrayList<>(allStudentList);
                examStudentList.removeAll(examUsers);
                allStudentLists.put(exam.getId(), examStudentList);
            }

            model.addAttribute("allStudentLists", allStudentLists);
        }

        model.addAttribute("exams", exams);

        return "exams/exams";
    }

    @GetMapping("/exams/{id}")
    public String getExamByNumber(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {

            Exam exam = examService.get(id);
            for (Task task : exam.getTasks()) {
                List<Answer> allAnswers = answerService.getAnswerByParent(task);
                List<Answer> variantList = randomlySelectAndShuffleAnswersList(allAnswers, task.getVariant());
                task.setAnswers(replacePlaceholderWithFormula(variantList));
            }
            model.addAttribute("exams", exam);
            return "/exams/examByNumber";
        } catch (ExamNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/exams";
        }
    }

    @GetMapping("/exams/delete/{id}")
    public String deleteExam(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes){
        try {
            Exam exam = examService.get(id);
            examService.delete(exam);
            redirectAttributes.addFlashAttribute(
                    "message",
                    "The Exam ID " + id + " has been deleted successfully");
        } catch (ExamNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/exams";
    }

    @GetMapping("/students/{studentId}/addToExam/{examId}")
    public String addStudentToExam(@PathVariable int studentId, @PathVariable int examId,
                                   RedirectAttributes redirectAttributes,HttpServletRequest request) throws UserNotFoundException, ExamNotFoundException {

        Exam exam = examService.get(examId);
        exam.addExamUser(userService.get(studentId));
        examService.save(exam);

        redirectAttributes.addFlashAttribute("message","Student with ID:" + studentId + " have been successfully added to exam " + examId);
        return "redirect:/exams";
    }

    @GetMapping("/students/{studentId}/removeFromExam/{examId}")
    public String removeStudentFromExam(@PathVariable int studentId, @PathVariable int examId,
                                        RedirectAttributes redirectAttributes, HttpServletRequest request) throws UserNotFoundException, ExamNotFoundException {

        Exam exam = examService.get(examId);
        exam.removeExamUser(userService.get(studentId));
        examService.save(exam);

        redirectAttributes.addFlashAttribute("message", "Student with ID:" + studentId + " have been successfully removed from Exam " + examId);
        return "redirect:/exams";
    }


}





