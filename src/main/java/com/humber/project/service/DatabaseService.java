package com.humber.project.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean isConnected() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class); // Select 1 is used to test connection
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}