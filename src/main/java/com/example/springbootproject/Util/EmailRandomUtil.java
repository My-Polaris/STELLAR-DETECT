package com.example.springbootproject.Util;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

public class EmailRandomUtil {
    private static final String SYMBOLS = "0123456789";
    private static final Random RANDOM = new SecureRandom();

    /**
     * 生成6位随机数字
     * @return 返回6位数字验证码
     */
    public static String generateVerCode() {
        char[] nonceChars = new char[6];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }
    /**
     *计算两个日期的分钟差
     */
    public static int getMinute(Date fromDate, Date toDate) {
        return (int) (toDate.getTime() - fromDate.getTime()) / (60 * 1000);
    }
}
