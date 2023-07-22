package com.sandbox.sbc.services;

import com.sandbox.sbc.entities.DbUser;
import com.sandbox.sbc.mappers.UserMapper;
import com.sandbox.sbc.repositories.DbUserRepository;
import com.sandbox.sbc.requests.DbUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(JwtUserDetailsService.class);

    @Autowired
    private DbUserRepository dbUserRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        if ("admin".equals(username)) {
            return new User("admin", "$2a$12$sZUSIhyVLN7MByX.CzRVf.70b/985QVkGzOJHrF8xavhZpLqd9MfW",
                    List.of((GrantedAuthority) () -> "admin"));
        } else {

            Optional<DbUser> user = dbUserRepository.findByUsername(username);

            if(user.isEmpty()) {
                logger.error("Utente {} non trovato", username);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utente " + username + " non trovato");
            } else {
                return new User(user.get().getUsername(), user.get().getPassword(),
                        List.of((GrantedAuthority) () -> "user"));
            }
        }
    }

    public void saveUser(DbUserRequest request) {

        if(request.getUsername().equals("admin")) {

            logger.error("Utilizzato un nome non consentito");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        } else if (dbUserRepository.findByUsername(request.getUsername()).isPresent()) {

            logger.error("Nome utente già utilizzato");
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        DbUser user = UserMapper.dbUserRequestToEntity(request, encoder);
        user.setFlag(false);

        dbUserRepository.save(user);

        logger.info("Utente {} salvato", user.getUsername());
    }
}
