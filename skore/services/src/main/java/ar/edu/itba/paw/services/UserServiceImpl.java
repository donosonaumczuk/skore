package ar.edu.itba.paw.services;

import org.springframework.stereotype.Service;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.User;
@Service
public class UserServiceImpl implements UserService {

    public User findById(String id) {
        // TODO Auto-generated method stub
        return new User();
    }

}
