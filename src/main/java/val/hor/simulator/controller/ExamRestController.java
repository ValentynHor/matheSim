package val.hor.simulator.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import val.hor.simulator.entity.Exam;
import val.hor.simulator.entity.Result;
import val.hor.simulator.entity.Task;
import val.hor.simulator.entity.User;
import val.hor.simulator.service.ExamService;
import val.hor.simulator.service.ResultService;
import val.hor.simulator.service.TaskService;
import val.hor.simulator.service.UserService;
import val.hor.simulator.util.Utility;
import val.hor.simulator.util.exception.ExamNotFoundException;
import val.hor.simulator.util.exception.TaskNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ExamRestController {

    private final TaskService taskService;
    private final ExamService examService;
    private final UserService userService;
    private final ResultService resultService;

    public ExamRestController(TaskService taskService, ExamService examService, UserService userService, ResultService resultService) {
        this.examService = examService;
        this.taskService = taskService;
        this.userService = userService;
        this.resultService = resultService;
    }

    @PostMapping("/saveExam")
    public String saveExam(HttpServletRequest request, @RequestBody List<Task> taskList)  {
        User user = userService.getByEmail(Utility.getEmailOfAuthenticatedUser(request));
        Exam exam = new Exam(taskList,user);
        examService.save(exam);
        return "exams/exams";
    }

    @PostMapping("/saveResult")
    public String saveResult(HttpServletRequest request, @RequestBody Map<String, Object> requestPayload)
            throws ExamNotFoundException, TaskNotFoundException{

        User user = userService.getByEmail(Utility.getEmailOfAuthenticatedUser(request));

        String examId = (String) requestPayload.get("examId");
        List<Map<String, Object>> resultListMap = (List<Map<String, Object>>) requestPayload.get("results");

        Exam exam = examService.get(Integer.parseInt(examId.trim()));

        List<Result> resultList = new ArrayList<>();

        for (Map<String, Object> resultData : resultListMap) {

            Result result = new Result(
                    (String) resultData.get("answerName"),
                    Integer.parseInt(((String) resultData.get("answerId")).trim()),
                    exam,
                    Boolean.parseBoolean(((String) resultData.get("isRight")).trim()),
                    (Boolean) resultData.get("isChecked"),
                    taskService.get(Integer.parseInt(((String) resultData.get("taskId")).trim())),
                    user
            );
            result.setTryCount(resultService.countByExamAndUser(exam,user));
            resultList.add(result);
        }
        resultService.save(resultList);

        return "exams/exams";
    }


}
