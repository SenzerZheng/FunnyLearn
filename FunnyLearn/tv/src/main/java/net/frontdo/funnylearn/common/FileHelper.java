package net.frontdo.funnylearn.common;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * 文件帮助类
 * 
 * @description
 * @author yanxi.li
 * @create 2014-4-24下午6:20:14
 * 
 */
public class FileHelper {
	public static final String TAG = "FileHelper";
	public static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
	public static String SD_ROOT = Environment.getExternalStorageDirectory()
			.getAbsolutePath();
	public static String UIL_IMAGES="/uil-images";							// 应用加载图片URL缓存文件夹
	public static String ROOT = SD_ROOT + "/FunnyLearn"; 					// 根路径
	public static String ROOT_DOWNLOAD_PICTURE = ROOT + "/download";		// 下载路径
	public static String ROOT_AUDIO = ROOT + "/audio";						// 语音存放目录
	public static final String ROOT_CAMEAR = ROOT + "/camera";			// 图片目录
	public static final String APKROOT = ROOT + "/install";
	public static final String EXCEPTIONROOT = ROOT + "/exception.txt";	// 异常错误
	public static final String ROOT_CACHE = ROOT + "/cache";				// 缓存路径
	public static final String ROOT_CRASH = ROOT+"/crash/";//
	
	// unit
	public static final int KB = 1024;
	public static final int MB = KB * KB;
	public static final int GB = KB * KB * KB;
	
	static {
		File fileroot = new File(ROOT);
		if (!fileroot.exists()) {
			fileroot.mkdir();
		}
	}

	/**
	 * 得到下载的路径
	 * 
	 * @return
	 */
	public static String getDownloadPath() {
		File download = new File(ROOT_DOWNLOAD_PICTURE);
		if (!download.exists()) {
			download.mkdir();
		} else if (!download.isDirectory()) {
			download.delete();
			download.mkdir();
		}
		return ROOT_DOWNLOAD_PICTURE;
	}

	public static String getDownloadFilePath(String filename) {
		return getDownloadPath() + "/" + filename;
	}

	public FileHelper() {
		File file1 = new File(ROOT);
		if (!file1.exists()) {
			file1.mkdir();
		}
	}

	@SuppressWarnings("unused")
	private void init() {
		File fileroot = new File(ROOT);
		if (!fileroot.exists()) {
			fileroot.mkdir();
		}
		File cameraroot = new File(ROOT_CAMEAR);
		if (!cameraroot.exists()) {
			cameraroot.mkdir();
		}
	}

	// 照相用路径
	public static String getCameraFileName() {
		File cameraroot = new File(ROOT_CAMEAR);
		if (!cameraroot.exists()) {
			cameraroot.mkdir();
		}
		return ROOT_CAMEAR + "/camera.jpg";
	}

	public static void deleteFile(String path) {
		if (TextUtils.isEmpty(path)) {
			return;
		}
		try {
			File file = new File(path);
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param dir
	 *            附件根目录
	 * @param filename
	 *            文件名
	 * @return
	 */
	public static boolean isExistFileByFilename(String dir, String filename) {
		String path = dir + "/" + filename;
		return isExistFile(path);
	}

	public static boolean isExistFile(String filepath) {
		File file = new File(filepath);
		if (file.exists()) {
			return true;
		}
		return false;
	}

	public static boolean isExistDownloadFileByFilename(String filename) {
		return isExistFileByFilename(ROOT_DOWNLOAD_PICTURE, filename);
	}

	/**
	 * 根据文件路径删除文件
	 * 
	 * @param filePath
	 */
	public static void deleteFileByPath(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return;
		}
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
	}

	
	/**
	 * 将位置的数据保存到文件中，类似断点续传
	 * 
	 * @param filename
	 * @param data
	 * @return
	 */
	public File saveLocatioinTOFile(String filename, String data) {

		File file = new File(filename);
		FileWriter fileWriter = null;
		BufferedWriter buffWriter = null;
		// 判断文件是否存在 如果不存在就创建
		if (!file.exists()) {
			try {
				file.createNewFile();
				fileWriter = new FileWriter(file);
				buffWriter = new BufferedWriter(fileWriter);
				buffWriter.write(data);
			} catch (IOException e) {
				e.printStackTrace();
				Log.e(TAG, "创建文件出现异常了");
			} finally {
				try {
					fileWriter.close();
					buffWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
					Log.e(TAG, "输出流关闭出现异常了");
				}
			}
		}
		return file;

	}

	/**
	 * 保存图片到指定的路径中，如果路径中存在，则删除
	 * 
	 * @param path
	 * @param bitmap
	 */
	public static void savePicture(String path, Bitmap bitmap) {
		try {
			File file = new File(path);
			if (file.exists()) {
				file.delete();
			}
			
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)) {
				fos.flush();
				fos.close();
			}
		} catch (Exception e) {
			Log.e(TAG, "保存图片，图片路径错误");
			e.printStackTrace();
		}
	}
	
	/**
	 * 从图片URL中获取对应文件的名称
	 * 
	 * @param url
	 * @return
	 */
	public static String getFileNameFromUrl(String url){
		if (StringUtil.checkEmpty(url)) {
			return "";
		}
		
		int index = url.lastIndexOf("/");
		if (index == -1) {
			return null;
		}
		return url.substring(index + 1);
	}
	
	/**
	 * 根据图片网络路径得到本地缓存路径
	 * 
	 * @param context
	 * @param imageUrl
	 * @return
	 */
//	public static String getCacheUilImagePath(Context context, String imageUrl) {
//		// 4edzzme6t8ggmfdlv6v5yixws
//		return StorageUtils.getIndividualCacheDirectory(context).getAbsolutePath()
//				+ "/"
//				+ MD5FileNameGenerator.generate(imageUrl) + ".0";
//	}
}
