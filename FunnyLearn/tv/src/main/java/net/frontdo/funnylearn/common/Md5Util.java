/**  

 * @文件名 :Md5Util.java 

 * @包名   :com.spider.subscriber.util 

 * @日期   :2014-12-2 下午4:50:24 

 * @版本   :V1.0.0 
  
 * @版权声明:Copyright (c) 2014年 spider. All rights reserved.

 */
package net.frontdo.funnylearn.common;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * @ClassName: Md5Util
 * @Description: Md5加密工具类
 * @author tufengchao
 * @date 2014-12-2 下午4:50:24
 * 
 */
public class Md5Util {
	/**
	 * 
	 * @ Title: getMd5 @ Description: Md5加密工具类 @ @param s 操作的String @ @return
	 * 加密后的String @ return String @ throws
	 */
	public final static String getMd5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };

		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;

			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}

			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取文件的md5
	 * 
	 * @param file
	 * @return
	 */
	public static String getFileMD5(File file) {
		if (!file.exists() || !file.isFile()) {
			return null;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16).toUpperCase();
	}

	public static String getFileMD5(String filepath) {
		File file = new File(filepath);
		return getFileMD5(file);
	}
}
