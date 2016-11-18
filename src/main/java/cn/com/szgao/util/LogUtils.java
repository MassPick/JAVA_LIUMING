package cn.com.szgao.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.output.FileWriterWithEncoding;
//import org.jfree.util.Log;

public class LogUtils {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static int i = 0;
	static int SIZE = 100000;
	static File logFile = null;
	private static BufferedWriter bufferedWriter;

	@SuppressWarnings("resource")
	public static void writerDataToLog(String content) throws IOException {
		if (!logFile.exists()) {
			logFile.createNewFile();
		}
		if (logFile == null) {
			throw new IllegalStateException("logFile can not be null!");
		}
		i++;
		System.out.println(i);
		Writer txtWriter = new FileWriter(logFile, true);
		txtWriter.write(dateFormat.format(new Date()) + "\t" + content + "\n");
		txtWriter.flush();
	}

	@SuppressWarnings({ "unused", "resource" })
	public static void writerDataToLog(File logFile, String content) throws IOException {
		if (!logFile.exists()) {
			logFile.createNewFile();
		}
		if (logFile == null) {
			throw new IllegalStateException("logFile can not be null!");
		}
		// i++;
		// System.out.println(i);
		Writer txtWriter = new FileWriter(logFile, true);
		// txtWriter.write(dateFormat.format(new Date()) + "\t" + content +
		// "\n");
		txtWriter.write(content + "\n");
		txtWriter.flush();
	}

	public static void writerDataToLogList(File logFile, List<String> contentList) throws IOException {
		if (!logFile.exists()) {
			logFile.createNewFile();
		}
		if (logFile == null) {
			throw new IllegalStateException("logFile can not be null!");
		}
		// i++;
		// System.out.println(i);

		// Writer txtWriter = new FileWriterWithEncoding(logFile,"UTF-8", true);

		// 可以
//		Writer txtWriter = new FileWriterWithEncoding(logFile, "GB18030", true);
		Writer txtWriter = new FileWriterWithEncoding(logFile, "UTF-8", true);
		// Writer txtWriter = new FileWriter(logFile, true);
		if (contentList != null) {
			if (contentList.size() > 0) {
				for (String value : contentList) {
					txtWriter.write(value + "\n");
				}
			}
		}
		txtWriter.flush();

		/*
		 * bufferedWriter = new BufferedWriter (new OutputStreamWriter( new
		 * FileOutputStream(logFile), "UTF-8")); if (contentList != null) { if
		 * (contentList.size() > 0) { for (String value : contentList) {
		 * bufferedWriter.write(value + "\n"); } } } bufferedWriter.flush();
		 */

	}

	public static void writerTxt(BufferedWriter fw , List<String> contentList) {

		try {
			if (contentList != null) {
				if (contentList.size() > 0) {
					for (String value : contentList) {
						fw.append(value + "\n");
					}
				}
			}

//			fw.newLine();
			fw.flush(); // 全部写入缓存中的内容
		} catch (Exception e) {
//			Log.error("写文件异常"+e.getMessage());
		} finally {
//			if (fw != null) {
//				try {
//					fw.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
		}
	}

	public static void main(String[] args) throws IOException {

		File fileDir = new File("D:/lm/log/temp");
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		String filename = null;
		File file = null;

		for (int i = 0; i < 250000; i++) {

			if (i % SIZE == 0) {
				filename = "/temp" + i + ".log";
				file = new File(fileDir.getPath() + filename);
				if (!file.exists()) {
					file.createNewFile();
				} else {
					file.delete();
					file = new File(fileDir.getPath() + filename);
				}
			}
			writerDataToLog(file, "这是内容: " + i);
			System.out.println(i);
		}
		System.out.println("完成");
	}

}
