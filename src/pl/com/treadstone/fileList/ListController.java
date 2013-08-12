package pl.com.treadstone.fileList;

import pl.com.treadstone.filemanagerapp.FileManagerApplication;
import pl.example.interfaces.Controller;

public class ListController implements Controller{

	
	private FileListFragment listFragment;
	
	
	public ListController(FileListFragment activity) {
		this.listFragment = activity;
		listFragment.setListener(fileListListener);
	}


	FileListFragment.FileListListener fileListListener = new FileListFragment.FileListListener() {
		
		@Override
		public void onLoadList() {
			FileManagerApplication.getInstance().getFileListManager().getFiles();
			
		}
	};
	
	@Override
	public void dispose() {
		if(listFragment != null)
			listFragment.setListener(null);

		
	}

}
