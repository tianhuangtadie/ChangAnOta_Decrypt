package com.changan;

import java.io.ByteArrayOutputStream;

public class StringEncode {
    private static final String NULL_STRING = "null";

    public static String hexEncode(byte[] data) {
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : data) {
            String hexString = Integer.toHexString(b & -1);
            if (hexString.length() < 2) {
                stringBuffer.append('0');
            }
            stringBuffer.append(hexString);
        }
        return stringBuffer.toString().toUpperCase();
    }

    public static byte[] hexDecode(String data) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = 0;
        while (i < data.length()) {
            int i2 = i + 2;
            byteArrayOutputStream.write(Integer.parseInt(data.substring(i, i2), 16) & 255);
            i = i2;
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static boolean isValid(String data) {
        return (data == null || "".equals(data) || NULL_STRING.equals(data)) ? false : true;
    }
}