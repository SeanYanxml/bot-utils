package com.yanxml.java.bot.utils.http.setu.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 获取照片工具类。
 * 
 * */
public class ImageUtil {
	// 需要读取的URL存档文件链接
	public static String urlFileNameN = "setu_n";
	// 需要抓取的图片地址
	public static String targetFolderN = "./image/setu";
	
	public static String urlFileNameR18 = "setu_r18";
	public static String targetFolderR18 = "./image/setu-r18";
	
	private static OkHttpClient okHttpclient = new OkHttpClient();

	private static List<String> imageUrlList;
	
	private static volatile int imageIndex = 0;

	/**
	 * 按行读取文件。
	 * 
	 * */
	public static void readFile(String filename) {
		File file = new File(filename);
		//InputStreamReader reader = new InputStreamReader(new BufferedInputStream());
		//reader.read();
	}
	
	/**
	 * 根据链接获取照片。
	 * 
	 * 为什么不使用"FileWriter"?
	 * */
	public static void getImage(String url,String targetFolder) {
		try {
			// OkHttpClient okHttpclient = new OkHttpClient();
			// 设置请求request
			Request request = new Request.Builder().url(url).get().build();
			// 获取reponse
			Response response = okHttpclient.newCall(request).execute();
			// 获取图片流
			InputStream imgaeInputStream = response.body().byteStream();
			String imageFileName =  targetFolder + '/' + getFileNameFromUrl(url);
			// 拷贝图片
			saveImage(imageFileName,targetFolder,imgaeInputStream);
			response.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
	}
	
	/**
	 * 根据二进制流存储照片。
	 * 
	 * 
	 * */
	public static void saveImage(String targetFileName, String targetFileFolder,InputStream in) {
		try {
			// 判断父文件夹是否存在
			imageFolderExists(targetFileFolder);
			// 创建拷贝的图片文件是否存在
			File targetImage = new File(targetFileName);
			targetImage.createNewFile();
			// 输出字符流
			FileOutputStream outputStream = new FileOutputStream(targetImage);
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
			// 缓冲区-拷贝
			int length = 0;
			byte b[] = new byte[1024*1024];
			while((length=in.read(b))!=-1) {
				//outputStream.write(b, 0, length);
				//
				bufferedOutputStream.write(b, 0, length);
			}
			
			// 关闭流
			//in.close();
			//outputStream.close();
			bufferedOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 判断图片写入的父文件夹是否存在.
	 * 
	 * */
	public static boolean imageFolderExists(String folder) {
		File parentFolder = new File(folder); 
		if(!parentFolder.exists()) {
			parentFolder.mkdirs();
		}
		return true;
	}
	/**
	 * 处理链接
	 * 
	 * 预计输入: https://i.pixiv.cat/img-original/img/2019/06/24/21/09/37/75389702_p0.png
	 * 预计输出: 75389702_p0.png
	 * */
	public static String getFileNameFromUrl(String url) {
		String name = url.substring(url.lastIndexOf('/')+1); 
		return name;
	}
	
	/**
	 * 读取URL文件.将其存入List内.
	 * 
	 * */
	public static void updateImageList(String imgaeUrlListFileName) {
		if(null == imageUrlList) {
			// 初始化
			imageUrlList = new ArrayList<String>();
		}
		try {
			// 读取文件
			BufferedReader reader = new BufferedReader(new FileReader(new File(imgaeUrlListFileName)));
			String line;
			while((line = reader.readLine())!=null) {
				imageUrlList.add(line);
			}
			// 关闭读取流
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	/**
	 * 启动多个线程获取照片。
	 * 
	 * */
	public static void multiThreadHandleJob() {
		//Executor executor = Executors.newFixedThreadPool(1);
		while(imageIndex<imageUrlList.size()) {
			System.out.println("ImageIndex:"+imageIndex+" url:"+imageUrlList.get(imageIndex)+"  Begin.");
			getImage(imageUrlList.get(imageIndex++),targetFolderN);
			System.out.println("ImageIndex:"+(imageIndex-1)+" url:"+imageUrlList.get(imageIndex-1)+"  Done.");
			/***
			executor.execute(new Runnable() {
				public void run() {
					System.out.println("ImageIndex:"+imageIndex+" url:"+imageUrlList.get(imageIndex)+"  Begin.");
					getImage(imageUrlList.get(imageIndex++),targetFolderN);
					System.out.println("ImageIndex:"+imageIndex+" url:"+imageUrlList.get(imageIndex-1)+"  Done.");
				}
			});***/
		}
	}
	
	public static void mainFunction() {
		// 设置需要读取的文件
		String testImageUrlListFile = "./src/main/resources/res/url/setu_n.txt";
		// 初始化ImageList
		updateImageList(testImageUrlListFile);
		
		//for(int i=0;i<imageUrlList.size();i++) {
		//	getImage(imageUrlList.get(i),targetFolderN);
		//}
		
		// 多线程处理
		multiThreadHandleJob();
	}
	public static void main(String []args) {
		// Test UrlMethod
		//String testUrl = "https://i.pixiv.cat/img-original/img/2019/06/24/21/09/37/75389702_p0.png";
		//getImage(testUrl,targetFolderN);
		
		// TestReadImageFile
		//String testImageUrlListFile = "./src/main/resources/res/url/setu_n.txt";
		//updateImageList(testImageUrlListFile);
		//int i=0;
		//for(String line:imageUrlList) {
		//	System.out.println("i="+(i++) +"  : "+ line);
		//}
		
		mainFunction();
		
	}

}
