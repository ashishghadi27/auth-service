package com.root.authservice.helpers;

import com.root.authservice.config.ConsulConfig;
import com.root.commondependencies.exception.ValidationException;
import com.root.commondependencies.vo.UserVO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.root.redis.constants.ExceptionConstants.INVALID_REQUEST;

@Component
public class CookieHelper {
    private final JWTHelper jwtHelper;


    private final ConsulConfig config;

    private HttpServletRequest httpServletRequest;

    private HttpServletResponse httpServletResponse;

    @Autowired
    public CookieHelper(JWTHelper jwtHelper,
                        ConsulConfig config,
                        HttpServletRequest httpServletRequest,
                        HttpServletResponse httpServletResponse) {
        this.jwtHelper = jwtHelper;
        this.config = config;
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
    }

    public void setCookie(UserVO userVO) throws ValidationException {

        int cookieTimeout = config.getConfigValueByKey("COOKIE_TIMEOUT", 1080);
        Cookie sessionCookie = null;
        Cookie[] cookies = httpServletRequest.getCookies();
        for(Cookie cookie : cookies){
            if("session-id".equals(cookie.getName())){
                sessionCookie = cookie;
            }
        }

        if(sessionCookie == null){
            throw new ValidationException.Builder().errorMessage(INVALID_REQUEST).build();
        }

        httpServletResponse.addCookie(getSessionCookie(sessionCookie, cookieTimeout));
        httpServletResponse.addCookie(getJwtCookie(userVO, cookieTimeout));
    }

    public void setCookie() throws ValidationException {

        int cookieTimeout = config.getConfigValueByKey("COOKIE_TIMEOUT", 1080);
        Cookie sessionCookie = null;
        Cookie[] cookies = httpServletRequest.getCookies();
        for(Cookie cookie : cookies){
            if("session-id".equals(cookie.getName())){
                sessionCookie = cookie;
            }
        }

        if(sessionCookie == null){
            throw new ValidationException.Builder().errorMessage(INVALID_REQUEST).build();
        }

        httpServletResponse.addCookie(getSessionCookie(sessionCookie, cookieTimeout));
    }

    private Cookie getSessionCookie(Cookie sessionCookie, int cookieTimeout){
        sessionCookie.setMaxAge(cookieTimeout);
        //sessionCookie.setSecure(true);
        sessionCookie.setHttpOnly(true);
        sessionCookie.setDomain("localhost");
        sessionCookie.setPath("/");
        return sessionCookie;
    }
    private Cookie getJwtCookie(UserVO userVO, int cookieTimeout){
        Cookie jwtTokenCookie = new Cookie("auth", jwtHelper.getJwtToken(userVO));
        jwtTokenCookie.setMaxAge(cookieTimeout);
        //jwtTokenCookie.setSecure(true);
        jwtTokenCookie.setHttpOnly(true);
        jwtTokenCookie.setDomain("localhost");
        jwtTokenCookie.setPath("/");
        return jwtTokenCookie;
    }
}
