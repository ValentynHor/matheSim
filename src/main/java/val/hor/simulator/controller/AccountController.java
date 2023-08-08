package val.hor.simulator.controller;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import val.hor.simulator.entity.User;
import val.hor.simulator.entity.setting.EmailSettingBag;
import val.hor.simulator.service.ProgressService;
import val.hor.simulator.service.SettingService;
import val.hor.simulator.service.TeacherService;
import val.hor.simulator.service.UserService;
import val.hor.simulator.util.Utility;
import val.hor.simulator.util.exception.UserNotFoundException;

import java.io.UnsupportedEncodingException;

@Controller
public class AccountController {

    private final UserService userService;
    private final SettingService settingService;
    private final ProgressService progressService;
    private final TeacherService teacherService;

    public AccountController(UserService userService, SettingService settingService, ProgressService progressService, TeacherService teacherService) {
        this.userService = userService;
        this.settingService = settingService;
        this.progressService = progressService;
        this.teacherService = teacherService;
    }

    @GetMapping("/register")
    public String newUser(Model model){
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("pageTitle","Create New User");
        return "register";
    }

    @PostMapping("/account/save")
    public String saveUser(User user,HttpServletRequest request, Model model) throws MessagingException, UnsupportedEncodingException {
        userService.registerUser(user);
        sendVerificationEmail(request, user);
        progressService.createProgressForNewStudent(user);
        model.addAttribute("pageTitle", "Registration Succeeded");
        return "register/register_success";
    }

    private void sendVerificationEmail(HttpServletRequest request, User user) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettings = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);

        String toAddress = user.getEmail();
        String subject = emailSettings.getStudentVerifySubject();
        String content = emailSettings.getStudentVerifyContent();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSettings.getFromAddress(),emailSettings.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFullName());
        String verifyUrl = Utility.getSiteUrl(request) + "/verify?code=" + user.getVerificationCode();
        content = content.replace("[[link]]", verifyUrl);
        helper.setText(content,true);

        mailSender.send(message);
    }
    @GetMapping("/verify")
    public String verifyAccount(@Param("code") String code, Model model){
        boolean verified = userService.verify(code);
        return "register/" + (verified ? "verify_success" : "verify_fail");
    }
    @GetMapping("/account")
    public String viewAccount(Model model, HttpServletRequest request){
        model.addAttribute("pageTitle", "Your Account Info");
        User user = userService.getByEmail(Utility.getEmailOfAuthenticatedUser(request));
        model.addAttribute("user",user);
        return "account";
    }
    @PostMapping("/account/accountEdit")
    public String editUserAccount(User user, Model model, RedirectAttributes redirectAttributes){
        userService.save(user);
        model.addAttribute("pageTitle", "Your Account Info");
        redirectAttributes.addFlashAttribute("message","Your account details have been updated successfully");
        return "redirect:/account";
    }

    @PostMapping("/account/accountDelete")
    public String deleteAccount(HttpServletRequest request) throws UserNotFoundException {

        User user = userService.getByEmail(Utility.getEmailOfAuthenticatedUser(request));
        progressService.deleteProgressForStudent(user);
        teacherService.deleteByTeacherOrStudent(user);
        userService.delete(user.getId());
        return "redirect:/logout";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }












}
