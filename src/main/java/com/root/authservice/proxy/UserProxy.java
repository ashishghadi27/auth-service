package com.root.authservice.proxy;

import com.root.authservice.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "db-service/db")
public interface UserProxy {

    @GetMapping("/user/getUserByEmail")
    UserVO getUserByEmail(@RequestParam("email") String email);

}
