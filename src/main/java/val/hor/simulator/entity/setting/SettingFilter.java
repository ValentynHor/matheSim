package val.hor.simulator.entity.setting;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import val.hor.simulator.service.SettingService;

import java.io.IOException;
import java.util.List;

@Component
public class SettingFilter implements jakarta.servlet.Filter {

    @Autowired
    private SettingService settingService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String url = request.getRequestURL().toString();

        if(url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".png") || url.endsWith(".jpg") || url.endsWith(".woff2")){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }

        List<Setting> generalSettings = settingService.getGeneralSettings();
        generalSettings.forEach(setting -> {
            servletRequest.setAttribute(setting.getKey(),setting.getValue());
        });
        //System.out.println(url);
        filterChain.doFilter(servletRequest,servletResponse);
    }





}
