package com.root.authservice.service;

import com.root.authservice.vo.AuthRequestVO;
import com.root.authservice.vo.AuthResponseVO;
import com.root.authservice.vo.OtpRequestVO;
import com.root.authservice.vo.OtpResponseVO;
import com.root.redis.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface LoginService {

    AuthResponseVO login(AuthRequestVO request) throws ValidationException;


    OtpResponseVO sendOtp(AuthRequestVO request) throws ValidationException;

    OtpResponseVO validateOtp(OtpRequestVO otpRequest) throws ValidationException;

}
