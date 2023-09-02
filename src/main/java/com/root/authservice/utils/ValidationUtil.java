package com.root.authservice.utils;


import com.root.authservice.context.SupplierContext;
import com.root.authservice.vo.AuthRequestVO;
import com.root.commondependencies.exception.ValidationException;
import com.root.commondependencies.vo.UserVO;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.root.authservice.utils.Constants.INVALID_EMAIL;
import static com.root.redis.constants.ExceptionConstants.INVALID_REQUEST;

public final class ValidationUtil {

    private static final String emailRegex = "[a-zA-Z0-9][a-zA-Z0-9_.]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+";

    public static void validateRequest(AuthRequestVO requestVO) throws ValidationException {
        validateEmail(requestVO.getEmailId());
        if (StringUtils.isEmpty(requestVO.getPassword())) {
            throw new ValidationException.Builder().errorMessage(INVALID_REQUEST).build();
        }
    }

    public static boolean isValidUser(String requestPassword, String actualPassword) throws ValidationException {
        String hashedPassword = CommonUtil.getMd5HashedString(requestPassword);
        return hashedPassword.equals(actualPassword);
    }


    public static void validateEmail(String email) throws ValidationException {
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.find()) {
            throw new ValidationException.Builder().errorMessage(INVALID_EMAIL).build();
        }
    }

    public static void validateContext(SupplierContext supplierContext) throws ValidationException {
        if (supplierContext == null) {
            throw new ValidationException.Builder().errorMessage(INVALID_REQUEST).build();
        }
    }

    public static void validateRegisterUser(UserVO userVO, UserVO requestVO) throws ValidationException {
        if (requestVO.getName().equals(userVO.getName())) {
            throw new ValidationException.Builder().errorMessage("User Already Exists!.").build();
        }
        if (requestVO.getEmail().equals(userVO.getEmail())) {
            throw new ValidationException.Builder().errorMessage("Email is already exists!.").build();
        }
    }

}
