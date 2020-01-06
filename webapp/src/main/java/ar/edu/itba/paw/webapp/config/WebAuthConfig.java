package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.JasonWebToken.JWTUserDetailsAuthProvider;
import ar.edu.itba.paw.webapp.auth.SkoreUserDetailsService;
import ar.edu.itba.paw.webapp.auth.loginFilter.LoginAuthFailureHandler;
import ar.edu.itba.paw.webapp.auth.loginFilter.LoginAuthFilter;
import ar.edu.itba.paw.webapp.auth.loginFilter.LoginAuthSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
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
    private JWTUserDetailsAuthProvider jwtUserDetailsAuthProvider;

    @Autowired
    private LoginAuthSuccessHandler loginAuthSuccessHandler;

    @Autowired
    private LoginAuthFailureHandler loginAuthFailureHandler;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.userDetailsService(userDetailService)
                .addFilterBefore(createLoginAuthFilter(), UsernamePasswordAuthenticationFilter.class) // Use JSON login for initial authentication
                //TODO add filter for access endoints with account
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().logout().disable()
                .rememberMe().disable()
                .csrf().disable();
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico", "/403"); //TODO: check url
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
        authManagerBuilder.authenticationProvider(jwtUserDetailsAuthProvider)
                .userDetailsService(userDetailService)
                .passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean
    public LoginAuthFilter createLoginAuthFilter() throws Exception {
        LoginAuthFilter filter = new LoginAuthFilter();
        filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login", "POST"));
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(loginAuthSuccessHandler);
        filter.setAuthenticationFailureHandler(loginAuthFailureHandler);
        return filter;
    }
}
