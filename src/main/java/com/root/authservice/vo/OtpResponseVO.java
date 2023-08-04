package com.root.authservice.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OtpResponseVO {

    private String responseCode;
    private String responseMsg;

}
