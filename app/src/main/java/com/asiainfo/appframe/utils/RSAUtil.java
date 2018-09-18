package com.asiainfo.appframe.utils;

import com.asiainfo.appframe.utils.Des3Util.Base64;
import com.google.zxing.common.StringUtils;


public class RSAUtil {
//	public static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCUtMAfg3Nhh0fS2Nzwa66Y W3p5hH3yOH9iHlRsBjYRTXbzzMnWy/Fb6ztv2ThIS2IRXi1PBzzWnrF/JQ4a LNdVOMDRX0OUBlfoEQH887+Q2FeXBEZXwfRtQQrnwUj+2HDri0hMzKVR1r65 kH/mytRS0R7uuY3mihgkVF/o8BWMFwIDAQAB";
////	public static String publicKey= "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJcltNtoJ5hxn4IvMOMruaeJIWbM2FpB6R0wglLF0fnsAULoCIaH72mOaUsIVK6szuEvcy9gMsZqoI1UWDgZqclo/AaUXLoMc+h2s3d0Xzuj5U/nhPT6P2CXcICL/TeLpmQANehqHfH6TqwmtLSC4j3E/auOqwIp53ksvrFAaRjQIDAQAB";
////	public static String privateKey="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIlyW022gnmHGfgi8w4yu5p4khZszYWkHpHTCCUsXR+ewBQugIhofvaY5pSwhUrqzO4S9zL2AyxmqgjVRYOBmpyWj8BpRcugxz6Hazd3RfO6PlT+eE9Po/YJdwgIv9N4umZAA16God8fpOrCa0tILiPcT9q46rAinneSy+sUBpGNAgMBAAECgYB4NrT0u3RSpn9oL+k1udmruVjMlE0NXgX+tPyixrKMPWTyQdnZdMvXEtRZqCps5ABpGd1rjRApfYGqSB6MboayrWB/LXqDt2m3xPeIHreGZ7pkqn/aY4KL+uxSC11AF7nsIAaRuwB2gpdgcIh9OEzFjrC/NWYcWJW8j1JoTxzMYQJBAOyGKpYRSIs/7mNFUHysvwh8uo6os+4YYsUUuZstMVU60lwMHjR6ef/WZX3FE7uNIpxVI0Zq8b/1dRNm+BPoExcCQQCUw62zgXetiD7jzYVpAN6EzikLyN43JfatWuyIA/yeXLl4DS5guLwcfkt+j+6WMgoYZiDJDBSR9NcHPdPNUzb7AkAcm9HU2vxSU4ZLHZwxqFdm+Gc8UC2LVAnGWs5kdZ/s9jNxjMmugU32YwFdqBPN8LFKBYZBwzfAhzkFAsRo6/47AkB9wjXTK71qO2U+2nzPWWbCyi00wyfnYvZ3cgfGq+ggT66SaEIujbyoMbPMWAyPd9nE6oEItGenQ12Lx56HUrOFAkBr1J8ej4F1RmdDQKCGpUC/sOdKP4YYFD9J9E24jfy/DRMm/iD8VT+qQW1NTmmnkj8/6XFq51e8kqwMRyLaYMnG";
//	public static String privateKey="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALRJFdkqGH2/bM/Y"
//			+"+Q3LdKkXQfckJC0OjK3TMxBg6kXWW3McTLiQe0sSgiYpmFQIavE8LNF4jlalnlpL"
//			+"FYW+wS5leZVTeoZqy+8GUfDrc0dOVSRcC+Ix/1XcDifQTArUNyNnDpZIjGXPbn0U"
//			+"6AI0fhRpTL4J8w9Njq6r/875m8I5AgMBAAECgYEAqDm+UlquacASp8u9cfrUS+fw"
//			+"3MzzmLuH17dIYdMdJ7eCs+HHUT04Xu/RVAziIB+OsrVE2N1WbFG0dmqzlY5OsQuS"
//			+"6/Jz4GhH6Htqx3KmqqKe4c30SU8oyRAumci1xpFpi+1pxdyLrMnL0nUny4jxRMnM"
//			+"spoTgU8AzD2PjmDcA4ECQQDl6YE8QyvXvV03KXMhneTg36p0emCCw9esQE3/uIa2"
//			+"pXP9OQPt/NlCj8V12xSNfBIOyqmMa9cf7cH4Bzd2TJrJAkEAyL4GNjhZyFeNmtmp"
//			+"Q8PfUFzL8D3qPextRCNTQ5x7VyRG4jXXUPSOlTKxGnrtQXrxH24dQMgP+c+lYX/J"
//			+"P2gz8QJAb7AwvPKwsLxoNSzopjMLBgcBylXM3pl/A0m12n1sy/uyThhNhWZosMIh"
//			+"HbDQ2i6koxTmqrWUqwkYSoJO+iQaqQJAQbbjNbCww4LV2lFStdpTKm2WXWkHW4og"
//			+"IuQlh8acIeTAR6E0gU3tty3HCFIhhQQIFy/m56QWhsLQ8t/wM4+bIQJBAKcHRxd7"
//			+"4AGzXDUjBo2IaE9vGC5iJc4yrIHtg/U9fQK2Af4MHO7lwGMU8L/VnQbTpuKjNV2y"
//			+"FsfG/5T9qeSl200=";
	
	public static String getEnCodeString(String preString, String publicKey){
		byte[] cipherData;
        String cipher = null;
        byte[] res = null;
		
		if(!StringUtil.isEmpty(preString)){
			System.out.println("--------------公钥加密私钥解密过程-------------------");  
	        //公钥加密过程  
	        
			try {
				cipherData = RSAEncrypt.encrypt(RSAEncrypt.loadPublicKeyByStr(publicKey), preString.getBytes());
				cipher=Base64.encode(cipherData);
				//私钥解密过程  
//				res = RSAEncrypt.decrypt(RSAEncrypt.loadPrivateKeyByStr(privateKey), Base64.decode(cipher)); 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}   
//	        String restr = new String(res);  
//	        System.out.println("原文："+preString);
//	        System.out.println("加密："+cipher);
//	        System.out.println("解密："+restr);
//	        System.out.println();
        
		}
		return cipher;
		
//      System.out.println("--------------私钥加密公钥解密过程-------------------");  
//      plainText="ihep_私钥加密公钥解密";  
//      //私钥加密过程  
//      cipherData=RSAEncrypt.encrypt(RSAEncrypt.loadPrivateKeyByStr(privateKey),plainText.getBytes());  
//      cipher=Base64.encode(cipherData);  
//      //公钥解密过程  
//      res=RSAEncrypt.decrypt(RSAEncrypt.loadPublicKeyByStr(publicKey), Base64.decode(cipher));  
//      restr=new String(res);  
//      System.out.println("原文："+plainText);  
//      System.out.println("加密："+cipher);  
//      System.out.println("解密："+restr);  
//      System.out.println();  
//        
//      System.out.println("---------------私钥签名过程------------------");  
//      String content="ihep_这是用于签名的原始数据";  
//      String signstr=RSASignature.sign(content,privateKey);  
//      System.out.println("签名原串："+content);  
//      System.out.println("签名串："+signstr);  
//      System.out.println();  
//        
//      System.out.println("---------------公钥校验签名------------------");  
//      System.out.println("签名原串："+content);  
//      System.out.println("签名串："+signstr);  
//        
//      System.out.println("验签结果："+RSASignature.doCheck(content, signstr, publicKey));  
//      System.out.println();
		
	}
}
