package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.JWTService;
import org.springframework.stereotype.Service;

@Service
public class JWTServiceImpl implements JWTService {


    public JWTServiceImpl() {

    }

    @Override
    public boolean isInBlacklist(String jwtoken) {//TODO: connect with database
        return false;
    }
}
