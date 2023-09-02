package com.root.authservice.proxy;

import com.root.commondependencies.vo.DelRequestVO;
import com.root.commondependencies.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "db-service/db")
public interface UserProxy {

    @GetMapping("/user/getUserByEmail")
    UserVO getUserByEmail(@RequestParam("email") String email);
    @PostMapping("/user/createUser")
    void createUser(@RequestBody UserVO requestVO);
    @DeleteMapping("/user/deleteUser")
    void deleteUser(@RequestBody DelRequestVO requestVO);

}
