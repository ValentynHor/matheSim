package val.hor.simulator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import val.hor.simulator.entity.*;
import val.hor.simulator.service.AnswerService;
import val.hor.simulator.service.ResultService;
import val.hor.simulator.service.TaskService;
import val.hor.simulator.util.exception.AnswerNotFoundException;
import val.hor.simulator.util.exception.TaskNotFoundException;
import val.hor.simulator.util.exception.UserNotFoundException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static val.hor.simulator.util.AnswerUtils.replacePlaceholderWithFormula;

@Controller
public class TaskController {

    private final TaskService taskService;
    private final AnswerService answerService;
    private final ResultService resultService;



    public TaskController(TaskService taskService, AnswerService answerService, ResultService resultService) {
        this.taskService = taskService;
        this.answerService = answerService;
        this.resultService = resultService;
    }

    @GetMapping("/tasks")
    public String getTask(Model model){
        // ADD PAGE ???
        List<Task> taskList = taskService.findAll();
        for (Task task : taskList){
            task.setAnswers(replacePlaceholderWithFormula(answerService.getAnswerByParent(task)));
        }
        model.addAttribute("taskList",taskList);
        System.out.println();
        return "tasks/tasks";
    }

    @GetMapping("/tasks/edit/{id}")
    public String editTask(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes){
        try {
            Task task = taskService.get(id);
            model.addAttribute("task",task);
            model.addAttribute("pageTitle","Edit Task (ID: " + id + ")");
            return "tasks/task_form";
        } catch (TaskNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/tasks";
        }
    }

    @PostMapping("/tasks/save")
    public String saveTask(Task task, RedirectAttributes redirectAttributes){
        taskService.save(task);
        redirectAttributes.addFlashAttribute("message","The task has been saved successfully.");
        return "redirect:/tasks";
    }

    @GetMapping("/tasks/new")
    public String newTask(Model model){
        Task task = new Task();
        model.addAttribute("task", task);
        model.addAttribute("pageTitle","Create New Task");
        return "tasks/task_form";
    }

    @GetMapping("/tasks/delete/{id}")
    public String deleteTask(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes){
        try {
            taskService.delete(id);
            redirectAttributes.addFlashAttribute(
                    "message",
                    "The task ID " + id + " has been deleted successfully");
        } catch (TaskNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/tasks";
    }

    //*******************************************************************************
    //********************   ANSWER   ***********************************************
    //*******************************************************************************

    @GetMapping("/answers/new")
    public String newAnswer(Model model) throws TaskNotFoundException {
        Answer answer = new Answer();
        model.addAttribute("answer", answer);
        List<Task> taskList = taskService.findAll();
        model.addAttribute("taskList", taskList);
        model.addAttribute("pageTitle","Create New Answer");
        return "tasks/answer_form";
    }
    @PostMapping("/answers/save")
    public String saveAnswer(Answer answer, RedirectAttributes redirectAttributes){
        answerService.save(answer);
        redirectAttributes.addFlashAttribute("message","The answer has been saved successfully.");
        return "redirect:/tasks";
    }
    @GetMapping("/answers/edit/{id}")
    public String editAnswer(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes){
        try {
            Answer answer= answerService.get(id);
            model.addAttribute("answer", answer);
            List<Task> taskList = taskService.findAll();
            model.addAttribute("taskList", taskList);
            model.addAttribute("pageTitle","Edit Answer (ID: " + id + ")");
            return "tasks/answer_form";
        } catch (AnswerNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/tasks";
        }
    }
    @GetMapping("/answers/delete/{id}")
    public String deleteAnswer(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes){
        try {
            answerService.delete(id);
            redirectAttributes.addFlashAttribute(
                    "message",
                    "The task ID " + id + " has been deleted successfully");
        } catch (AnswerNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());

        }
        return "redirect:/tasks";
    }
    //*******************************************************************************
    //*******************************************************************************







}
