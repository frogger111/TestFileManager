package pl.com.treadstone.fileList;

import java.util.HashMap;

import tparys.signals.Signal;

public class FileListManager {

	
	public Signal<FilesLoader.Result> onLoadFiles = new Signal<FilesLoader.Result>();
	private HashMap<Integer, FilesLoader> _filesLoadertasks = new HashMap<Integer, FilesLoader>();
	
	public synchronized void getFiles(){
		
		FilesLoader _task;
		
		_task = _filesLoadertasks.get(1);
		
		// zadanie w¸asnie jest wykonywane
		if(_task != null) return;
		
		_task = new FilesLoader(){
			
			@Override
			protected void onPostExecute(FilesLoader.Result result){
				
				synchronized (FileListManager.this) {
					
					_filesLoadertasks.remove(1);
					onLoadFiles.dispatch(result);
				}
			}
		};
		
		_filesLoadertasks.put(1, _task);
		_task.execute(null);
	}
}
