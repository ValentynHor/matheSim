package val.hor.simulator.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import val.hor.simulator.controller.exporter.UserCsvExporter;
import val.hor.simulator.controller.exporter.UserExcelExporter;
import val.hor.simulator.controller.exporter.UserPdfExporter;
import val.hor.simulator.entity.Role;
import val.hor.simulator.entity.User;
import val.hor.simulator.service.UserService;
import val.hor.simulator.util.exception.UserNotFoundException;

import java.io.IOException;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping("/users")
    public String listFirstPage(Model model){
        return listByPage(1,model, "firstName", "asc", null);
    }

    @GetMapping("/users/page/{pageNum}")
    public String listByPage(
            @PathVariable(name = "pageNum") int pageNum, Model model, @Param("sortField") String sortField,
            @Param("sortDir") String sortDir, @Param("keyword") String keyword
    ){
        Page<User> page = userService.listByPage(pageNum, sortField, sortDir, keyword);
        List<User> userList = page.getContent();

        long startCount = (long) (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
        long endCount = startCount + UserService.USERS_PER_PAGE - 1;
        if(endCount > page.getTotalElements()){
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("userList", userList);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);

        return "users/users";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes) {
        userService.save(user);
        redirectAttributes.addFlashAttribute("message","The user has been saved successfully.");
        return getRedirectURL(user);
    }

    private String getRedirectURL(User user) {
        String firstPartOfEmail = user.getEmail().split("@")[0];
        return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(
            @PathVariable(name = "id") Integer id,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            User user = userService.get(id);
            List<Role> roleList = userService.listRoles();
            model.addAttribute("user",user);
            model.addAttribute("pageTitle","Edit User (ID: " + id + ")");
            model.addAttribute("roleList", roleList);
            return "users/user_form";
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserEnabledStatus(
            @PathVariable(name = "id") Integer id, @PathVariable(name = "status") boolean enabled,
            RedirectAttributes redirectAttributes
    ){
        userService.updateEnableStatus(id,enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The user ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/users";
    }
    @GetMapping("/users/delete/{id}")
    public String deleteUser(
            @PathVariable(name = "id") Integer id,
            RedirectAttributes redirectAttributes
    ){
        try {
            userService.delete(id);
            redirectAttributes.addFlashAttribute("message","The user ID " + id + " has been deleted successfully");
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/users/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        List<User> userList = userService.listAll();
        UserCsvExporter userCsvExporter = new UserCsvExporter();
        userCsvExporter.export(userList,response);

    }

    @GetMapping("/users/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<User> userList = userService.listAll();
        UserExcelExporter userExcelExporter = new UserExcelExporter();
        userExcelExporter.export(userList,response);
    }

    @GetMapping("/users/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws IOException {
        List<User> userList = userService.listAll();
        UserPdfExporter userPdfExporter = new UserPdfExporter();
        userPdfExporter.export(userList,response);
    }












}
