package com.sandbox.sbc.controllers;

import com.sandbox.sbc.services.SimpleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/simple")
public class SimpleController {

    private static final Logger logger = LoggerFactory.getLogger(SimpleController.class);

    @Autowired
    private SimpleService simpleService;

    @GetMapping
    public ResponseEntity<String> basicInfos() {

        logger.info("Invocazione del SimpleController");
        return ResponseEntity.status(HttpStatus.OK).body("Sample text");
    }

    @PutMapping("/flag/{value}/{id}")
    public ResponseEntity<String> modifyUserAttribute(@PathVariable("value") String value,
                                                      @PathVariable("id") Long id) {

        logger.info("Invocazione del metodo di modifica flag");
        simpleService.modifyUserAttrib(value, id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
