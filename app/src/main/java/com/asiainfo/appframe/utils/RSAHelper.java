package com.asiainfo.appframe.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import com.rt.BASE64Decoder;
import com.rt.BASE64Encoder;

public class RSAHelper {
	private static final String RSA = "RSA"; // RSA/ECB/PKCS1Padding

    /**
     * 读取密钥信息
     * 
     * @param in
     * @return
     * @throws IOException
     */
    public static String readKey(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String readLine = null;
        StringBuilder sb = new StringBuilder();
        while ((readLine = br.readLine()) != null) {
            if (readLine.charAt(0) == '-') {
                continue;
            } else {
                sb.append(readLine);
                sb.append("");
            }
        }
        return sb.toString();
    }

    /**
     * 从文件中输入流中加载公钥 ,android 可以将公钥放在assets目录
     * 
     * @param in
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(InputStream in) throws Exception {
        try {
            return getPublicKey(readKey(in));
        } catch (IOException e) {
            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥输入流为空");
        }
    }

    /**
     * 读取公钥信息
     * 
     * @param key
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String key) throws Exception {
        try {
            byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(key);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }

    }

    /**
     * 从文件中输入流中加载私钥 ,android 可以将私钥放在assets目录
     * 
     * @param in
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(InputStream in) throws Exception {
        try {
            return getPrivateKey(readKey(in));
        } catch (IOException e) {
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥输入流为空");
        }
    }

    /**
     * 读取私钥信息
     * 
     * @param key
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        try {
            byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(key);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            return privateKey;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }

    }

    /**
     * 读取Key的信息
     * 
     * @param key
     * @return
     * @throws Exception
     */
    public static String getKeyString(Key key) throws Exception {
        byte[] keyBytes = key.getEncoded();
        String s = (new BASE64Encoder()).encode(keyBytes);
        return s;
    }

    /**
     * RSA加密
     * 
     * @param plainText
     *            明文
     * @param key
     *            密钥
     * @return
     * @throws Exception
     */
    public static byte[] encryptData(byte[] plainText, PublicKey key)
            throws Exception {
        // 加解密类
        Cipher cipher = Cipher.getInstance(RSA);
        // 加密
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] enBytes = cipher.doFinal(plainText);
        return enBytes;
    }

    /**
     * RSA 解密
     * 
     * @param enBytes
     *            加密数据
     * @param key
     *            私钥
     * @return
     * @throws Exception
     */
    public static byte[] decryptData(byte[] enBytes, PrivateKey key)
            throws Exception {
        // 加解密类
        Cipher cipher = Cipher.getInstance(RSA);
        // 解密
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] deBytes = cipher.doFinal(enBytes);
        return deBytes;
    }

    public static void main(String[] args) throws Exception {

        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(RSA);
        // 密钥位数
        keyPairGen.initialize(1024);
        // 密钥对
        KeyPair keyPair = keyPairGen.generateKeyPair();

        // 公钥
        PublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        // 私钥
        PrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        String publicKeyString = getKeyString(publicKey);
        System.out.println("public:\n" + publicKeyString);

        String privateKeyString = getKeyString(privateKey);
        System.out.println("private:\n" + privateKeyString);

        String text = "这里是测试RSA算法的明文";
        byte[] plainText = text.getBytes();
        //将公钥字符串还原公钥信息
        publicKey = getPublicKey(publicKeyString);
        byte[] enBytes = encryptData(plainText, publicKey);

        //将私钥字符串还原私钥信息
        privateKey = getPrivateKey(privateKeyString);
        byte[] deBytes = decryptData(enBytes, privateKey);
        publicKeyString = getKeyString(publicKey);

        System.out.println("公钥:\n" + publicKeyString);

        privateKeyString = getKeyString(privateKey);
        System.out.println("私钥:\n" + privateKeyString);

        String s = new String(deBytes);
        System.out.println(s);

    }
}
