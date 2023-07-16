package com.root.authservice.utils;


import com.root.authservice.vo.AuthRequestVO;
import com.root.redis.exception.ValidationException;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.root.redis.constants.ExceptionConstants.INVALID_REQUEST;

public class ValidationUtil {

    private static final String emailRegex = "[a-zA-Z0-9][a-zA-Z0-9_.]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+";

    public static void validateRequest(AuthRequestVO requestVO) throws ValidationException {
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(requestVO.getEmailId());
        if(!matcher.find() || StringUtils.isEmpty(requestVO.getPassword())){
            throw new ValidationException.Builder().errorMessage(INVALID_REQUEST).build();
        }
    }

    public static boolean isValidUser(String requestPassword, String actualPassword) throws ValidationException {
        String hashedPassword = CommonUtil.getMd5HashedString(requestPassword);
        return hashedPassword.equals(actualPassword);
    }

}
