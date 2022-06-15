package org.example.amazing.db;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class UserDAO {
    static MysqlDataSource dataSource;

    static {
        dataSource = new MysqlDataSource();
        dataSource.setUser("admin");
        dataSource.setPassword("admin");
        dataSource.setServerName("amazingDB");
    }

    public UserDTO fetchUserByUsername(String username) throws SQLException {
        String query = "SELECT * FROM Users WHERE username=?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                UserDTO userDTO = new UserDTO();
                userDTO.setEmail(rs.getString("email"));
                userDTO.setFirstName(rs.getString("firstName"));
                userDTO.setLastName(rs.getString("lastName"));
                userDTO.setUsername(username);
                userDTO.setHashedPassword(rs.getString("pass").getBytes());
                userDTO.setSalt(rs.getString("salt").getBytes());
                return userDTO;
            }
        }
        return null;
    }
}
