package com.root.authservice.controllers;

import com.root.authservice.service.LoginService;
import com.root.authservice.vo.AuthRequestVO;
import com.root.authservice.vo.AuthResponseVO;
import com.root.redis.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class AuthController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public AuthResponseVO login(@RequestBody AuthRequestVO requestVO,
                                HttpServletResponse servletResponse,
                                HttpServletRequest serverHttpRequest) throws ValidationException {
        return loginService.login(requestVO, servletResponse, serverHttpRequest);
    }

}
