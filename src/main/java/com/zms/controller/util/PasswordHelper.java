package com.zms.controller.util;

import com.zms.vo.User;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @Author:zms
 * @Description:注册用户时的密码加密类
 * @Date:Create On 2018/4/27 14:40
 */
public class PasswordHelper {
    private static final int hashIterations = 2;
    private static RandomNumberGenerator randomNumberGenerator =
            new SecureRandomNumberGenerator();
    private static String algorithmName = "md5";

    public static void encryptPassword(User user) {
        user.setSalt(randomNumberGenerator.nextBytes().toHex());
        String newPassword = new SimpleHash(
                algorithmName,
                user.getPassword(),
                ByteSource.Util.bytes(user.getCredentialsSalt()),
                hashIterations).toHex();
        user.setPassword(newPassword);
    }
}
