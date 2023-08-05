package com.root.authservice.impl;

import com.root.authservice.context.SupplierContext;
import com.root.authservice.helpers.CookieHelper;
import com.root.authservice.proxy.UserProxy;
import com.root.authservice.service.LoginService;
import com.root.authservice.utils.CommonUtil;
import com.root.authservice.utils.SendEmailUtil;
import com.root.authservice.utils.SessionUtil;
import com.root.authservice.utils.ValidationUtil;
import com.root.authservice.vo.*;
import com.root.redis.exception.ValidationException;
import com.root.redis.services.RedisContextWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    private final UserProxy userProxy;

    private final CookieHelper cookieHelper;

    private final RedisContextWrapper redisContextWrapper;

    private final SessionUtil sessionUtil;

    private final SendEmailUtil sendEmailUtil;

    @Autowired
    public LoginServiceImpl(UserProxy userProxy,
                            CookieHelper cookieHelper,
                            RedisContextWrapper redisContextWrapper,
                            SessionUtil sessionUtil,
                            SendEmailUtil sendEmailUtil){
        this.userProxy = userProxy;
        this.cookieHelper = cookieHelper;
        this.redisContextWrapper = redisContextWrapper;
        this.sessionUtil = sessionUtil;
        this.sendEmailUtil = sendEmailUtil;
    }

    @Override
    public AuthResponseVO login(AuthRequestVO request) throws ValidationException {
        AuthResponseVO authResponse = new AuthResponseVO();
        String sessionId = sessionUtil.getSessionId();
        try{
            ValidationUtil.validateRequest(request);
            UserVO userVO = userProxy.getUserByEmail(request.getEmailId());
            if(ValidationUtil.isValidUser(request.getPassword(), userVO.getPassword())){
                authResponse.setValidUser(true);
                userVO.setPassword(null);

                SupplierContext supplierContext = new SupplierContext();
                supplierContext.setUserVO(userVO);
                redisContextWrapper.setContext(sessionId, supplierContext);


                authResponse.setUser(userVO);
                cookieHelper.setCookie(userVO);
            }
        }
        catch (ValidationException e){
            //LOGGING
            throw e;
        }
        return authResponse;
    }

    @Override
    public OtpResponseVO sendOtp(AuthRequestVO request) throws ValidationException {

        String sessionId = sessionUtil.getSessionId();

        ValidationUtil.validateEmail(request.getEmailId());

        UserVO userVO = userProxy.getUserByEmail(request.getEmailId());
        if(userVO != null && StringUtils.isNotEmpty(userVO.getEmail())){
            String otp = CommonUtil.generateOtp();

            SupplierContext supplierContext = new SupplierContext();
            supplierContext.setOtp(otp);
            supplierContext.setUserVO(userVO);

            OtpResponseVO otpResponseVO = new OtpResponseVO();
            if(sendEmailUtil.sendMail(otp, request.getEmailId())){
                redisContextWrapper.setContext(sessionId, supplierContext);
                cookieHelper.setCookie();
                otpResponseVO.setResponseCode("200");
                otpResponseVO.setResponseMsg("SEND_OTP_SUCCESS");
                return otpResponseVO;
            }
            throw new ValidationException.Builder().errorMessage("SEND_OTP_FAILED").build();
        }
        throw new ValidationException.Builder().errorMessage("INVALID_USER").build();
    }

    @Override
    public OtpResponseVO validateOtp(OtpRequestVO otpRequest) throws ValidationException {
        String sessionId = sessionUtil.getSessionId();

        SupplierContext supplierContext = redisContextWrapper.getContext(sessionId, SupplierContext.class);
        ValidationUtil.validateContext(supplierContext);

        String generatedOtp = supplierContext.getOtp();

        OtpResponseVO otpResponseVO = new OtpResponseVO();
        if(StringUtils.isNotEmpty(generatedOtp) && generatedOtp.equalsIgnoreCase(otpRequest.getOtp())){
            cookieHelper.setCookie(supplierContext.getUserVO());

            supplierContext.setOtp(null);
            redisContextWrapper.setContext(sessionId, supplierContext);

            otpResponseVO.setResponseMsg("VERIFY_OTP_SUCCESS");
            return otpResponseVO;
        }
        throw new ValidationException.Builder().errorMessage("VERIFY_OTP_FAILED").build();
    }
}
