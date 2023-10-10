package pl.iwaniuk.webapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import pl.iwaniuk.webapi.services.UserServices;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private  AuthSuccesHandler succesHandler;
    @Autowired
    private AuthFailureHandler failureHandler;
    @Value("${jwt_prop.secretKey}") String secret;
    @Autowired
    UserServices userServices;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http.csrf().disable();
       http.cors().disable();
       http.authorizeRequests()
               //swagger
               .antMatchers("/v2/api-docs",
                       "/configuration/ui",
                       "/swagger-resources/**",
                       "/configuration/security",
                       "/swagger-ui.html",
                       "/webjars/**").permitAll()
               //autoryzacja
               .antMatchers("/login","/register","/registerSpecialUser").permitAll()
                //komentarze
               .antMatchers("/api/comments/**").authenticated()
               //posty
               .antMatchers("/api/posts/**").authenticated()
               //kursy
               .antMatchers(HttpMethod.GET,"/api/courses/**").authenticated()
               .antMatchers(HttpMethod.POST,"/api/courses/**").hasAnyRole("EDUCATOR","ADMIN")
               .antMatchers(HttpMethod.PUT,"/api/courses/**").hasAnyRole("EDUCATOR","ADMIN")
               .antMatchers(HttpMethod.DELETE,"/api/courses/**").hasAnyRole("EDUCATOR","ADMIN")
               //eventy
               .antMatchers(HttpMethod.GET,"/api/events/**").permitAll()
               .antMatchers(HttpMethod.POST,"/api/events/**").hasAnyRole("ADMIN")
               .antMatchers(HttpMethod.PUT,"/api/events/**").hasAnyRole("ADMIN")
               .antMatchers(HttpMethod.DELETE,"/api/events/**").hasAnyRole("ADMIN")
               //edge_przypisane_kursy
               .antMatchers(HttpMethod.GET,"/api/groups/course/**").authenticated()
               .antMatchers(HttpMethod.POST,"/api/groups/course/**").hasAnyRole("EDUCATOR","ADMIN")
               .antMatchers(HttpMethod.PUT,"/api/groups/course/**").hasAnyRole("EDUCATOR","ADMIN")
               .antMatchers(HttpMethod.DELETE,"/api/groups/course/**").hasAnyRole("EDUCATOR","ADMIN")
               //grupa_czlonkowie
               .antMatchers("/api/groups/member/**").authenticated()
               //grupa
               .antMatchers(HttpMethod.GET,"/api/groups/**").authenticated()
               .antMatchers(HttpMethod.POST,"/api/groups/**").hasAnyRole("EDUCATOR","ADMIN")
               .antMatchers(HttpMethod.PUT,"/api/groups/**").hasAnyRole("EDUCATOR","ADMIN")
               .antMatchers(HttpMethod.DELETE,"/api/groups/**").hasAnyRole("EDUCATOR","ADMIN")
               //pliki
               .antMatchers("/api/files/**").permitAll()
               //rodzaje
               .antMatchers(HttpMethod.GET,"/api/kinds/**").permitAll()
               .antMatchers(HttpMethod.POST,"/api/kinds/**").hasAnyRole("ADMIN")
               .antMatchers(HttpMethod.PUT,"/api/kinds/**").hasAnyRole("ADMIN")
               .antMatchers(HttpMethod.DELETE,"/api/kinds/**").hasAnyRole("ADMIN")
               //uzykownik
               .antMatchers("/api/users/**").authenticated()

               .antMatchers("/api/hello").permitAll()
               .antMatchers("/api/hello2").hasRole("ADMIN")
               .anyRequest().authenticated()
               .and()
               .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
               .and()
               .addFilter(authFilter())
               .addFilter(new JWT_Auth_Filter(authenticationManager(),userServices,secret))
               .exceptionHandling()
               .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
    }


    public JsonAuthFilter authFilter() throws Exception {
        JsonAuthFilter jsonAuthFilter = new JsonAuthFilter(objectMapper);
        jsonAuthFilter.setAuthenticationSuccessHandler(succesHandler);
        jsonAuthFilter.setAuthenticationFailureHandler(failureHandler);
        jsonAuthFilter.setAuthenticationManager(super.authenticationManager());
        return jsonAuthFilter;
    }
}
