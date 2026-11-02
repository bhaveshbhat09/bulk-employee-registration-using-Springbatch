package com.epfo.learn.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DbCheckController {

    private static final Logger log = LogManager.getLogger(DbCheckController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/dbcheck")
    public String dbcheck() {
        String user = jdbcTemplate.queryForObject("SELECT USER FROM dual", String.class);
        log.debug("dbcheck connected as {}", user);
        return "Connected as: " + user;
    }
}