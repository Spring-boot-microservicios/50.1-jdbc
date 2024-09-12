package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        final String jdbcUrl = "jdbc:h2:~/test;DB_CLOSE_DELAY=-1"; // /test; => nombre de DB // DB_CLOSE_DELAY=-1 => para test
        final String userDB = "sa"; // usuarios por default
        final String passwordDB = ""; // usuarios sin password

        final String scriptPath = "src/main/resources/schema.sql";
        final String initDB = "RUNSCRIPT FROM '" + scriptPath + "'"; // Inicializar el script de DB en memoria H2

        final String query = "SELECT " +
                "e.id, e.name, e.email, e.department_id, d.name AS department_name " +
                "FROM employees e " +
                "JOIN departments d ON e.department_id = d.id";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, userDB, passwordDB)) {
            Statement statement = connection.createStatement();
            statement.execute(initDB);

            ResultSet resultSet = statement.executeQuery(query);
            List<EmployeeDTO> employees = new ArrayList<>();

            while (resultSet.next()) {
                EmployeeDTO employeeDTO = new EmployeeDTO();
                employeeDTO.setId(resultSet.getInt("id"));
                employeeDTO.setName(resultSet.getString("name"));
                employeeDTO.setEmail(resultSet.getString("email"));
                employeeDTO.setDepartmentId(resultSet.getInt("department_id"));
                employeeDTO.setDepartmentName(resultSet.getString("department_name"));

                employees.add(employeeDTO);
            }

            employees.forEach(System.out::println);

        } catch (SQLException e) {
            System.err.println("Error DB: " + e.getMessage());
        }

    }

}