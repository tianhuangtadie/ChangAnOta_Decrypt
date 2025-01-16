package com.changan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ChangAn {
    private JarFile mApkFile;
    private DigestInfoProvider mDigestPriovider;
    private JarEntry mMainfestFile;
    private JarEntry mRsaFile;
    private JarEntry mSfFile;
    private SignatureInfoProvider mSignProvider;
    private ArrayList<String> mSupportAlgs;
    private ArrayList<String> mSupportDecrytionEncodings;
    private boolean mConStatus = false;
    private String mToken = null;


    public static void main(String[] args) {
        ChangAn changAn = new ChangAn();
        DigestInfoProvider digestInfoProvider = changAn.getDigestInfoProvider();
        System.out.println(digestInfoProvider);
        SignatureInfoProvider signatureProvider = changAn.getSignatureProvider();
        System.out.println(signatureProvider);
        changAn.connection(signatureProvider, digestInfoProvider);
    }


    public String connection(SignatureInfoProvider signProvider, DigestInfoProvider digestProvider) {
        if (signProvider == null || digestProvider == null || signProvider.getApkCodePath() == null || signProvider.getSfFilePath() == null) {
            return null;
        }
        if ((signProvider.getSignaturContent() == null && signProvider.getSignatureLen() == 0) || signProvider.getPubkey() == null) {
            return null;
        }
        String ret = connectionToNativeVerifySign(signProvider.getSignaturContent(), signProvider.getSignatureLen(), signProvider.getPubkey(), signProvider.getSignAlg(), signProvider.getApkCodePath(), signProvider.getSfFilePath());
        if (digestProvider.getMainfestFileDigest() == null || digestProvider.getMainfestFilePath() == null || digestProvider.getDexClassDigest() == null || digestProvider.getDexClassFilePath() == null) {
            return null;
        }
        String ret2 = connectionToNativeVerifyDigest(digestProvider.getMainfestFileDigest(), digestProvider.getMainfestFileDigest().length, digestProvider.getMainfestFilePath(), digestProvider.getDexClassDigest(), digestProvider.getDexClassDigest().length, digestProvider.getDexClassFilePath(), signProvider.getApkCodePath(), signProvider.getSignAlg(), digestProvider.getDigestSignAlg());
        return ret2;
    }

    private String connectionToNativeVerifyDigest(byte[] mainfestFileDigest, int length, String mainfestFilePath, byte[] dexClassDigest, int length1, String dexClassFilePath, String apkCodePath, String signAlg, String digestSignAlg) {
        System.out.println("111");
        return "";
    }

    private String connectionToNativeVerifySign(byte[] signaturContent, int signatureLen, byte[] pubkey, String signAlg, String apkCodePath, String sfFilePath) {
        return "";
    }

    private SignatureInfoProvider getSignatureProvider() {
        if (this.mSignProvider == null) {
            try {
                if (this.mRsaFile == null) {
                    this.mRsaFile = getEntryFile("META-INF", ".RSA");
                }
                if (this.mSfFile == null) {
                    this.mSfFile = getEntryFile("META-INF", ".SF");
                }
                this.mSignProvider = new SignatureInfoProvider(this.mApkFile.getInputStream(this.mRsaFile), this.mSfFile.getName(), "C:\\Users\\Administrator\\Desktop\\Fota.apk");
            } catch (IOException e) {
            }
        }
        return this.mSignProvider;
    }

    private DigestInfoProvider getDigestInfoProvider() {
        if (this.mDigestPriovider == null) {
            try {
                if (this.mSfFile == null) {
                    this.mSfFile = getEntryFile("META-INF", ".SF");
                }
                if (this.mMainfestFile == null) {
                    this.mMainfestFile = getEntryFile("META-INF", "MF");
                }
                this.mDigestPriovider = new DigestInfoProvider(this.mApkFile.getInputStream(this.mSfFile), this.mMainfestFile.getName(), this.mApkFile.getInputStream(this.mMainfestFile), "classes.dex");
            } catch (IOException e) {
            }
        }
        return this.mDigestPriovider;
    }

    private JarEntry getEntryFile(String prefix, String endfix) throws IOException {
        if (this.mApkFile == null) {
            this.mApkFile = new JarFile("C:\\Users\\Administrator\\Desktop\\Fota.apk");
        }
        Enumeration<JarEntry> entries = this.mApkFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String entryName = entry.getName();
            if (entryName.startsWith(prefix) && entryName.endsWith(endfix)) {
                return entry;
            }
        }
        return null;
    }

}
