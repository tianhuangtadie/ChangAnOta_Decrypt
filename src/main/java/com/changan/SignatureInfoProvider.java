package com.changan;

import com.dianebao.Base64;
import sun.security.pkcs.PKCS7;
import sun.security.pkcs.SignerInfo;
import sun.security.util.DerInputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;

/* loaded from: C:\Users\Administrator\Desktop\boot-ext_classes.cdex.dex */
public class SignatureInfoProvider {
    private final String TAG = "Leosin-WhiteBox";
    private String apkCodePath;
    private byte[] mPubkeyContent;
    private String sfFilePath;
    private String signAlg;
    private byte[] signatureContent;
    private int signatureLen;

    public SignatureInfoProvider(InputStream certFileInputStream) {
        init(certFileInputStream, null, null);
    }

    public SignatureInfoProvider(InputStream certFileInputStream, String sfFilePath) {
        init(certFileInputStream, sfFilePath, null);
    }

    public SignatureInfoProvider(InputStream certFileInputStream, String sfFilePath, String codeApkPath) {
        init(certFileInputStream, sfFilePath, codeApkPath);
    }

    private void init(InputStream certFileInputStream, String sfFileName, String apkCodePath) {
        if (certFileInputStream != null) {
            collectionSignNatureAndPubkey(certFileInputStream);
        }
        if (sfFileName != null) {
            this.sfFilePath = sfFileName;
        }
        if (apkCodePath != null) {
            this.apkCodePath = apkCodePath;
        }
    }

    private void collectionSignNatureAndPubkey(InputStream certFileInputStream) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            while (true) {
                int len = certFileInputStream.read(buf);
                if (len != -1) {
                    bos.write(buf, 0, len);
                } else {
                    byte[] array = bos.toByteArray();
                    PKCS7 pkcs7 = new PKCS7(new DerInputStream(array));
                    getPubkeyContent(pkcs7);
                    getSignatureContent(pkcs7);
                    certFileInputStream.close();
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getPubkeyContent(PKCS7 pkcs7) {
        Certificate[] certificate = pkcs7.getCertificates();
        StringBuilder strBuilder = new StringBuilder();
        int i = 0;
        String content = Base64.encodeToString(certificate[0].getPublicKey().getEncoded(), 2);
        if (content != null) {
            strBuilder.append("-----BEGIN PUBLIC KEY-----\n");
            while (true) {
                if (i >= content.length()) {
                    break;
                }
                if ((i + 1) * 64 > content.length()) {
                    strBuilder.append(content.substring(i * 64, content.length()) + "\n");
                    break;
                }
                strBuilder.append(content.substring(i * 64, (i + 1) * 64) + "\n");
                i++;
            }
            strBuilder.append("-----END PUBLIC KEY-----\n");
            this.mPubkeyContent = strBuilder.toString().getBytes();
        }
    }

    private void getSignatureContent(PKCS7 pkcs7) {
        SignerInfo[] signerInfos = pkcs7.getSignerInfos();
        if (signerInfos != null) {
            this.signatureContent = signerInfos[0].getEncryptedDigest();
            this.signatureLen = this.signatureContent.length;
        }
        String digestAlg = signerInfos[0].getDigestAlgorithmId().toString();
        if (digestAlg.equals("SHA1")) {
            digestAlg = "SHA-1";
        }
        this.signAlg = digestAlg + "with" + signerInfos[0].getDigestEncryptionAlgorithmId();
        StringBuilder sb = new StringBuilder();
        sb.append("this.signAlg : ");
        sb.append(this.signAlg);
        d("Leosin-WhiteBox", sb.toString());
        d("Leosin-WhiteBox", "this.signatureLen : " + this.signatureLen);
        d("Leosin-WhiteBox", "signerInfos.length = " + signerInfos.length);
        for (int i = 0; i < signerInfos.length; i++) {
            d("Leosin-WhiteBox", "NUM " + i + " : digestAlg = " + signerInfos[i].getDigestAlgorithmId().toString());
        }
    }

    public byte[] getPubkey() {
        return this.mPubkeyContent;
    }

    public String getSignAlg() {
        d("Leosin-WhiteBox", "return getSignAlg : " + this.signAlg);
        return this.signAlg;
    }

    private void d(String s, String s1) {
        System.out.println(s + "  " + s1);
    }

    public byte[] getSignaturContent() {
        return this.signatureContent;
    }

    public int getSignatureLen() {
        return this.signatureLen;
    }

    public String getSfFilePath() {
        return this.sfFilePath;
    }

    public String getApkCodePath() {
        return this.apkCodePath;
    }


}