/*
 * IOUtil.java
 * classes : com.spider.subscriber.io.IOUtil
 * @author liujun
 * V 1.0.0
 * Create at 2015-1-15 上午11:51:02
 * Copyright (c) 2015年  Spider. All Rights Reserved.
 */
package net.frontdo.funnylearn.io;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * @ClassName: IOUtil
 * @Description:加密io工具类
 * @author LiuJun
 * @date 2015-1-15 上午10:53:04
 * 
 */
public class IOUtil {

	// 加密秘钥
	public static final String DES_KEY = "@*fewA+#";

	/**
	 * 初始化 cipher
	 * 
	 * @param mode
	 *            加密模式
	 * @return cipher
	 * @throws Exception
	 */
	static Cipher initCipher(int mode) throws Exception {
		Cipher cipher = Cipher.getInstance("DES");
		String key = DES_KEY;
		// initialize cipher
		cipher.init(mode, generateCipherKey(key));
		return cipher;
	}

	/**
	 * 初始化CipherKey
	 * 
	 * @param keyWord
	 * @return Key
	 */
	private static Key generateCipherKey(String keyWord) {
		Key key = null;
		try {
			byte[] dst = new byte[8];
			// get keyword bytes
			byte[] src = keyWord.getBytes();
			// trim
			System.arraycopy(src, 0, dst, 0, Math.min(dst.length, src.length));
			// return
			key = new SecretKeySpec(dst, "DES");
		} catch (Exception e) {

		}
		return key;
	}

	/**
	 * 获取压缩的输入流
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	static InputStream generateZIPInputStream(File file) throws Exception {
		return new GZIPInputStream(new java.io.FileInputStream(file));
	}

	/**
	 * 获取压缩的输出流
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	static OutputStream generateZIPOutputStream(File file) throws Exception {
		return new GZIPOutputStream(new java.io.FileOutputStream(file));
	}

}
