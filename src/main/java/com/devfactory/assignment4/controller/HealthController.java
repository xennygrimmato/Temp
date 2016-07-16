package com.devfactory.assignment4.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by vaibhavtulsyan on 17/07/16.
 */

@RequestMapping("/api")
@RestController
public class HealthController {

    @RequestMapping(value = "/health")
    public ResponseEntity getHealth() {
        return new ResponseEntity(null, HttpStatus.OK);
    }

}
