package ie.mtu.sqlspringapi.dto;

import lombok.Data; // Or add getters/setters manually if not using Lombok

@Data // Generates getters, setters, toString, etc.
public class SqlQueryRequest {
    private String sql; // Field to hold the SQL query string
}
