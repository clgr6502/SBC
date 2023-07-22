package com.sandbox.sbc.services;

import com.sandbox.sbc.entities.DbUser;
import com.sandbox.sbc.repositories.DbUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class SimpleService {

    @Autowired
    private DbUserRepository dbUserRepository;

    private static final Logger logger = LoggerFactory.getLogger(SimpleService.class);

    public void modifyUserAttrib(String value, Long id) {

        logger.info("Modifica parametro dell'utente di id {}", id);

        Optional<DbUser> user = dbUserRepository.findById(id);

        if(user.isEmpty()) {
            logger.error("Utente di id {} non esiste", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if("t".equals(value)) {
            user.get().setFlag(true);
        } else if("f".equals(value)) {
            user.get().setFlag(false);
        } else {
            logger.error("Parametro {} non valido", value);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        dbUserRepository.save(user.get());
    }
}
