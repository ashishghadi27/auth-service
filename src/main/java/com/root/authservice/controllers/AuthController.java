package com.root.authservice.controllers;

import com.root.authservice.context.SupplierContext;
import com.root.authservice.helpers.CookieHelper;
import com.root.authservice.service.LoginService;
import com.root.authservice.utils.SessionUtil;
import com.root.authservice.vo.AuthRequestVO;
import com.root.authservice.vo.AuthResponseVO;
import com.root.authservice.vo.OtpRequestVO;
import com.root.authservice.vo.OtpResponseVO;
import com.root.redis.exception.ValidationException;
import com.root.redis.services.RedisContextWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    public AuthResponseVO login(@RequestBody AuthRequestVO requestVO,
                                HttpServletResponse servletResponse,
                                HttpServletRequest serverHttpRequest) throws ValidationException {
        return loginService.login(requestVO, servletResponse, serverHttpRequest);
    }

    @PostMapping("/sendOtp")
    public OtpResponseVO sendOtp(@RequestBody AuthRequestVO requestVO,
                                 HttpServletResponse servletResponse,
                                 HttpServletRequest serverHttpRequest) throws ValidationException {
        return loginService.sendOtp(requestVO, servletResponse, serverHttpRequest);
    }

    @PostMapping("/validateOtp")
    public OtpResponseVO validateOtp(@RequestBody OtpRequestVO otpRequest,
                                     HttpServletResponse servletResponse,
                                     HttpServletRequest serverHttpRequest) throws ValidationException {
        return loginService.validateOtp(otpRequest, servletResponse, serverHttpRequest);
    }

    @PostMapping("/setPass")
    public String setPass() throws ValidationException {
        //cookieHelper.removeCookie(servletResponse, serverHttpRequest);
        return "deleted";
    }

}
