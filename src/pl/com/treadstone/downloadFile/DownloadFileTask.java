package pl.com.treadstone.downloadFile;

import pl.com.treadstone.downloadFile.DownloadFileTask.Result.Status;
import tparys.conturrent.AsyncTask;

public class DownloadFileTask extends AsyncTask<String, DownloadFileTask.Result, DownloadFileTask.Result>{

	@Override
	protected Result doInBackground(String url) {
		
		int progress = 0;
		
		// TODO Auto-generated method stub
		return createResult(Status.FAILED, url, progress);
	}
	
	private Result createResult(Status status, String url, int progress){
		Result result = new Result();
		result.status = status;
		result.url = url;
		
		return result;
	}
	
	
	public static class Result{
		
		public String url;
		public Status status;
		public int progress;
		public static enum Status{
			SUCCESS,
			CANCELED,
			PAUSED,
			RESUMED,
			FAILED,
			RUNNING,
			PENDING,
		}
	}


	
}
