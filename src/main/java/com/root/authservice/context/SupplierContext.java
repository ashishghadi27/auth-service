package com.root.authservice.context;

import com.root.commondependencies.vo.UserVO;
import com.root.redis.context.RedisSessionContext;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SupplierContext extends RedisSessionContext {

    private UserVO userVO;
    private String otp;
    private String otpSentTime;

    @Override
    public String getContextIdentifier() {
        return "SUPPLIER";
    }

    @Override
    public Integer sessionExpiryTime() {
        return null;
    }
}
