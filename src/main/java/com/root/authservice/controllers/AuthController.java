package com.root.authservice.controllers;

import com.root.authservice.helpers.CookieHelper;
import com.root.authservice.service.LoginService;
import com.root.authservice.utils.SessionUtil;
import com.root.authservice.vo.AuthRequestVO;
import com.root.authservice.vo.AuthResponseVO;
import com.root.authservice.vo.OtpRequestVO;
import com.root.authservice.vo.OtpResponseVO;
import com.root.commondependencies.exception.ValidationException;
import com.root.redis.services.RedisContextWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class AuthController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private CookieHelper cookieHelper;

    @Autowired
    private RedisContextWrapper redisContextWrapper;

    @Autowired
    private SessionUtil sessionUtil;

    @PostMapping("/login")
    public AuthResponseVO login(@RequestBody AuthRequestVO requestVO) throws ValidationException {
        return loginService.login(requestVO);
    }

    @PostMapping("/sendOtp")
    public OtpResponseVO sendOtp(@RequestBody AuthRequestVO requestVO) throws ValidationException {
        return loginService.sendOtp(requestVO);
    }

    @PostMapping("/validateOtp")
    public OtpResponseVO validateOtp(@RequestBody OtpRequestVO otpRequest) throws ValidationException {
        return loginService.validateOtp(otpRequest);
    }

    @PostMapping("/setPass")
    public String setPass() throws ValidationException {
        return "deleted";
    }

}
