package com.sciento.wumu.gpscontroller.Utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by wumu on 17-7-31.
 */

public class Md5Util {

        public static String md5(String string) {
            byte[] hash;
            try {
                hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Huh, MD5 should be supported?", e);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Huh, UTF-8 should be supported?", e);
            }

            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10)
                    hex.append("0");
                hex.append(Integer.toHexString(b & 0xFF));
            }
            return hex.toString();
        }

        public static String encrypt(String data) {
            if (data == null)
                data = "";
            byte[] btRet = null;
            try {
                btRet = _encrypt(data.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (btRet == null)
                return null;
            return btRet.toString();
        }


        private static byte[] _encrypt(byte[] btData) {
            try {
                // 获得MD5摘要算法的 MessageDigest 对象
                MessageDigest mdInst = MessageDigest.getInstance("MD5");
                // 使用指定的字节更新摘要
                mdInst.update(btData);
                // 获得密文
                return mdInst.digest();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    }
