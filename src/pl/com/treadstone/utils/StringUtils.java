package pl.com.treadstone.utils;

import java.util.Random;

public class StringUtils {

	
	public static String getFileName(String url){
		String fileName = "";
		
		if(url != null && url.length() > 0){
			String[] array = url.split("/");
			
			fileName = array[array.length - 1];
			
			if(fileName.length() > 0){
				return fileName;
			}
		}
		
		Random random = new Random();
		fileName = random.nextInt(100000) + "";
		
		return fileName;
	}
}
