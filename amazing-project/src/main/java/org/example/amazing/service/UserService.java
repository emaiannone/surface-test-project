package org.example.amazing.service;

import org.example.amazing.core.UserBean;
import org.example.amazing.db.UserDAO;
import org.example.amazing.db.UserDTO;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Arrays;

public class UserService {
    public static final MessageDigest passwordHasher;
    static {
        try {
            passwordHasher = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean register(UserBean user, String suppliedPwd) {
        byte[] salt = generateSalt();
        byte[] hashedPassword = hashPassword(suppliedPwd, salt);
        UserDTO userDto = new UserDTO();
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setUsername(user.getUsername());
        userDto.setHashedPassword(hashedPassword);
        userDto.setSalt(salt);
        UserDAO userDao = new UserDAO();
        try {
            return userDao.storeUser(userDto);
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean login(String suppliedPwd, byte[] userSalt, byte[] storedPasswordHash) {
        byte[] hashedInputPass = hashPassword(suppliedPwd, userSalt);
        return Arrays.equals(hashedInputPass, storedPasswordHash);
    }

    private static byte[] hashPassword(String pwd, byte[] salt) {
        passwordHasher.update(salt);
        return passwordHasher.digest(pwd.getBytes(StandardCharsets.UTF_8));
    }

    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
}
