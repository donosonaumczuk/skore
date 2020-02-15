package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.token.JWTUserDetailsAuthProvider;
import ar.edu.itba.paw.webapp.auth.SkoreUserDetailsService;
import ar.edu.itba.paw.webapp.auth.filters.login.LoginAuthFailureHandler;
import ar.edu.itba.paw.webapp.auth.filters.login.LoginAuthFilter;
import ar.edu.itba.paw.webapp.auth.filters.login.LoginAuthSuccessHandler;
import ar.edu.itba.paw.webapp.auth.filters.session.SessionAuthFailureHandler;
import ar.edu.itba.paw.webapp.auth.filters.session.SessionAuthFilter;
import ar.edu.itba.paw.webapp.auth.filters.session.SessionAuthSuccessHandler;
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
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

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
                .addFilterBefore(createSessionAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().logout().disable()
                .rememberMe().disable()
                .csrf().disable();
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/css/**", "/js/**", "/media/**", "/favicon.ico"); //TODO: check url
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

    //TODO: get endpoints from another class
    @Bean
    public LoginAuthFilter createLoginAuthFilter() throws Exception {
        LoginAuthFilter filter = new LoginAuthFilter();
        filter.setRequiresAuthenticationRequestMatcher(new RegexRequestMatcher("/api/auth/login/?", "POST"));
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(loginAuthSuccessHandler);
        filter.setAuthenticationFailureHandler(loginAuthFailureHandler);
        return filter;
    }

    @Bean
    public SessionAuthFilter createSessionAuthFilter() throws Exception {
        SessionAuthFilter filter = new SessionAuthFilter();
        filter.setAuthenticationManager(authenticationManager());
        filter.setRequiresAuthenticationRequestMatcher(needAuthEndpointsMatcher());
        filter.setAuthenticationSuccessHandler(new SessionAuthSuccessHandler());
        filter.setAuthenticationFailureHandler(new SessionAuthFailureHandler());
        return filter;
    }

    @Bean
    public RequestMatcher needAuthEndpointsMatcher() {
        return new OrRequestMatcher(//TODO make list
                new RegexRequestMatcher("/api/auth/logout/?", "POST"),
                new RegexRequestMatcher("/api/users/[a-zA-Z0-9_.]+/?", "PUT"),
                new RegexRequestMatcher("/api/users/[a-zA-Z0-9_.]+/?", "DELETE"),
                new RegexRequestMatcher("/api/users/[a-zA-Z0-9_.]+/likedUsers/?", "POST"),
                new RegexRequestMatcher("/api/users/[a-zA-Z0-9_.]+/likedUsers/[a-zA-Z0-9_.]+/?", "DELETE"),
                new RegexRequestMatcher("/api/matches/?", "POST"),
                new RegexRequestMatcher("/api/matches/[0-9.-a-zA-Z_]+/?", "PUT"),
                new RegexRequestMatcher("/api/matches/[0-9.-a-zA-Z_]+/?", "DELETE"),
                new RegexRequestMatcher("/api/matches/[0-9.-a-zA-Z_]+/result/?", "POST"),
                optionalAuthEndpointsMatcher(),
                adminAuthEndpointsMatcher()
        );
    }

    @Bean
    public RequestMatcher optionalAuthEndpointsMatcher() {
        return new OrRequestMatcher( //TODO make list
                new RegexRequestMatcher("/api/matches/?", "GET"),
                new RegexRequestMatcher("/api/users/[a-zA-Z0-9_.]+/matches/?", "GET"),
                new RegexRequestMatcher("/api/matches/[0-9.-a-zA-Z_]+/players/?", "POST"),
                new RegexRequestMatcher("/api/matches/[0-9.-a-zA-Z_]+/players/[0-9]+/?", "DELETE"),
                new RegexRequestMatcher("/api/matches/[0-9.-a-zA-Z_]+/players/code/\\w+/?", "DELETE")
        );
    }

    @Bean
    public RequestMatcher adminAuthEndpointsMatcher() {
        return new OrRequestMatcher(
                new AntPathRequestMatcher("/api/sports/**", "DELETE"),
                new AntPathRequestMatcher("/api/sports/**", "PUT"),
                new AntPathRequestMatcher("/api/sports/**", "POST")
        );
    }
}
