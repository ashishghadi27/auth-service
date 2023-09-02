package com.root.authservice.impl;

import com.root.authservice.config.ConsulConfig;
import com.root.authservice.context.SupplierContext;
import com.root.authservice.helpers.CookieHelper;
import com.root.authservice.proxy.UserProxy;
import com.root.authservice.service.AsyncService;
import com.root.authservice.service.LoginService;
import com.root.authservice.utils.*;
import com.root.authservice.vo.*;
import com.root.commondependencies.exception.ValidationException;
import com.root.commondependencies.vo.DelRequestVO;
import com.root.commondependencies.vo.UserVO;
import com.root.redis.services.RedisContextWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

@Service
public class LoginServiceImpl implements LoginService {

    private final UserProxy userProxy;

    private final CookieHelper cookieHelper;

    private final RedisContextWrapper redisContextWrapper;

    private final SessionUtil sessionUtil;

    private final AsyncService asyncService;

    private final ConsulConfig config;

    @Autowired
    public LoginServiceImpl(UserProxy userProxy,
                            CookieHelper cookieHelper,
                            RedisContextWrapper redisContextWrapper,
                            SessionUtil sessionUtil,
                            AsyncService asyncService,
                            ConsulConfig config) {
        this.userProxy = userProxy;
        this.cookieHelper = cookieHelper;
        this.redisContextWrapper = redisContextWrapper;
        this.sessionUtil = sessionUtil;
        this.asyncService = asyncService;
        this.config = config;
    }

    @Override
    public AuthResponseVO login(AuthRequestVO request) throws ValidationException {
        AuthResponseVO authResponse = new AuthResponseVO();
        String sessionId = sessionUtil.getSessionId();
        try {
            ValidationUtil.validateRequest(request);
            UserVO userVO = userProxy.getUserByEmail(request.getEmailId());
            if (ValidationUtil.isValidUser(request.getPassword(), userVO.getPassword())) {
                authResponse.setValidUser(true);
                userVO.setPassword(null);

                SupplierContext supplierContext = new SupplierContext();
                supplierContext.setUserVO(userVO);
                redisContextWrapper.setContext(sessionId, supplierContext);


                authResponse.setUser(userVO);
                cookieHelper.setCookie(userVO);
            }
        } catch (ValidationException e) {
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
        if (userVO != null && StringUtils.isNotEmpty(userVO.getEmail())) {
            String otp = CommonUtil.generateOtp();

            SupplierContext supplierContext = new SupplierContext();
            supplierContext.setOtp(otp);
            supplierContext.setUserVO(userVO);
            supplierContext.setOtpSentTime(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

            redisContextWrapper.setContext(sessionId, supplierContext);
            cookieHelper.setCookie();

            OtpResponseVO otpResponseVO = new OtpResponseVO();
            otpResponseVO.setResponseCode("200");
            otpResponseVO.setResponseMsg("SEND_OTP_SUCCESS");

            CompletableFuture.runAsync(() -> {
                try {
                    asyncService.sendEmail(otp, request.getEmailId());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            return otpResponseVO;
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
        if (StringUtils.isNotEmpty(generatedOtp)
                && generatedOtp.equalsIgnoreCase(otpRequest.getOtp())) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime otpSentTime = LocalDateTime.parse(supplierContext.getOtpSentTime(),
                    DateTimeFormatter.ISO_DATE_TIME);
            int otpTimeOut = config.getConfigValueByKey("OTP_TIMEOUT_IN_MINS", Constants.OTP_TIMEOUT_IN_MINS);
            otpSentTime = otpSentTime.plusMinutes(otpTimeOut);
            if ((currentDateTime.isBefore(otpSentTime) || currentDateTime.equals(otpSentTime))) {
                cookieHelper.setCookie(supplierContext.getUserVO());

                supplierContext.setOtp(null);
                redisContextWrapper.setContext(sessionId, supplierContext);

                otpResponseVO.setResponseMsg("VERIFY_OTP_SUCCESS");
                return otpResponseVO;
            }
            throw new ValidationException.Builder().errorMessage("OTP_EXPIRED").build();
        }
        throw new ValidationException.Builder().errorMessage("VERIFY_OTP_FAILED").build();
    }

    @Override
    public RegisterResponseVO register(UserVO requestVO) throws ValidationException {
        RegisterResponseVO registerResponseVO = new RegisterResponseVO();
        String sessionId = sessionUtil.getSessionId();
        try {

            ValidationUtil.validateEmail(requestVO.getEmail());
            UserVO userVO = userProxy.getUserByEmail(requestVO.getEmail());
            ValidationUtil.validateRegisterUser(userVO, requestVO);
            SupplierContext supplierContext = redisContextWrapper.getContext(sessionId, SupplierContext.class);
            String role = supplierContext.getUserVO().getRole();
            if (("ADMIN").equals(role)) {
                requestVO.setPassword(CommonUtil.getMd5HashedString(requestVO.getPassword()));
                userProxy.createUser(requestVO);
                registerResponseVO.setRegisterUserSuccessful(true);
            } else {
                registerResponseVO.setRegisterUserSuccessful(false);
            }
        } catch (ValidationException e) {
            //LOGGING
            throw e;
        }
        return registerResponseVO;
    }

    @Override
    public DelResponseVO delete(DelRequestVO requestVO) throws ValidationException {
        DelResponseVO delResponseVO = new DelResponseVO();
        String sessionId = sessionUtil.getSessionId();
        try {
            ValidationUtil.validateEmail(requestVO.getEmailId());
            UserVO userVO = userProxy.getUserByEmail(requestVO.getEmailId());
            SupplierContext supplierContext = redisContextWrapper.getContext(sessionId, SupplierContext.class);
            String role = supplierContext.getUserVO().getRole();
            if (("ADMIN").equals(role)) {
                if (requestVO.getEmailId().equals(userVO.getEmail())) {
                    userProxy.deleteUser(requestVO);
                    delResponseVO.setUserdeletedSuccessful(true);
                }
            }

        } catch (ValidationException e) {
            //LOGGING
            throw e;
        }
        return delResponseVO;
    }
}
