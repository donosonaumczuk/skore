package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.User;
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

import java.util.Arrays;
import java.util.Collection;

@ComponentScan("ar.itba.edu.paw.webapp.auth")
@Component
public class SkoreUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SkoreUserDetailsService.class);


    @Autowired
    private PremiumUserService us;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final PremiumUser user = us.findByUserName(username);
        if(user == null) {
            throw new UsernameNotFoundException("No user by the name " + username);
        }
        final Collection<? extends GrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_ADMIN"));
        LOGGER.trace("username found: {} with password: {} and authorities: {}", user.getUserName(), user.getPassword(), authorities);
        //System.out.println("\n\n\n\n" + "userFound = " + user.getUserName() + " " + user.getPassword() + "  " + authorities +"\n\n\n\n");
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(),
                authorities);

    }
}
