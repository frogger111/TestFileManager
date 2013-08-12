package pl.com.treadstone.utils;

import pl.com.treadstone.filemanagerapp.FileManagerApplication;
import android.content.Context;
import android.os.Environment;

public class FileUtil {

	/**
	 * Sprawdza dostepnosc zaenetrznej karty pamieci.
	 * 
	 * @return
	 */
	public static boolean isExternalStrageWritable(){
		
		String state = Environment.getExternalStorageState();
		
		if(Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Sprawdza czy mozna czytac z zewnetrznej karty pamieci.
	 * 
	 * @return
	 */
	public boolean isExternalStorageReadable() {
		
	    String state = Environment.getExternalStorageState();
	    
	    if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    
	    return false;
	}
	
	
	public static String getAppPath(){
		return FileManagerApplication.getInstance().getExternalFilesDir("").getAbsolutePath();
	}
}
