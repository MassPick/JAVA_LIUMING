package cn.com.szgao.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.com.szgao.dto.CodeVO;
import cn.com.szgao.util.PrCiCouText;

public class Test2 {

	public static void main(String[] args) throws FileNotFoundException {

		// List<CodeVO> code = PrCiCouText.getregNumList("120116000219635
		// 911201160931130117");
		// for (CodeVO codeVO : code) {
		// System.out.println(codeVO.getCode() +" "+codeVO.getStatus());
		// }
		
		long startTime = System.currentTimeMillis();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
		Date date = new Date(startTime);
		System.out.println("开始时间--------------------" + formatter.format(date));
		
		

		List<Path> sources = new ArrayList<Path>();

		String path="E:/工商数据未排序/广西.json";
		File fileS = new File(path);
		if (!fileS.exists()) {
			try {
				fileS.createNewFile();
				
			} catch (IOException e) {
			}
		} else {
			fileS.delete();
			fileS =  new File(path);
		}
		Path target = fileS.toPath();
		
		
		File file = new File("D:/lm/log/工商清洗数据/广西");
		
		

		if (file.isFile()) {
			try {

			} catch (Exception e) {
				e.printStackTrace();
			}
			// file.delete();
			return;
		}
		File[] files = file.listFiles();
		if (null != files) {
			for (File fi : files) {
				if (fi.isFile()) {
					sources.add(fi.toPath());
				} else if (fi.isDirectory()) {
				} else {
					continue;
				}
			}
		}

		for (Path f : sources) {
			System.out.println(f.getFileName());
			try {
				Files.write(target, Files.readAllLines(f, Charset.defaultCharset()), Charset.defaultCharset(),
						StandardOpenOption.APPEND);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		long endTime = System.currentTimeMillis();
		Date endDate = new Date(endTime);
		System.out.println("结束时间--------------------" + formatter.format(endDate));
		System.out.println("Took : " + (float) ((endTime - startTime) / 1000) + "秒");
		System.out.println("Took : " + (float) ((endTime - startTime) / 1000) / 60 + "分钟");
		
	}

}
