package com.changan;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.crypto.symmetric.AES;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;

public class Decrypt {

    private static final String TAG = "ttm";

    public static void main(String[] args) throws Exception {
        String filePath = "E:\\Share\\S311MCA-7020\\IncallUpgrade\\HuOs\\S311_MCA_WT1.0.1_7020_full.zip";// update.zip文件路径
        String rawFilePath = newRawFile(filePath);
        fileCheck(filePath);
        check(filePath, rawFilePath);
    }


    public static boolean check(String s, String s1) throws Exception {
        long v4;
        try {
            FileOutputStream fileOutputStream0 = new FileOutputStream(s1);
            FileInputStream fileInputStream0 = new FileInputStream(s);
            byte[] arr_b = new byte[5];
            if (fileInputStream0.read(arr_b, 0, 5) < 5) {
                fileOutputStream0.close();
                fileInputStream0.close();
                return false;
            }

            int v1 = bytes2Int_LE(arr_b);
            d("Utils", "otalen = " + v1);
            long v2 = (long) v1;
            int v3 = v1 < 0 || arr_b[4] == 2 ? 1 : 0;
            if (v3 == 0) {
                d("Utils", "len is int :" + v1);
                crypt(fileInputStream0, fileOutputStream0, 2, v2);
                v4 = v2;
            } else {
                d("Utils", "len is long need getNewLongLen:" + v1);
                byte[] arr_b1 = new byte[8];
                if (((long) fileInputStream0.read(arr_b1, 0, 8)) < 8L) {
                    fileOutputStream0.close();
                    fileInputStream0.close();
                    return false;
                }
                v4 = bytesToLong(arr_b1);
                d("Utils", "otalen2 = " + v4);
                crypt(fileInputStream0, fileOutputStream0, 2, v4);
            }
            return true;
        } catch (IOException iOException0) {
            iOException0.printStackTrace();
            d("Utils", "check file to: " + s + " failed, info: " + iOException0.getLocalizedMessage());
        } catch (Exception exception0) {
            exception0.printStackTrace();
        }
        return false;
    }


    public static void crypt(InputStream inputStream0, OutputStream outputStream0, int v, long v1) throws Exception {
        int progress;
        int v2 = 1;
        SecretKeySpec secretKeySpec0 = new SecretKeySpec(getkey(), "AES");
        Cipher cipher0 = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher0.init(v, secretKeySpec0, new IvParameterSpec(new byte[]{1, 0, 0, 0, 0, 0, 6, 0, 0, 0, 3, 0, 0, 4, 0, 0}));
        byte[] arr_b = new byte[0x2800];
        byte[] arr_b1 = new byte[0x2810];
        long v3 = 0L;
        int v4 = 0;
        int v5 = 0x2800;
        while (v2 != 0) {
            if (v3 > v1 - 0x2800L) {
                v5 = (int) (v1 - v3);
            }

            if (v5 <= 0) {
                d("test", "rlen 值小于0 len:" + v1 + "  readlen:" + v3);
            }

            int v6 = inputStream0.read(arr_b, 0, v5);
            if (v6 == 0x2800) {
                outputStream0.write(arr_b1, 0, cipher0.update(arr_b, 0, 0x2800, arr_b1));
                v3 += 0x2800L;
                progress = (int) (v3 / (v1 / 60L));
                if (progress < 0) {
                    d("test", "readlen = " + v3);
                }
                v2 = v2;
                v4 = 0x2800;
                continue;
            }
            v4 = v6;
            v2 = 0;
        }
        outputStream0.write((v4 <= 0 ? cipher0.doFinal() : cipher0.doFinal(arr_b, 0, v4)));
        outputStream0.flush();
    }


    public static boolean fileCheck(String srcfile) {
        boolean z;
        long length = new File(srcfile).length();
        try {
            FileInputStream fileInputStream = new FileInputStream(srcfile);
            byte[] bArr = new byte[5];
            if (fileInputStream.read(bArr, 0, 5) < 5) {
                fileInputStream.close();
                return false;
            }
            byte b = bArr[4];
            if (b != 1 && b != 2) {
                d(TAG, "file::not hash1:" + ((int) b));
                fileInputStream.close();
                return false;
            }
            int bytes2Int_LE = bytes2Int_LE(bArr);
            long j = bytes2Int_LE;
            if (bytes2Int_LE >= 0 && b != 2) {
                z = false;
                if (!z) {
                    d(TAG, "file::not Int Head");
                    byte[] bArr2 = new byte[8];
                    if (fileInputStream.read(bArr2, 0, 8) < 8) {
                        fileInputStream.close();
                        return false;
                    }
                    j = bytesToLong(bArr2);
                } else {
                    d(TAG, "file::is Int Head");
                }
                d(TAG, "file::not aes16");
                if (j % 16 == 0) {
                    d(TAG, "file::not aes16");
                    fileInputStream.close();
                    return false;
                }
                long j2 = 5 + j + 256;
                if (z) {
                    d(TAG, "file::add Long Head");
                    j2 += 8;
                }
                if (length != j2) {
                    d(TAG, "file::wrong len:  filesize>>" + length + "  combSize>>" + j);
                    fileInputStream.close();
                    return false;
                }
                fileInputStream.close();
                return true;
            }
            z = true;
            if (!z) {
            }
            d(TAG, "file::not aes16");
            if (j % 16 == 0) {
            }
        } catch (IOException e) {
            e.printStackTrace();
            d(TAG, "check file to: " + srcfile + " failed, info: " + e.getLocalizedMessage());
            return false;
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
        return true;
    }

    public static byte[] toByteArray(String hexString) {
        if (hexString.isEmpty())
            throw new IllegalArgumentException("this hexString must not be empty");
        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() / 2];
        int k = 0;
        for (int i = 0; i < byteArray.length; i++) {// 因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
            byteArray[i] = (byte) (high << 4 | low);
            k += 2;
        }
        return byteArray;
    }

    private static synchronized byte[] getkey() {
        byte[] bArr;
        bArr = new byte[16];
        byte[] bArr2 = new byte[32];
        //todo  不同车型，key不一样
        byte[] decrypt = toByteArray("");
        for (int i = 0; i < 16; i++) {
            bArr[i] = decrypt[i];
        }
        return bArr;
    }

    public static void decrypt(AES aes, String filePath) throws Exception {
        if (!FileUtil.exist(filePath)) {
            System.out.println("文件不存在!");
            return;
        }
        InputStream in = FileUtil.getInputStream(filePath);
        String rawFilePath = newRawFile(filePath);
        OutputStream out = FileUtil.getOutputStream(rawFilePath);
        byte[] bArr2 = new byte[5];
        byte[] bArr3 = new byte[8];
        if (in.read(bArr3, 0, 8) < 8) {
        }
    }

    public static long bytesToLong(byte[] arr_b) {
        long v = 0L;
        for (int v1 = 0; v1 < 8; ++v1) {
            v = v << 8 | ((long) (arr_b[v1] & 0xFF));
        }

        return v;
    }

    private static int bytes2Int_LE(byte[] arr_b) {
        return arr_b.length >= 4 ? (arr_b[3] & 0xFF) << 24 | (arr_b[0] & 0xFF | (arr_b[1] & 0xFF) << 8 | (arr_b[2] & 0xFF) << 16) : -1;
    }


    private static void d(String s, String s1) {
        System.out.println(s + "  " + s1);
    }

    private static String newRawFile(String filePath) {
        String parentPath = FileUtil.getParent(filePath, 1);
        String fileName = FileNameUtil.mainName(filePath);
        String rawFilePath = parentPath + "/" + fileName + "_raw." + FileUtil.getSuffix(filePath);
        File rawFile = FileUtil.file(rawFilePath);
        if (rawFile.exists()) {
            FileUtil.del(rawFile);
        }
        FileUtil.touch(rawFile);
        return rawFilePath;
    }
}