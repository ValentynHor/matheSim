 package val.hor.simulator.security;


 import org.springframework.context.annotation.Bean;
 import org.springframework.context.annotation.Configuration;
 import org.springframework.security.authentication.AuthenticationProvider;
 import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
 import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
 import org.springframework.security.config.annotation.web.builders.HttpSecurity;
 import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
 import org.springframework.security.core.userdetails.UserDetailsService;
 import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
 import org.springframework.security.crypto.password.PasswordEncoder;
 import org.springframework.security.web.SecurityFilterChain;

 @Configuration
 @EnableMethodSecurity
 public class SecurityConfig {



     @Bean
     public UserDetailsService userDetailsService(){
         return new MatheSimUserDetailsService();
     }


     @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         http
                 .authorizeRequests()
                 .requestMatchers("/home", "/login", "/register","/verify","/account/save","/users/check_email","/aboutApp").permitAll()
                 .requestMatchers("/users/**","/settings/**","/teachers/**").hasAuthority("Admin")
                 .requestMatchers("/skills/**","/tasks/**","/students/**").hasAnyAuthority("Admin","Teacher")
                 .requestMatchers("/exams/**","/results/**","/progress/**","/account/**").hasAnyAuthority("Admin","Teacher","Student")

                 .anyRequest().authenticated()

                 .and()
                 .formLogin(customizer -> customizer
                         .loginPage("/login")
                         .usernameParameter("email")
                         .defaultSuccessUrl("/home", true)
                         .permitAll()
                 );

         return http.build();
     }
     @Bean
     public PasswordEncoder passwordEncoder() {
         return new BCryptPasswordEncoder();
     }

     @Bean
     public WebSecurityCustomizer webSecurityCustomizer() {
         return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**","/webfonts/**");
     }

     @Bean
     public AuthenticationProvider authenticationProvider() {
         DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
         authenticationProvider.setUserDetailsService(userDetailsService());
         authenticationProvider.setPasswordEncoder(passwordEncoder());
         return authenticationProvider;
     }

 }


