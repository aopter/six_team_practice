package net.onest.timestoryprj.util;

public class PhoneUtil {

    public static boolean isMobileNo(String mobiles){
        String telRegex = "[1][3456789]\\d{9}"; // "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if ("".equals(mobiles.trim()) || null == mobiles){
            return  false;
        } else {
            return  mobiles.matches(telRegex);
        }
    }
}
