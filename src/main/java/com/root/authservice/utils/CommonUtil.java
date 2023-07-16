package com.root.authservice.utils;

import com.root.redis.exception.ValidationException;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.root.redis.constants.ExceptionConstants.INTERNAL_ERROR;

public final class CommonUtil {

    public static String getMd5HashedString(String password) throws ValidationException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(password.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashText = new StringBuilder(no.toString(16));
            while (hashText.length() < 32) {
                hashText.insert(0, "0");
            }
            return hashText.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new ValidationException.Builder().errorMessage(INTERNAL_ERROR).build();
        }

    }

}
