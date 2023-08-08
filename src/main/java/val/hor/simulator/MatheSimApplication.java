package val.hor.simulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import val.hor.simulator.entity.setting.SettingFilter;

@SpringBootApplication
public class MatheSimApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatheSimApplication.class, args);
	}


}
