package ie.mtu.sqlspringapi.controller;

import ie.mtu.sqlspringapi.dto.SqlQueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/query")
public class QueryController {

    private static final Logger log = LoggerFactory.getLogger(QueryController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate; // Spring automatically configures this

    @PostMapping
    public ResponseEntity<?> executeSql(@RequestBody SqlQueryRequest request) {
        if (request == null || request.getSql() == null || request.getSql().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "SQL query string is missing or empty"));
        }

        String sql = request.getSql().trim();
        log.info("Executing SQL: {}", sql);

        try {
            // Basic check: Distinguish SELECT from others for result handling
            // WARNING: This is a simplistic check and not foolproof for complex SQL.
            if (sql.toUpperCase().startsWith("SELECT")) {
                // Execute query expecting results
                List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
                log.info("Query returned {} rows", results.size());
                return ResponseEntity.ok(results);
            } else {
                // Execute DDL/DML (INSERT, UPDATE, DELETE, CREATE, ALTER, etc.)
                int rowsAffected = jdbcTemplate.update(sql); // Use update for non-select potentially affecting rows
                // For DDL (like CREATE TABLE), rowsAffected might be 0. execute() could also be used.
                log.info("Execution affected {} rows (or completed for DDL)", rowsAffected);
                return ResponseEntity.ok(Map.of(
                        "message", "Execution successful",
                        "rowsAffected", rowsAffected)
                );
            }
        } catch (Exception e) {
            log.error("Error executing SQL [{}]: {}", sql, e.getMessage(), e);
            // Return the database error message for comparison purposes
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Failed to execute SQL",
                            "details", e.getMessage(), // Send back the actual DB error
                            "sqlExecuted", sql
                    ));
        }
    }
}