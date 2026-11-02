package com.epfo.learn.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private static final Logger log = LogManager.getLogger(HelloController.class);

    @GetMapping("/hello")
    public String hello() {
        log.info("hello endpoint called");
        return "Hello from the EPFO learning project!";
    }
}