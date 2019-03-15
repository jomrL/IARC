package cn.edu.swufe.utils;

public class EmailFormatUtil {
    public static boolean emailFormat(String address){
        if(address.trim().endsWith("qq.com")){
            return true;
        }else{
            return false;
        }

    }
}
