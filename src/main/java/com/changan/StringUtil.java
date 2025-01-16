package com.changan;

import java.io.UnsupportedEncodingException;

public class StringUtil {
    public static byte[] getBytes(String data, String encoding) throws UnsupportedEncodingException {
        return data.getBytes(encoding);
    }

    public static String transferBytesHexStr(byte[] bytesIn) {
        if (bytesIn != null) {
            StringBuilder strBuilder = new StringBuilder();
            for (byte oneByte : bytesIn) {
                String hex = Integer.toHexString(oneByte & 255);
                if (hex.length() == 1) {
                    hex = "0" + hex;
                }
                strBuilder.append(hex);
            }
            return strBuilder.toString().toUpperCase();
        }
        return null;
    }

    public static byte[] transferHexToBytes(String strIn) {
        if (strIn != null) {
            try {
                if (strIn.length() % 2 == 0) {
                    int len = strIn.length() / 2;
                    byte[] bytesAfterTransfer = new byte[len];
                    for (int i = 0; i < len; i++) {
                        int high = Integer.parseInt(strIn.substring(i * 2, (i * 2) + 1), 16);
                        int low = Integer.parseInt(strIn.substring((i * 2) + 1, (i * 2) + 2), 16);
                        bytesAfterTransfer[i] = (byte) ((high * 16) + low);
                    }
                    return bytesAfterTransfer;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}