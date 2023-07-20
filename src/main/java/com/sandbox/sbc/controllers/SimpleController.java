package com.sandbox.sbc.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {

    private static final Logger logger = LoggerFactory.getLogger(SimpleController.class);

    @GetMapping("/simple")
    public ResponseEntity<String> basicInfos() {

        logger.info("Invocazione del SimpleController");
        return ResponseEntity.status(HttpStatus.OK).body("Sample text");
    }
}
