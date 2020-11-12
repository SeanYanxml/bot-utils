package com.yanxml.java.bot.utils.userlist.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 用来读取数据文件。拼接后输出为HTML文件。
 * 
 * */
public class UserListHTML {
	// 存储的临时用户列表
	static class User{
		String id;
		String userName;
		public User() {}
		public User(String id,String userName) {
			this.id = id;
			this.userName = userName;
		}
	}
	
	public static List<User> userList;
	
	public static boolean userListInit() {
		if(null== userList) {
			userList = new ArrayList<UserListHTML.User>(); 
		}
		return true;
	}
	
	/**
	 * 步骤1: 从文件读取信息 将其转变为User对象 附带到userList后。
	 * 
	 * (文件内容格式信息 "* 1174696337925 松籽")
	 * */
	public static void exchangeUserListFromFile(String filePath) {
		userListInit();
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(filePath)));
			String line;
			// 读取文件信息
			while((line=bufferedReader.readLine())!=null) {
				String userArray[] = line.split(" ");
				User user = new User(userArray[1],userArray[2]);
				userList.add(user);
			}
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	/**
	 * 步骤2: 拼接HTML字符串
	 * 
	 * <html><body><h1>It works!</h1></body></html>
	 * <br/><a href="./PCR/Battle/PJJC/ID/1360497861174">加油P场挖矿人</a>
	 * */
	public static String contactHtmlString() {
		StringBuilder builder = new StringBuilder();
		// 添加头字符串 <html><body><h1>It works!</h1></body></html>
		builder.append(" <html><body><h1>It works!</h1>");
		// 拼接换行符号
		builder.append("\r");
		
		// 拼接用户本体
		for(User user:userList) {
			builder.append("<br/><a href=\"./PCR/Battle/PJJC/V1/ID/");
			builder.append(user.id);
			builder.append("\">");
			builder.append("* "+user.id+" : "+user.userName);
			builder.append("</a>");
			// 文件换行 windows 选择\n\r mac \r linux \n 
			builder.append("\r");
		}
		
		// 添加尾巴字符串
		builder.append("</body></html>");
		
		return builder.toString();
	}
	
	/**
	 * 步骤3: 根据输出文件。写出到文件内。
	 * 
	 * */
	public static void outputToFile(String fileString,String outputFileName) {
		File file = new File(outputFileName);
		try {
			// 创建新文件
			file.createNewFile();
			//BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
			writer.write(fileString);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	// 主要流程方法
	public static void mainFunction() {
		String readFilePath = "./src/main/resources/userlist.txt";
		String outputFilePath = "./src/main/resources/userlist.html";
		//1. 读取文件内的人物列表信息
		exchangeUserListFromFile(readFilePath);
		//2. 拼接HTML页面字符串
		String resultString = contactHtmlString();
		//3. 将HTML页面字符串创建成HTML文件输出
		outputToFile(resultString,outputFilePath);
	}
	
	public static void main(String args[]) {
		mainFunction();
	}

}
