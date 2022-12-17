package com.ecohome.service.hash;

//> imports crypto and security libraries

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;

//> imports exceptions
import java.security.spec.InvalidKeySpecException;
import java.security.NoSuchAlgorithmException;

//> misc imports
import java.math.BigInteger;

public class Hash {


    public String getHashedPassword(String password) {
        int iterations = 100;
        char[] chars = password.toCharArray();

        byte[] salt = getSalt();
        if (salt == null) return null;

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);

        byte[] hash;

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hash = skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }

        return iterations + ":" + toHex(salt) + ":" + toHex(hash);

    }

    public boolean validatePassword(String originalPassword, String storedPassword) {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);

        byte[] salt;
        byte[] hash;

        try {
            salt = fromHex(parts[1]);
            hash = fromHex(parts[2]);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(),
                salt, iterations, hash.length * 8);

        byte[] testHash;
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            testHash = skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return false;
        }

        int diff = hash.length ^ testHash.length;
        for (int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    private byte[] fromHex(String hex)
    {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i < bytes.length ;i++) {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    private byte[] getSalt()
    {
        byte[] salt;
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            salt = new byte[16];
            sr.nextBytes(salt);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        return salt;
    }

    private String toHex(byte[] array)
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);

        int paddingLength = (array.length * 2) - hex.length();

        if(paddingLength > 0)
            return String.format("%0"  + paddingLength + "d", 0) + hex;

        return hex;

    }
}
