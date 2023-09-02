package com.root.authservice.controllers;

import com.root.authservice.helpers.CookieHelper;
import com.root.authservice.service.LoginService;
import com.root.authservice.utils.SessionUtil;
import com.root.authservice.vo.*;
import com.root.commondependencies.exception.ValidationException;
import com.root.commondependencies.vo.DelRequestVO;
import com.root.commondependencies.vo.UserVO;
import com.root.redis.services.RedisContextWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/register")
    public RegisterResponseVO register(@RequestBody UserVO requestVO) throws ValidationException {
        return loginService.register(requestVO);
    }
    @DeleteMapping("/delete")
    public DelResponseVO delete(@RequestBody DelRequestVO requestVO) throws ValidationException {
        return loginService.delete(requestVO);
    }

}
