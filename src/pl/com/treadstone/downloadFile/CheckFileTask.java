package pl.com.treadstone.downloadFile;

import pl.com.treadstone.downloadFile.CheckFileTask.Result.Status;
import pl.com.treadstone.filemanagerapp.FileManagerApplication;
import pl.com.treadstone.utils.StringUtils;
import pl.com.treadstone.utils.Util;
import tparys.conturrent.AsyncTask;
import android.content.Context;
import android.net.ConnectivityManager;
import android.webkit.URLUtil;

import com.example.filemanagerapp.R;

public class CheckFileTask extends AsyncTask<String, Float, CheckFileTask.Result>{
	
	// zgodnosc pliku z typem akceptowalnym przez aplikacje
	private boolean isValidMimeType = false;
	
	@Override
	protected Result doInBackground(String url) {
		
		Context context = FileManagerApplication.getInstance().getContext();
		
		// czy zadanie zosta∏o przerwane?
		if(isCancelled())
			return createResult(Status.CANCELED, url);
		
		// sprawdzam poprawnosc url-a
		if(!URLUtil.isValidUrl(url)){
			
			return createResult(Status.INVALID_URL, url);
		} else {
			
			// nazwa pliku
			String fileName = StringUtils.getFileName(url);
			
			if(fileName == null)
				return createResult(Status.FAILED, url);
			
			// czy istnieje nazwa pliku
			if(fileName.equals(""))
				return createResult(Status.INVALID_URL, url);
			
			// czy zadanie zosta∏o przerwane?
			if(isCancelled())
				return createResult(Status.CANCELED, url);
			
			
			// sprawdzam dostepnoÊç po∏aczenia internetowego
			ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connMgr.getActiveNetworkInfo() == null || !connMgr.getActiveNetworkInfo().isConnected()) {
				
			    return createResult(Status.INTERNET_CONNECTION_PROBLEM, url);
			} else {
			       
				String mimetype = Util.getMimeType(url);
				
				if(mimetype == null)
					return createResult(Status.INVALID_MIMETYPE, url);
				
				// sprawdzam czy plik ma odpowiedni type, akceptowalny przez aplikacje
				for(String type : context.getResources().getStringArray(R.array.MIMETYPE)){
					if(mimetype.equals(type)){
						isValidMimeType = true;
						break;
					}
				}
				
				
				if(!isValidMimeType)
					return createResult(Status.INVALID_MIMETYPE, url);
				
				// czy zadanie zosta∏o przerwane?
				if(isCancelled())
					return createResult(Status.CANCELED, url);
				
				if(FileManagerApplication.getInstance().getFileManagerHelper().checkFile(fileName)){
					
					// plik istnieje w bazie danych, czy nadpisaç?
					return createResult(Status.EXIST_IN_DATABASE, url);
				} else {
					return createResult(Status.BEGIN_DOWNLOAD, url);
				}
			}
		}
	}
	
	private Result createResult(Status status, String url){
		Result result = new Result();
		result.status = status;
		result.url = url;
		
		return result;
	}
	
	public static class Result{
		
		public String url;
		public Status status;
		
		public static enum Status{
			FAILED,
			CANCELED,
			EXIST_IN_DATABASE,
			INVALID_URL,
			SQL_INIECTION,
			INVALID_MIMETYPE,
			INTERNET_CONNECTION_PROBLEM,
			BEGIN_DOWNLOAD
			
		}
	}

	
}
