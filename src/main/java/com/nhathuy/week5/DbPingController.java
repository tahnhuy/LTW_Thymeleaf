package com.nhathuy.week5;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DbPingController {

    private final JdbcTemplate jdbc;

    public DbPingController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping("/db/ping")
    public String ping() {
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM dbo.Categories", Integer.class);
        return "DB OK, categories=" + n;
    }
}


