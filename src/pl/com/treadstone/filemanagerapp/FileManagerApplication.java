package pl.com.treadstone.filemanagerapp;

import pl.com.treadstone.database.FileManagerHelper;
import pl.com.treadstone.downloadFile.DownloadFileManager;
import pl.com.treadstone.fileList.FileListFragment;
import pl.com.treadstone.fileList.FileListManager;
import pl.com.treadstone.utils.DisplayUtils;
import tparys.download.DownloadManager;
import tparys.signals.SignalListener;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;

public class FileManagerApplication extends Application{

	private static FileManagerApplication _instance;
	private FileManager _fileManager;
	
	
	private int _windowWidth = -1;
	private int _windowHeight = -1;
	
	private FragmentActivity _currentFragmentActivity;
	
	// zarzàdca baza danych
	private FileManagerHelper fileManagerHelper;
	
	public FileManagerHelper getFileManagerHelper(){
		return fileManagerHelper;
	}
	
	
	
	// zarzadca pobierania plików
	private DownloadFileManager downloadFileManager;
	
	public DownloadFileManager getDownloadFileManager(){
		return downloadFileManager;
	}
	
	// zarzadca listy plików
	private FileListManager fileListManager;
	
	public FileListManager getFileListManager(){
		return fileListManager;
	}
	/**
	 * Aplikacja jest uruchamiana.
	 */
	public void onCreate() {
		_instance = this;
		_fileManager = new FileManager();
		
		fileManagerHelper = new FileManagerHelper(getApplicationContext());
		
		downloadFileManager = new DownloadFileManager();
		
		fileListManager = new FileListManager();
		
	}
	
	public FileManager getFileManager(){
		return _fileManager;
	}
	
	/**
	 * Rejestruje activity w aplikacji.
	 * @param activity
	 */
	public void registerActivity(final FragmentActivity fragmentActivity) {		
		_currentFragmentActivity = fragmentActivity;
		
		// rozmiary okna
		if (_windowWidth <= 0) _windowWidth = DisplayUtils.getAvailableWidth(fragmentActivity);
		if (_windowHeight <= 0) _windowHeight = DisplayUtils.getAvailableWidth(fragmentActivity);
		
		// niepowodzenie w trakcia uruchamiania activity
		fragmentActivity.onStartFailed.addOnce(new SignalListener<String>() {			
			@Override
			public void onSignal(String reason) {
				onActivityStartFailed(fragmentActivity, reason);	
			}
		});
	}

	/**
	 * Activity nie mogÔøΩo zostaÔøΩ uruchomione.
	 * @param activity
	 */
	protected void onActivityStartFailed(FragmentActivity activity, String reason) {
		
		// nie potrzebujemy ju˝ tego activity, skoro juz nie dzia∏a
		activity.finish();
		
		// przechodzimy do preloadera
		Intent intent = new Intent(this, MainFragmentActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		activity.startActivity(intent);
	}
	
	/**
	 * Usuwa activity z aplikacji.
	 * @param activity
	 */
	public void unregisterActivity(FragmentActivity activity) {
		
				
		// sprawdza, czy nie zostaje zabite zastopowane ostatnie activity
		if (_currentFragmentActivity == activity) {
			_currentFragmentActivity = null;
			onStop();
		}
	}
	
	/**
	 * Zwraca aktualne activity.
	 * @return
	 */
	public FragmentActivity getCurrentActivity() {
		return _currentFragmentActivity;
	}
	
	
	/**
	 * Aplikacja zostaje wstrzymana.
	 * Wy≈ÇƒÖczymy niepotrzebne taski.
	 */
	protected void onStop() {
		
		DownloadManager.getInstance(FileManagerApplication.getInstance()).cancelAllDownloads();
		
		Log.i("", "Application Stopped!");
	}
	
	
	
	/**
	 * Zwraca Context aplikacji.
	 * @return
	 */
	public Context getContext() {
		return this;
	}
	
	/**
	 * Zwraca zasoby aplikacji.
	 * @return
	 */
	@Override
	public Resources getResources() {
		return super.getResources();
	}
	
	/**
	 * Zwraca aktualnà wysokoÊç okna aplikacji.
	 * @return
	 */
	public int getWindowHeight() {
		return _windowHeight;
	}
	
	/**
	 * Zwraca aktualnà szerokoÊç okna aplikacji.
	 * @return
	 */
	public int getWindowWidth() {
		return _windowWidth;
	}

	public static FileManagerApplication getInstance() {
		
		return _instance;
	}
	
}
