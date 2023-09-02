package com.root.authservice.service;

import com.root.authservice.vo.*;
import com.root.commondependencies.exception.ValidationException;
import com.root.commondependencies.vo.DelRequestVO;
import com.root.commondependencies.vo.UserVO;

public interface LoginService {

    AuthResponseVO login(AuthRequestVO request) throws ValidationException;


    OtpResponseVO sendOtp(AuthRequestVO request) throws ValidationException;

    OtpResponseVO validateOtp(OtpRequestVO otpRequest) throws ValidationException;

    RegisterResponseVO register(UserVO requestVO) throws ValidationException;

    DelResponseVO delete(DelRequestVO requestVO) throws ValidationException;
}
