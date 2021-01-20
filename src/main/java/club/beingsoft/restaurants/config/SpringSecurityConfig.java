package club.beingsoft.restaurants.config;

import club.beingsoft.restaurants.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";
    @Autowired
    private UserService service;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(service).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/rest/dishes/**").hasAnyRole(ADMIN, USER)
                .antMatchers("/rest/dishes/**").hasRole(ADMIN)
                .antMatchers(HttpMethod.GET, "/rest/menus/**").hasAnyRole(ADMIN, USER)
                .antMatchers("/rest/menus/**").hasRole(ADMIN)
                .antMatchers(HttpMethod.GET, "/rest/restaurants/**").hasAnyRole(ADMIN, USER)
                .antMatchers("/rest/restaurants/**").hasRole(ADMIN)
                .antMatchers("/rest/users/**").hasRole(ADMIN)
                .antMatchers("/rest/votes/**").hasAnyRole(ADMIN, USER)
                .antMatchers("/v2/api-docs/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html").permitAll()
                .antMatchers("/**").denyAll()
                .and().httpBasic()
                .and().csrf().disable()
                .authorizeRequests().anyRequest().authenticated();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
