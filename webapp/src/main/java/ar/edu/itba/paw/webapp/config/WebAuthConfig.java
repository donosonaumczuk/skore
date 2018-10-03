package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.SkoreUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
@ComponentScan({"ar.edu.itba.paw.webapp.auth", "ar.edu.itba.paw.webapp.config"})
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SkoreUserDetailsService userDetailService;


    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.userDetailsService(userDetailService)
                .sessionManagement()
                    .invalidSessionUrl("/")
                .and().authorizeRequests()
                    .antMatchers("/", "/lang", "/profile/**", "/404UserNotFound", "/filterMatch",
                                "/404", "/match/*", "/confirm/**", "/joinMatch/*", "/joinMatchForm/*",
                            "/confirmMatch/*", "/cancelMatch","sport/**" ).permitAll()
                    .antMatchers("/create", "/login", "/joinMatchForm").anonymous()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/**").authenticated()
                .and().formLogin()
                    .usernameParameter("user_username")
                    .passwordParameter("user_password")
                    .defaultSuccessUrl("/", false)
                    .loginPage("/login")
                .and().rememberMe()
                    .rememberMeParameter("user_rememberme")
                    .userDetailsService(userDetailService)
                    .key("secretKey")
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30))
                .and().logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login")
                .and().exceptionHandling()
                    .accessDeniedPage("/403")
                .and().csrf().disable();
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico", "/403");
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
        //authManagerBuilder.userDetailsService(userDetailService).passwordEncoder(bCryptPasswordEncoder());
        authManagerBuilder.userDetailsService(userDetailService)
                .and().jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder())
                .usersByUsernameQuery("select username as principal, password as credentials, enabled from accounts where username = ?")
                .authoritiesByUsernameQuery("select username as principal, roles.roleName from userroles, roles where roles.roleId = userRoles.role AND username = ?");
    }
}
