package pl.com.treadstone.downloadFile;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import pl.com.treadstone.database.FileManagerHelper;
import pl.com.treadstone.filemanagerapp.FileManagerApplication;
import pl.com.treadstone.utils.Util;
import tparys.data.IValueChangeListener;
import tparys.download.DownloadManager;
import tparys.download.DownloadRequest;
import tparys.download.DownloadRequestStatus;
import tparys.download.URLUtil;
import tparys.signals.Signal;
import android.content.Context;

public class DownloadFileManager {

	
	
	
	public Signal<CheckFileTask.Result> onCheckFileSignal = new Signal<CheckFileTask.Result>();
	private HashMap<String, CheckFileTask> _checkFiletasks = new HashMap<String, CheckFileTask>();
	
	public synchronized void checkFile(String url){
		
		CheckFileTask _task;
		
		_task = _checkFiletasks.get(url);
		
		// zadanie w¸asnie jest wykonywane
		if(_task != null) return;
		
		_task = new CheckFileTask(){
			
			@Override
			protected void onPostExecute(CheckFileTask.Result result){
				
				synchronized (DownloadFileManager.this) {
					
					if(result.status == Result.Status.BEGIN_DOWNLOAD){
						
						//rozpoczynam pobieranie
						downloadFile(result.url);
					}
					_checkFiletasks.remove(result.url);
					onCheckFileSignal.dispatch(result);
				}
				
			}
			
			@Override
			protected void onCancelled(){
				
			}
			
		};
		
		_checkFiletasks.put(url, _task);
		_task.execute(url);
		
	}
			
			
	public Signal<DownloadRequestStatus> onDownlaodFile = new Signal<DownloadRequestStatus>();
	public Signal<Float> onPogress = new Signal<Float>();
			
	private DownloadRequest downloadRequest;
	
	public void downloadFile(String url){
		
		Context context = FileManagerApplication.getInstance();
		
		
		try {
			downloadRequest = DownloadManager.getInstance(context).download(new URL(url));
			downloadRequest.getStatus().addListener(new IValueChangeListener<DownloadRequestStatus>() {
				
				@Override
				public void onChange(DownloadRequestStatus status) {
					onDownlaodFile.dispatch(status);
					if(status == DownloadRequestStatus.SUCCESS)
						putFileIntoDatabase(downloadRequest);
					if(status == DownloadRequestStatus.SUCCESS || status == DownloadRequestStatus.FAILED)
						downloadRequest = null;
				}
			});
			
			downloadRequest.getProgress().addListener(new IValueChangeListener<Float>() {
				
				@Override
				public void onChange(Float value) {
					onPogress.dispatch(value);
					
				}
			});
			
		} catch (MalformedURLException e) {
			onDownlaodFile.dispatch(DownloadRequestStatus.UNKONWN_HOST_EXCEPTION);
		}
	}

	


	protected void putFileIntoDatabase(DownloadRequest downloadRequest) {
		String mimetype = Util.getMimeType(downloadRequest.getURL().toString());
		
		if(mimetype != null)
			if(FileManagerHelper.getInstance(FileManagerApplication.getInstance()).putFileIntoDatabase(downloadRequest.getURL().toString(), mimetype, downloadRequest.getLenght(), URLUtil.getPathToDirectory(downloadRequest.getFile().getAbsolutePath()) + downloadRequest.getFileName()))
				onDownlaodFile.dispatch(DownloadRequestStatus.SUCCESS);
			else
				onDownlaodFile.dispatch(DownloadRequestStatus.FAILED);
			
		else {
			onDownlaodFile.dispatch(DownloadRequestStatus.FAILED);
		}
	}




	public void pause() {
		
		DownloadManager.getInstance(FileManagerApplication.getInstance()).pause();
	}
	
	public void resume(){
		if(downloadRequest != null)
			DownloadManager.getInstance(FileManagerApplication.getInstance()).resume(downloadRequest);
	}


	public Signal<Boolean> resetSignal = new Signal<Boolean>();

	public void clearAfterCrash() {
		if(downloadRequest == null){
			DownloadManager.getInstance(FileManagerApplication.getInstance()).clear();
			
		}
		resetSignal.dispatch();
	}


	public DownloadRequestStatus wasCrashed() {
		return DownloadManager.getInstance(FileManagerApplication.getInstance()).getDownloadRequestStatus();
	}
}
