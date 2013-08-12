package pl.com.treadstone.downloadFile;

import pl.com.treadstone.downloadFile.DownloadFileFragment.DownloadFileListener;
import pl.com.treadstone.filemanagerapp.FileManagerApplication;
import pl.example.interfaces.Controller;
import tparys.download.DownloadRequestStatus;
import android.content.Context;


public class DownloadFileController implements Controller{

	
	private DownloadFileFragment fragment;
	private Context context;
	
	public DownloadFileController(DownloadFileFragment fragment){
		this.fragment = fragment;
		context = fragment.getActivity();
		fragment.setListsner(downloadFileListener);
	}
	
	
	DownloadFileListener downloadFileListener = new DownloadFileListener() {
		
		@Override
		public void onDownloadClick(String url) {
			FileManagerApplication.getInstance().getDownloadFileManager().downloadFile(url);
		}

		@Override
		public void onCheckClick(String url) {
			
			FileManagerApplication.getInstance().getDownloadFileManager().checkFile(url);
			
		}

		@Override
		public void pause() {
			FileManagerApplication.getInstance().getDownloadFileManager().pause();
			
		}

		@Override
		public void resume() {
			FileManagerApplication.getInstance().getDownloadFileManager().resume();
			
		}

		@Override
		public void clearAfterCrash() {
			FileManagerApplication.getInstance().getDownloadFileManager().clearAfterCrash();
			
		}

		@Override
		public DownloadRequestStatus wasCrashed() {
			return FileManagerApplication.getInstance().getDownloadFileManager().wasCrashed();
		}
	};

	@Override
	public void dispose() {
		fragment.setListsner(null);
		
	}
	
}
