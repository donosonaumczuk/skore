package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.exceptions.notfound.UserNotFoundException;
import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@ComponentScan("ar.itba.edu.paw.webapp.auth")
@Component
public class SkoreUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SkoreUserDetailsService.class);

    @Autowired
    private PremiumUserService us;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        PremiumUser user = us.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username: " + username));
        final Collection<GrantedAuthority> authorities = new ArrayList<>();
        Set<Role> roles = user.getRoles();
        for(Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        LOGGER.trace("username found: {} with password: {} and authorities: {}", user.getUserName(), user.getPassword(), authorities);
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }
}
