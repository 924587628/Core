package com.baiyi.core.util;

import java.security.MessageDigest;

public class MD5 {
    /**

     * MD5 ("") = d41d8cd98f00b204e9800998ecf8427e
     * MD5 ("a") = 0cc175b9c0f1b6a831c399e269772661
     * MD5 ("abc") = 900150983cd24fb0d6963f7d28e17f72
     * MD5 ("message digest") = f96b697d7cb7938d525a2f31aaf161d0
     * MD5 ("abcdefghijklmnopqrstuvwxyz") = c3fcd3d76192e4007dfb496cca67e13b
     *
     * @param res
     * @return md5ֵ
     */
    public final static String getMD5(String res) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = res.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            String dd = new String(str);
            return dd;
        } catch (Exception e) {
            return null;
        }
    }
    public final static String getMD5(byte[] datas){
    	  char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                  'a', 'b', 'c', 'd', 'e', 'f'};
    	  try {
              MessageDigest mdTemp = MessageDigest.getInstance("MD5");
              mdTemp.update(datas);
              byte[] md = mdTemp.digest();
              int j = md.length;
              char str[] = new char[j * 2];
              int k = 0;
              for (int i = 0; i < j; i++) {
                  byte byte0 = md[i];
                  str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                  str[k++] = hexDigits[byte0 & 0xf];
              }
              String dd = new String(str);
              return dd;
          } catch (Exception e) {
              return null;
          }
    }
    public static void main(String[] args) {
        //System.out.println(MD5.MD5(""));
        //System.out.println(MD5.MD5("a"));
        //System.out.println(MD5.MD5("xcily"));
//        System.out.println(MD5.getMD5("1xcily1245162110984UCfHrA589Na"));
        
    }
}
