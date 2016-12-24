/*
 * EncryptOutputStream.java
 * classes : com.spider.subscriber.io.EncryptOutputStream
 * @author liujun
 * V 1.0.0
 * Create at 2015-1-15 上午11:51:02
 * Copyright (c) 2015年  Spider. All Rights Reserved.
 */
package net.frontdo.funnylearn.io;

import java.io.File;
import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;

/**
 * 
* @ClassName: EncryptOutputStream 
* @Description: 加密的输出流
* @author LiuJun
* @date 2015-1-15 上午11:51:02
*
 */
public class EncryptOutputStream extends CipherOutputStream {

	public EncryptOutputStream(File file) throws Exception {
		//获取压缩的输出流和解密秘钥
		this(IOUtil.generateZIPOutputStream(file), IOUtil
				.initCipher(Cipher.ENCRYPT_MODE));
	}

	private EncryptOutputStream(OutputStream os, Cipher c) {
		super(os, c);
	}

	private EncryptOutputStream(OutputStream os) {
		super(os);
	}

}
