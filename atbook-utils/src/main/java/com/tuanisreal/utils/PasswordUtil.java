package com.tuanisreal.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

@Slf4j
public class PasswordUtil {
    private static SecretKeyFactory factory;
    private static SecureRandom random;

    static {
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            random = new SecureRandom();
        } catch (NoSuchAlgorithmException ex) {
            log.error("NoSuchAlgorithmException", ex);
            throw new RuntimeException(ex);
        }
    }

    private PasswordUtil() {
    }

    public static String generateSalt(){
        byte[] salt = new byte[64];
        random.nextBytes(salt);
        return Util.byteToString(salt);
    }

    public static String encryptPassword(String password, String salt) {
        byte[] hash;
        long start = System.currentTimeMillis();
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 212212, 256);
        try {
            hash = factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        long end = System.currentTimeMillis();
        System.out.println("Time encrypt: " + (end - start));
        return Util.byteToString(hash);
    }
}
