package val.hor.simulator.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import val.hor.simulator.controller.exporter.ExamPdfExporter;
import val.hor.simulator.entity.*;
import val.hor.simulator.repository.RoleRepository;
import val.hor.simulator.service.*;
import val.hor.simulator.util.Utility;
import val.hor.simulator.util.exception.ExamNotFoundException;
import val.hor.simulator.util.exception.UserNotFoundException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ResultController {

    private final UserService userService;
    private final ResultService resultService;
    private final ExamService examService;
    private final TaskService taskService;
    private final TeacherService teacherService;
    private final RoleRepository roleRepository;

    public ResultController(UserService userService, ResultService resultService, ExamService examService, TaskService taskService, TeacherService teacherService, RoleRepository roleRepository) {
        this.userService = userService;
        this.resultService = resultService;
        this.examService = examService;
        this.taskService = taskService;
        this.teacherService = teacherService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/results")
    public String getResultsByUser(Model model, HttpServletRequest request) {
        Role studentRole = roleRepository.findByName("Student");
        User user = userService.getByEmail(Utility.getEmailOfAuthenticatedUser(request));
        List<Exam> allExams = examService.findAll();

        if (user.getRoles().contains(studentRole)) {
            Map<Exam, List<Integer>> examTryCountMap = createExamTryCountMap(user, allExams);
            model.addAttribute("examTryCountMap", examTryCountMap);
            model.addAttribute("userId", user.getId());
        } else {
            Set<User> allStudentSet = teacherService.findUserByTeacher(user).stream()
                    .map(Teacher::getStudent)
                    .collect(Collectors.toSet());

            Map<User, Map<Exam, List<Integer>>> userExamTryCountMap = new HashMap<>();

            for (User u : allStudentSet) {
                Map<Exam, List<Integer>> examTryCountMap = createExamTryCountMap(u, allExams);
                userExamTryCountMap.put(u, examTryCountMap);
            }
            model.addAttribute("userExamTryCountMap", userExamTryCountMap);
        }
        return "results/results";
    }

    private Map<Exam, List<Integer>> createExamTryCountMap(User user, List<Exam> exams) {
        Map<Exam, List<Integer>> examTryCountMap = new HashMap<>();
        for (Exam exam : exams) {
            List<Integer> listTryCount = resultService.findDistinctTryCountsByExamAndUser(exam, user);
            if (!listTryCount.isEmpty()) {
                Collections.sort(listTryCount);
                examTryCountMap.put(exam, listTryCount);
            }
        }
        return examTryCountMap;
    }


    @GetMapping("/results/userId/{userId}/examId/{examId}/{tryCount}")
    public String getResultForUserAndExam(@PathVariable Integer userId, @PathVariable Integer examId,
                                          @PathVariable Integer tryCount, Model model,
                                          RedirectAttributes redirectAttributes, HttpServletRequest request) {

        try {
            User user = userService.get(userId);
            Exam exam = examService.get(examId);
            List<Result> allResults = resultService.findResultsByUserAndExamAndTryCount(user, exam, tryCount);
            List<Task> taskResults = resultService.findDistinctTaskIdsByUserAndExamAndTryCount(user, exam, tryCount);

            setTaskResults(allResults, taskResults);

            model.addAttribute("taskList",taskResults);
            //for export
            model.addAttribute("userId",user.getId());
            model.addAttribute("examId",examId);
            model.addAttribute("tryCount",tryCount);

            return "results/endResult";
        } catch (ExamNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/results";
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/results";
        }
    }

    @GetMapping("/results/userId/{userId}/examId/{examId}/{tryCount}/export/pdf")
    public void exportToPDF(HttpServletResponse response, @PathVariable int examId,
                            @PathVariable int tryCount, @PathVariable int userId,
                            RedirectAttributes redirectAttributes) throws IOException {
        try {
            Exam exam = examService.get(examId);
            User userTakingExam = userService.get(userId);

            List<Result> allResults = resultService.findResultsByUserAndExamAndTryCount(userTakingExam, exam, tryCount);
            List<Task> taskResults = resultService.findDistinctTaskIdsByUserAndExamAndTryCount(userTakingExam, exam, tryCount);

            setTaskResults(allResults, taskResults);
            exam.setTasks(taskResults);

            ExamPdfExporter examPdfExporter = new ExamPdfExporter();
            examPdfExporter.exportResult(exam, userTakingExam, response);
        } catch (ExamNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
    }


    private void setTaskResults(List<Result> allResults, List<Task> taskResults) {
        for (Task task : taskResults) {
            List<Result> newList = allResults.stream()
                    .filter(r -> task.getId().equals(r.getTask().getId()))
                    .collect(Collectors.toList());
            task.setResults(newList);
        }
    }


}
