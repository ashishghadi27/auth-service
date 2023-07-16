package com.root.authservice.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthRequestVO {

    private String emailId;
    private String password;

}
