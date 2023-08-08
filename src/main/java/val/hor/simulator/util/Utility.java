package val.hor.simulator.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import val.hor.simulator.entity.setting.EmailSettingBag;

import java.util.Properties;

public class Utility {

    public static String getSiteUrl(HttpServletRequest request){
        String siteUrl = request.getRequestURL().toString();
        return siteUrl.replace(request.getServletPath(),"");
    }

    public static String getEmailOfAuthenticatedUser(HttpServletRequest request){
        Object principal = request.getUserPrincipal();
        if(principal instanceof UsernamePasswordAuthenticationToken
                || principal instanceof RememberMeAuthenticationToken){
            return request.getUserPrincipal().getName();
        } else {
            return null;
        }
    }

    public static JavaMailSenderImpl prepareMailSender(EmailSettingBag settings){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(settings.getHost());
        mailSender.setPort(settings.getPort());
        mailSender.setUsername(settings.getUsername());
        mailSender.setPassword(settings.getPassword());

        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth",settings.getSmtpAuth());
        properties.setProperty("mail.smtp.starttls.enable",settings.getSmtpSecured());
        mailSender.setJavaMailProperties(properties);
        return mailSender;
    }
}
