package pl.com.treadstone.fileList;

import java.util.List;

import pl.com.treadstone.database.FileManagerHelper;
import pl.com.treadstone.filemanagerapp.FileManagerApplication;
import pl.com.treadstone.model.DownoadFile;
import tparys.conturrent.AsyncTask;

public class FilesLoader extends AsyncTask<Void, Void, FilesLoader.Result>{

	@Override
	protected Result doInBackground(Void param) {
		
		
		List<DownoadFile> fileList = FileManagerHelper.getInstance(FileManagerApplication.getInstance()).getDownloadFiles();
		
		return createResult(fileList);
	}
	
	private Result createResult(List<DownoadFile> fileList){
		
		Result result = new Result();
		result.downoadFiles = fileList;
		
		return result;
	}
	
	
	public static class Result {
	
		public List<DownoadFile> downoadFiles;
		
	}

		
	

}



//