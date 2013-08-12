package pl.com.treadstone.utils;

import android.webkit.MimeTypeMap;

public class Util {

	
	public static String getMimeType(String url)
	{
		String type = null;
	    String extension = MimeTypeMap.getFileExtensionFromUrl(url);
	    if (extension != null) {
	        MimeTypeMap mime = MimeTypeMap.getSingleton();
	        type = mime.getMimeTypeFromExtension(extension);
	    }
	    return type;
	}
}
