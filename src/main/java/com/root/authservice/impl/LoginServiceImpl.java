package com.root.authservice.impl;

import com.root.authservice.context.SupplierContext;
import com.root.authservice.helpers.CookieHelper;
import com.root.authservice.proxy.UserProxy;
import com.root.authservice.service.LoginService;
import com.root.authservice.utils.ValidationUtil;
import com.root.authservice.vo.AuthRequestVO;
import com.root.authservice.vo.AuthResponseVO;
import com.root.authservice.vo.UserVO;
import com.root.redis.exception.ValidationException;
import com.root.redis.services.RedisContextWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    private final UserProxy userProxy;

    private final CookieHelper cookieHelper;

    private final RedisContextWrapper redisContextWrapper;

    @Autowired
    public LoginServiceImpl(UserProxy userProxy,
                            CookieHelper cookieHelper,
                            RedisContextWrapper redisContextWrapper){
        this.userProxy = userProxy;
        this.cookieHelper = cookieHelper;
        this.redisContextWrapper = redisContextWrapper;
    }

    @Override
    public AuthResponseVO login(AuthRequestVO request,
                                HttpServletResponse servletResponse,
                                HttpServletRequest serverHttpRequest) throws ValidationException {
        AuthResponseVO authResponse = new AuthResponseVO();;
        try{
            ValidationUtil.validateRequest(request);
            UserVO userVO = userProxy.getUserByEmail(request.getEmailId());
            if(ValidationUtil.isValidUser(request.getPassword(), userVO.getPassword())){
                authResponse.setValidUser(true);
                userVO.setPassword(null);

                SupplierContext supplierContext = new SupplierContext();
                supplierContext.setUserVO(userVO);
                redisContextWrapper.setContext("session-id", supplierContext);


                authResponse.setUser(userVO);
                cookieHelper.setCookie(userVO, servletResponse, serverHttpRequest);
            }
        }
        catch (ValidationException e){
            //LOGGING
            throw e;
        }
        return authResponse;
    }
}
