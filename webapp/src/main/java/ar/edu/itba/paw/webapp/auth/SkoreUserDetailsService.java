package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@ComponentScan("ar.itba.edu.paw.webapp.auth")
@Component
public class SkoreUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SkoreUserDetailsService.class);

    @Autowired
    private PremiumUserService us;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final Optional<PremiumUser> user = us.findByUserName(username);
        if(!user.isPresent()) {
            throw new UsernameNotFoundException("No user by the name " + username);
        }
        final PremiumUser currentUser = user.get();
        final Collection<GrantedAuthority> authorities = new ArrayList<>();
        Set<Role> roles = currentUser.getRoles();
        for(Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        LOGGER.trace("username found: {} with password: {} and authorities: {}", currentUser.getUserName(), currentUser.getPassword(), authorities);
        return new org.springframework.security.core.userdetails.User(username, currentUser.getPassword(),
                authorities);

    }

}
