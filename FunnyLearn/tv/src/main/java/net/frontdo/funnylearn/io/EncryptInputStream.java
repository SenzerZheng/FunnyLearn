/*
 * EncryptInputStream.java
 * classes : com.spider.subscriber.io.EncryptInputStream
 * @author liujun
 * V 1.0.0
 * Create at 2015-1-15 上午11:51:02
 * Copyright (c) 2015年  Spider. All Rights Reserved.
 */
package net.frontdo.funnylearn.io;

import java.io.File;
import java.io.InputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;

/**
 * 
 * @ClassName: EncryptInputStream
 * @Description: 加密的输入流
 * @author LiuJun
 * @date 2015-1-15 上午11:51:02
 * 
 */
public class EncryptInputStream extends CipherInputStream {

	public EncryptInputStream(File file) throws Exception {
		//获取压缩流和加密秘钥
		this(IOUtil.generateZIPInputStream(file), IOUtil
				.initCipher(Cipher.DECRYPT_MODE));
	}

	private EncryptInputStream(InputStream is, Cipher c) {
		super(is, c);
	}

	private EncryptInputStream(InputStream is) {
		super(is);
	}

}
