package org.example.amazing.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
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
