package com.root.authservice.vo;

import com.root.commondependencies.vo.UserVO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class AuthResponseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean validUser;
    private UserVO user;

}
