package com.changan;

import com.dianebao.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/* loaded from: C:\Users\Administrator\Desktop\boot-ext_classes.cdex.dex */
public class DigestInfoProvider {
    private final String TAG = "Leosin-WhiteBox";
    private byte[] dexClassDigest;
    private String dexClassFilePath;
    private byte[] mainfestFileDigest;
    private String mainfestFilePath;
    private String signAlg;

    public DigestInfoProvider(InputStream sfileInputStream, String mainfestFilePath) {
        init(sfileInputStream, mainfestFilePath, null, null);
    }

    public DigestInfoProvider(InputStream sfFileInputSteam, String mainfestFilePath, InputStream mainestFileInputSream, String dexClassFilePath) {
        init(sfFileInputSteam, mainfestFilePath, mainestFileInputSream, dexClassFilePath);
    }

    private void init(InputStream sfFileInputSteam, String mainfestFilePath, InputStream mainestFileInputSream, String dexClassFilePath) {
        if (sfFileInputSteam != null) {
            getMainestFileDigest(sfFileInputSteam);
            this.mainfestFilePath = mainfestFilePath;
        }
        if (mainestFileInputSream != null) {
            getDexClassFileDigest(mainestFileInputSream, dexClassFilePath);
            this.dexClassFilePath = dexClassFilePath;
        }
    }

    private void getMainestFileDigest(InputStream sfFileInputSream) {
        try {
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(sfFileInputSream));
            while (true) {
                String line = bufReader.readLine();
                if (line == null) {
                    break;
                }
                String[] tmp = line.split(":");
                if (tmp[0].endsWith("Manifest")) {
                    this.mainfestFileDigest = Base64.decode(tmp[1].getBytes(), 2);
                    break;
                }
            }
            d("Leosin-WhiteBox", "this.mainfestFileDigest" + StringUtil.transferBytesHexStr(this.mainfestFileDigest));
            bufReader.close();
            sfFileInputSream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getDexClassFileDigest(InputStream mainfestFileInputStream, String targetFilePath) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(mainfestFileInputStream));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                String[] tmp = line.split(":");
                if (tmp.length == 2) {
                    if (tmp[0].equals("Name") && targetFilePath.endsWith(tmp[1].trim())) {
                        String[] keyValue = bufferedReader.readLine().split(":");
                        this.dexClassDigest = Base64.decode(keyValue[1].getBytes(), 2);
                        this.signAlg = byteAarrayToStr(keyValue[0].getBytes());
                        d("Leosin-WhiteBox", "this.signAlg : " + this.signAlg);
                        d("Leosin-WhiteBox", "this.dexClassDigest  :  " + StringUtil.transferBytesHexStr(this.dexClassDigest));
                        break;
                    }
                }
            }
            mainfestFileInputStream.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String byteAarrayToStr(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        String str = new String(byteArray);
        return str;
    }

    public byte[] getMainfestFileDigest() {
        d("Leosin-WhiteBox", "mainfestFileDigest  :  " + StringUtil.transferBytesHexStr(this.mainfestFileDigest));
        return this.mainfestFileDigest;
    }

    public byte[] getDexClassDigest() {
        d("Leosin-WhiteBox", "dexClassDigest  :  " + StringUtil.transferBytesHexStr(this.dexClassDigest));
        return this.dexClassDigest;
    }

    public String getMainfestFilePath() {
        d("Leosin-WhiteBox", "mainfestFilePath  :  " + this.mainfestFilePath);
        return this.mainfestFilePath;
    }

    public String getDexClassFilePath() {
        d("Leosin-WhiteBox", "dexClassFilePath  :  " + this.dexClassFilePath);
        return this.dexClassFilePath;
    }

    public String getDigestSignAlg() {
        d("Leosin-WhiteBox", "return getDigestSignAlg : " + this.signAlg);
        return this.signAlg;
    }

    private void d(String s, String s1) {
        System.out.println(s + "  " + s1);
    }
}