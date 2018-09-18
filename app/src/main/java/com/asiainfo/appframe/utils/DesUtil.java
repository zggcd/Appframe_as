package com.asiainfo.appframe.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by count on 17/3/22.
 */
public class DesUtil
{
    // 初始化向量值
    private static byte[] iv = {1, 2, 3, 4, 5, 6, 7, 8};

    // 编码格式
    private static String charset = "UTF-8";

    /**
     * @return DES算法密钥
     */
    public static byte[] generateKey()
    {
        try
        {

            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();

            // 生成一个DES算法的KeyGenerator对象
            KeyGenerator kg = KeyGenerator.getInstance("DES");
            kg.init(sr);

            // 生成密钥
            SecretKey secretKey = kg.generateKey();

            // 获取密钥数据
            byte[] key = secretKey.getEncoded();

            return key;
        } catch (NoSuchAlgorithmException e)
        {
            System.err.println("DES算法，生成密钥出错!");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * DES(ECB)加密
     *
     * @param data 加密数据
     * @param key  密钥
     * @return 返回加密后的数据
     */
    public static byte[] ECBEncrypt(String data, String key)
    {

        try
        {

            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();

            // 从原始密钥数据创建DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key.getBytes(charset));

            // 创建一个密匙工厂，然后用它把DESKeySpec转换成 SecretKey 对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(dks);

            // using DES in ECB mode
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, sr);

            // 执行加密操作
            byte encryptedData[] = cipher.doFinal(data.getBytes(charset));

            return encryptedData;
        } catch (Exception e)
        {
            System.err.println("DES算法，加密数据出错!");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解密函数
     *
     * @param data 解密数据
     * @param key  密钥
     * @return 返回解密后的数据
     */
    public static String ECBDecrypt(byte[] data, String key)
    {
        try
        {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();

            // byte rawKeyData[] = /* 用某种方法获取原始密匙数据 */;

            // 从原始密匙数据创建一个DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key.getBytes(charset));

            // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(dks);

            // using DES in ECB mode
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, secretKey, sr);

            // 正式执行解密操作
            byte decryptedData[] = cipher.doFinal(data);

            return new String(decryptedData, charset);
        } catch (Exception e)
        {
            System.err.println("DES算法，解密出错。");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 加密函数
     *
     * @param data 加密数据
     * @param key  密钥
     * @return 返回加密后的数据
     */
    public static byte[] CBCEncrypt(String data, String key)
    {

        try
        {
            // 从原始密钥数据创建DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key.getBytes(charset));

            // 创建一个密匙工厂，然后用它把DESKeySpec转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(dks);

            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            // 若采用NoPadding模式，data长度必须是8的倍数
            // Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");

            // 用密匙初始化Cipher对象
            IvParameterSpec param = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, param);

            // 执行加密操作
            byte encryptedData[] = cipher.doFinal(data.getBytes(charset));

            return encryptedData;
        } catch (Exception e)
        {
            System.err.println("DES算法，加密数据出错!");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解密函数
     *
     * @param data 解密数据
     * @param key  密钥
     * @return 返回解密后的数据
     */
    public static String CBCDecrypt(byte[] data, String key)
    {
        try
        {
            // 从原始密匙数据创建一个DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key.getBytes(charset));

            // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(dks);

            // using DES in CBC mode
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

            // 若采用NoPadding模式，data长度必须是8的倍数
            // Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");

            // 用密匙初始化Cipher对象
            IvParameterSpec param = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, param);

            // 正式执行解密操作
            byte decryptedData[] = cipher.doFinal(data);

            return new String(decryptedData, charset);
        } catch (Exception e)
        {
            System.err.println("DES算法，解密出错。");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * DEC(ECB) 加密 结果十六进制输出
     * @param data
     * @param key
     * @return
     */
    public static String ECBEncryptHex(String data, String key)
    {
        return byteArr2HexStr(ECBEncrypt(data, key));
    }

    /**
     * DEC(ECB) 解密 结果十六进制输出
     * @param data
     * @param key
     * @return
     */
    public static String ECBDecryptHex(String data, String key)
    {
        try
        {
            return ECBDecrypt(hexStr2ByteArr(data), key);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * DEC(CBC) 加密 结果十六进制输出
     * @param data
     * @param key
     * @return
     */
    public static String CBCEncryptHex(String data, String key)
    {
        return byteArr2HexStr(CBCEncrypt(data, key));
    }



    /**
     * DEC(CBC) 解密 结果十六进制输出
     * @param data
     * @param key
     * @return
     */
    public static String CBCDecryptHex(String data, String key)
    {
        try
        {
            return CBCDecrypt(hexStr2ByteArr(data), key);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 将指定byte数组以16进制的形式打印到控制台
     *
     * @param buf  byte[]
     * @return String
     */
    public static String byteArr2HexStr(byte[] buf)
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++)
        {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1)
            {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }

        return sb.toString();
    }

    /**
     * 将表示16进制值的字符串转换为byte数组，
     * 和public static String printHexString(byte[] arrB)
     * 互为可逆的转换过程
     * @param strIn 需要转换的字符串
     * @return 转换后的byte数组
     * @throws Exception 本方法不处理任何异常，所有异常全部抛出
     */
    public static byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    public static void main(String[] args)
    {
        try
        {
            String plainText = "123456";
            String key = "22222222";

            System.out.println("DES(CBC) 加密: " + plainText);

            String result = DesUtil.CBCEncryptHex(plainText, key);
            System.out.println(result);

            System.out.println("DES(CBC) 解密: ");
            System.out.println(DesUtil.CBCDecryptHex(result, key));

            System.out.println();

            System.out.println("DES(ECB) 加密: " + plainText);

            String result2 = DesUtil.ECBEncryptHex(plainText, key);
            System.out.println(result);

            System.out.println("DES(ECB) 解密: ");
            System.out.println(DesUtil.ECBDecryptHex(result2, key));

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
