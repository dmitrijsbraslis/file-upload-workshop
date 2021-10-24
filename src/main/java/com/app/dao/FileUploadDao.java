package com.app.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FileUploadDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void storeFileData(String fileName) {
        jdbcTemplate.update("INSERT INTO files (name) VALUES (?)", fileName);
    }

    public List<String> getAllFiles() {
        RowMapper<String> rowMapper = (rs, rowNumber) -> mapFileName(rs);
        return jdbcTemplate.query("SELECT name FROM files", rowMapper);
    }

    private String mapFileName(ResultSet rs) throws SQLException {
        return rs.getString("name");
    }
}
