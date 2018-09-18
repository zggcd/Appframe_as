package com.asiainfo.appframe.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.asiainfo.appframe.data.Constants;
import com.asiainfo.appframe.exception.NotEnouchSpaceException;
import com.asiainfo.appframe.exception.SDNotEnouchSpaceException;
import com.asiainfo.appframe.exception.SDUnavailableException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;

public class FileHelper {
	private final static String TAG = "FileHelper";

	/**
	 * 当出错的时候返回的字节数为-1
	 */
	public final static int ERROR = -1;

	/**
	 * 当没有写入任何数据的时候返回0
	 */
	public final static int NONE = 0;

	/**
	 * 该变量是从sd卡读取文件时默认的字符缓冲区的大小
	 */
	private final static int MAX_LENTH = 1024;

	/**
	 * sd卡所在的区块位置
	 */
	private final static int SDCARD_SYSTEM = 4;

	/**
	 * 文件的存放路径
	 */
	private static String filePath = null;

	public static final long fileMaxLength = 20971520;

	/**
	 * 将从网络获取来的数据流写入文件中
	 * 
	 * @param ops
	 *            从网络获取来的io流
	 * @param fileName
	 *            需要存储的文件的名称
	 * @return 总共存储成功的字节数
	 * @throws SDNotEnouchSpaceException
	 * @throws SDUnavailableException
	 */
	public static int writeFile(RandomAccessFile file, byte[] io)
			throws SDUnavailableException, SDNotEnouchSpaceException
	{
		int length = NONE;

		if (io != null)
		{
			if (file != null)
			{
				try
				{
					file.seek(file.length());
					file.write(io);
				} catch (IOException e)
				{
					Log.trace(e.getMessage(), " fail");
					checkSD(io);
					// 如果出现异常，返回的字节数为-1，表示出现了异常，没有写入成功
					return ERROR;
				}
				length = io.length;
			} else
			{
				// checkSD(io);
				// 如果出现异常，返回的字节数为-1，表示出现了异常，没有写入成功
				return ERROR;
			}
		}
		return length;
	}

	/**
	 * 对sdcard的检查，主要是检查sd是否可用，并且sd卡的存储空间是否充足
	 * 
	 * @param io
	 *            写入sd卡的数据
	 * @throws SDUnavailableException
	 * @throws SDNotEnouchSpaceException
	 */
	public static void checkSD(byte[] io) throws SDUnavailableException,
			SDNotEnouchSpaceException
	{
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
		{
			// throw new SDUnavailableException();
		} else
		{
			if (io.length >= getFreeSD())
			{
				// 通知UI
				// throw new SDNotEnouchSpaceException();

			}
		}
	}

	/**
	 * 获取SD卡的剩余空间
	 * 
	 * @return SD卡的剩余的字节数
	 */
	public static long getFreeSD()
	{
		long nAvailableCount = 0l;
		try
		{
			StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
					.getAbsolutePath());
			nAvailableCount = (long) (stat.getBlockSize() * ((long) stat
					.getAvailableBlocks() - SDCARD_SYSTEM));
		} catch (Exception e)
		{
			Log.trace("getFreeSD() Exception", "in FileHelper.java");
		}
		return nAvailableCount;
	}

	/**
	 * 本地文件大小
	 * 
	 * @param fileName
	 *            文件的名称
	 * @return 返回文件的大小
	 */
	public static long getLocalFileSize(final String fileName)
	{
		File file = null;
		try
		{
			file = createFile(fileName);
		} catch (SDNotEnouchSpaceException e)
		{
			Log.trace(e.getMessage(),
					"FileHelper.java getLocalFileSize() Exception");
		}
		long length = 0;
		if (isFileExist(file))
		{
			length = file.length();
		}
		return length;
	}

	/**
	 * 通过提供的文件名在默认路径下生成文件
	 * 
	 * @param fileName
	 *            文件的名称
	 * @return 生成的文件
	 */
	public static File createFile(String fileName)
			throws SDNotEnouchSpaceException
	{
		File file = new File(fileName);
		if (!isFileExist(file))
		{
			try
			{
				file.createNewFile();
			} catch (IOException e)
			{
				throw new SDNotEnouchSpaceException(e.getMessage());
			}
		}
		return file;
	}

	/**
	 * 是否存在此文件或目录
	 * 
	 * @param file
	 *            判断是否存在的文件
	 * @return 存在返回true，否则返回false
	 */
	public static boolean isFileExist(final File file)
	{
		// 在无SD卡时file会为空
		if (file == null)
		{
			return false;
		}
		if (file.exists())
		{
			return true;
		} else
		{
			return false;
		}
	}

	/**
	 * 删除一个目录（可以是非空目录）
	 * 
	 * @param dir
	 */
	public static boolean delDir(File dir)
	{
		if (dir == null || !dir.exists() || dir.isFile())
		{
			return false;
		}
		for (File file : dir.listFiles())
		{
			if (file.isFile())
			{
				file.delete();
			} else if (file.isDirectory())
			{
				delDir(file);// 递归
			}
		}
		dir.delete();
		return true;
	}

	// /**
	// * 选择文件的存储路径
	// * @param fileSize 要存的文件的大小
	// * @param fileSpecies 创建的文件种类，是资源文件，是下载的文件还是解压的文件
	// * @return 拼接好的文件的存储路径
	// * @throws NotEnouchSpaceException 手机和SD卡存储空间均不足的异常
	// * @throws SDUnavailableException SD卡不可用的异常
	// * @throws SDNotEnouchSpaceException SD卡空间不足的异常
	// */
	// public static String selectFileSavaPath(long fileSize, int fileSpecies)
	// throws NotEnouchSpaceException, SDUnavailableException,
	// SDNotEnouchSpaceException
	// {
	// switch (fileSpecies)
	// {
	// // 文件的类型是资源文件
	//
	// // 文件的类型是下载的文件
	// case FusionCode.DOWNLOADFILE:
	// if (!Environment.getExternalStorageState().equals(
	// Environment.MEDIA_MOUNTED))
	// {
	// throw new SDUnavailableException("|:sd_NotEnoughSpaceAndUnload");
	// }
	// else if (!(fileSize <= getFreeSD()))
	// {
	// throw new SDNotEnouchSpaceException(
	// "|:sd_NotEnoughSpaceAndUnload");
	// }
	// else
	// {
	// filePath = FusionField.DOWNLOAD_PATH_SD;
	// }
	// break;
	//
	// default:
	// break;
	// }
	// return filePath;
	// }

	public static String selectFileSavaPath(long fileSize, String path)
			throws NotEnouchSpaceException, SDUnavailableException,
			SDNotEnouchSpaceException
	{

		if (path.startsWith("/data/data/"))
		{
			filePath = path;
			return filePath;
		}
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
		{
			throw new SDUnavailableException("|:sd_NotEnoughSpaceAndUnload");
		} else if (!(fileSize <= getFreeSD()))
		{
			throw new SDNotEnouchSpaceException("|:sd_NotEnoughSpaceAndUnload");
		} else
		{
			filePath = path;
		}

		return filePath;
	}

	/**
	 * 创建File文件
	 * 
	 * @param fileName
	 *            文件名
	 * @return 生成的文件
	 */
	public static File createDownloadFile(String path, String fileName)
			throws SDNotEnouchSpaceException
	{
		return createFile(path + fileName);
	}

	/**
	 * 删除路径指向的文件
	 * 
	 * @param filePath
	 *            文件的名称
	 */
	public static boolean deleteFile(final String filePath)
	{
		boolean isComplete = false;

		final File file = new File(filePath);

		if (file.exists())
		{
			isComplete = file.delete();
		} else
		{
			isComplete = true;
		}
		return isComplete;
	}

	/**
	 * 给文件进行md5加密
	 * 
	 * @param file
	 *            要加密的文件
	 * @return 文件加密好后形成的字符串
	 */
	public static String getMD5(File file)
	{
		FileInputStream fis = null;
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			fis = new FileInputStream(file);
			byte[] buffer = new byte[8192];
			int length = -1;
			while ((length = fis.read(buffer)) != -1)
			{
				md.update(buffer, 0, length);
			}
			return bytesToString(md.digest());
		} catch (IOException ex)
		{
			return null;
		} catch (NoSuchAlgorithmException ex)
		{
			return null;
		} finally
		{
			try
			{
				if (fis != null)
				{
					fis.close();
				}
			} catch (IOException ex)
			{
			}
		}
	}

	/**
	 * 给文件的特定区域进行加密
	 * 
	 * @param file
	 *            要加密的文件
	 * @param start
	 *            加密文件的起始位置
	 * @param end
	 *            加密文件的最终位置
	 * @return 加密好后的字符串
	 */
	public static String getMD5(File file, int start, int end)
	{
		FileInputStream fis = null;
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			fis = new FileInputStream(file);
			int len = end - start + 1;
			FileChannel ch = fis.getChannel();
			MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY,
					start, len);
			md.update(byteBuffer);
			return bytesToString(md.digest());
		} catch (IOException ex)
		{
			return null;
		} catch (NoSuchAlgorithmException ex)
		{
			return null;
		} finally
		{
			try
			{
				fis.close();
			} catch (IOException ex)
			{
			}
		}

	}

	/**
	 * 把byte转换成string
	 * 
	 * @param data
	 *            要转换的byte数组
	 * @return
	 */
	public static String bytesToString(byte[] data)
	{
		char hexDigits[] =
		{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
				'e', 'f' };
		char[] temp = new char[data.length * 2];
		for (int i = 0; i < data.length; i++)
		{
			byte b = data[i];
			temp[i * 2] = hexDigits[b >>> 4 & 0x0f];
			temp[i * 2 + 1] = hexDigits[b & 0x0f];
		}
		return new String(temp);

	}

	/**
	 * 判断文件是否为0字节。不可上传
	 * 
	 * @param path
	 *            路径
	 * @return true
	 */
	public static boolean checkFileLength(String path)
	{
		boolean result = true;
		if (path == null || 0 >= path.trim().length())
		{
			return false;
		}
		File temp = new File(path);
		long fileLength = temp.length();
		if (fileLength <= 0 || fileLength >= fileMaxLength)
		{
			result = false;
		}
		return result;
	}

	/**
	 * 判断该文件名或目录名是否合法
	 * 
	 * @param name
	 *            文件或目录名
	 * @return true 合法 false 不合法
	 */
	public static boolean isLegitName(String name)
	{
		boolean result = true;
		if (name == null || 0 >= name.trim().length())
		{
			result = false;
			return result;
		}
		String temp = name.trim();

		if (temp.contains("/") || temp.contains("\\") || temp.contains("?")
				|| temp.contains("\"") || temp.contains("'")
				|| temp.contains("<") || temp.contains(">")
				|| temp.contains("|") || temp.contains("*")
				|| temp.contains(":") || temp.contains("&"))
		{
			result = false;
		}

		return result;
	}

	/**
	 * 
	 * write bitmap to cache file
	 * 
	 * @author jinfeng.ma
	 * @param bitmap
	 * @param fileName
	 * @return
	 */
	public static boolean saveImage(Bitmap bitmap, String path,String fileName)
	{
		if (null == bitmap)
		{
			return false;
		}
		File directory = new File(path);
		if (!directory.exists())
		{
			directory.mkdirs();
		}
		File newFile = new File(path + fileName
				+ ".png");
		FileOutputStream fos = null;
		if (!newFile.exists())
		{
			try
			{
				byte[] cacheBytes = bitmapToBytes(bitmap);
				fos = new FileOutputStream(newFile);
				byte[] buffer;
				int startOffset = 0;
				int step = 1024;
				while (startOffset < cacheBytes.length)
				{
					buffer = new byte[1024];
					step = cacheBytes.length - startOffset < 1024 ? cacheBytes.length
							- startOffset
							: buffer.length;
					System.arraycopy(cacheBytes, startOffset, buffer, 0, step);
					startOffset += buffer.length;
					Log.d(String.valueOf(buffer.length));
					fos.write(buffer);
					fos.flush();
				}

			} catch (IOException e)
			{
				Log.d(TAG, "IOException" + e.getMessage());

				return false;
			} finally
			{
				if (null != fos)
				{
					try
					{
						fos.close();
						fos = null;
					} catch (IOException e)
					{
					}
				}
			}
			return true;
		} else
		{
			return false;
		}

	}

	/**
	 * Bitmap对象转化byte数组
	 * 
	 * @param bitmap
	 *            Bitmap对象
	 * @return byte数组
	 */
	public static byte[] bitmapToBytes(Bitmap bitmap)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 
	 * @param dirPath
	 * @return
	 */
	public static String creatDir2SDCard(String dirPath)
	{
		File file = new File(dirPath);
		if (!file.exists())
		{
			file.mkdirs();
		}
		if (dirPath.contains(Constants.BASE_DIR))
		{
			if (!new File(Constants.BASE_DIR + ".nomedia").exists())
			{
				try
				{
					FileHelper.createFile(Constants.BASE_DIR + ".nomedia");
				} catch (SDNotEnouchSpaceException e)
				{
					e.printStackTrace();
				}
			}
		}
		return dirPath;
	}

	public static String creatFile2SDCard(String filePath) throws IOException
	{
		// String filedir = creatDir2SDCard(getFileDir(filePath));
		// String fileFinalPath = filedir + getFileName(filePath);
		creatDir2SDCard(filePath.substring(0, filePath.lastIndexOf("/")+1));
		File file = new File(filePath);
		if (!file.exists())
		{
			file.createNewFile();
		}
		return filePath;
	}

	/**
	 * 获取文件名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath)
	{
		int index = 0;
		String tempName = "";
		if ((index = filePath.lastIndexOf("/")) != -1)
		{
			// 如果有后缀名才
			tempName = filePath.substring(index + 1);
		}
		return tempName.contains(".") ? tempName : "";
	}

	/**
	 * 获取文件目录路径
	 * 
	 * @param filePath
	 * @return
	 */
	private static String getFileDir(String filePath)
	{
		if (filePath.startsWith(getSDCardPath()))
		{
			return filePath.replace(getFileName(filePath), "");
		}
		return getSDCardPath() + filePath.replace(getFileName(filePath), "");
	}

	/**
	 * 获取SD卡路径
	 * 
	 * @return /sdcard/
	 */
	public static String getSDCardPath()
	{
		if (sdCardIsExit())
		{
			return Environment.getExternalStorageDirectory().toString() + "/";
		}
		return null;
	}

	/**
	 * 判断SD卡是否存在
	 * 
	 * @return
	 */
	public static boolean sdCardIsExit()
	{
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}


    /**
     * 获取文件后缀名
     *
     * @param fileName 文件名
     * @return String
     */
    public static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
    }

    public static Intent openFile(String filePath) {

        File file = new File(filePath);

        if ((file == null) || !file.exists() || file.isDirectory())
            return null;

        Log.d("FileHelper.openFile:文件存在！");
        /* 取得扩展名 */
        String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase();
        /* 依扩展名的类型决定MimeType */
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") ||
                end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            return getAudioFileIntent(filePath);
        } else if (end.equals("3gp") || end.equals("mp4")) {
            return getAudioFileIntent(filePath);
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") ||
                end.equals("jpeg") || end.equals("bmp")) {
            return getImageFileIntent(filePath);
        } else if (end.equals("apk")) {
            return getApkFileIntent(filePath);
        } else if (end.equals("ppt")) {
            return getPptFileIntent(filePath);
        } else if (end.equals("xls")) {
            return getExcelFileIntent(filePath);
        } else if (end.equals("doc")) {
            return getWordFileIntent(filePath);
        } else if (end.equals("pdf")) {
            return getPdfFileIntent(filePath);
        } else if (end.equals("chm")) {
            return getChmFileIntent(filePath);
        } else if (end.equals("txt")) {
            return getTextFileIntent(filePath, false);
        } else {
            return getAllIntent(filePath);
        }
    }

    //Android获取一个用于打开APK文件的intent
    public static Intent getAllIntent(String param) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "*/*");
        return intent;
    }

    //Android获取一个用于打开APK文件的intent
    public static Intent getApkFileIntent(String param) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    //Android获取一个用于打开VIDEO文件的intent
    public static Intent getVideoFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    //Android获取一个用于打开AUDIO文件的intent
    public static Intent getAudioFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    //Android获取一个用于打开Html文件的intent
    public static Intent getHtmlFileIntent(String param) {

        Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").
                scheme("content").encodedPath(param).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    //Android获取一个用于打开图片文件的intent
    public static Intent getImageFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    //Android获取一个用于打开PPT文件的intent
    public static Intent getPptFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    //Android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    //Android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    //Android获取一个用于打开CHM文件的intent
    public static Intent getChmFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    //Android获取一个用于打开文本文件的intent
    public static Intent getTextFileIntent(String param, boolean paramBoolean) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean) {
            Uri uri1 = Uri.parse(param);
            intent.setDataAndType(uri1, "text/plain");
        } else {
            Uri uri2 = Uri.fromFile(new File(param));
            intent.setDataAndType(uri2, "text/plain");
        }
        return intent;
    }

    //Android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }
    
    /**
     * 获取刚刚拍摄的照片
     *
     * @param file
     * @return
     */
    public static String getRealFilePath(File file) {
        String filePath = "";
        File[] fileList = file.listFiles();
        if (fileList != null && fileList.length > 0) {
            File temp = null;
            for (int i = 0; i < fileList.length; i++) {
                for (int j = fileList.length - 1; j > i; j--) {
                    if (fileList[j].lastModified() > fileList[j - 1].lastModified()) {
                        temp = fileList[j];
                        fileList[j] = fileList[j - 1];
                        fileList[j - 1] = temp;
                    }
                }

            }
            filePath = fileList[0].getAbsolutePath();
        }

        return filePath;
    }
}
