package pl.com.treadstone.filemanagerapp;


import android.content.Context;
import pl.com.treadstone.filemanagerapp.MainFragmentActivity.MainListsner;
import pl.example.interfaces.Controller;

public class MainController implements Controller {

	
	Context context;
	MainFragmentActivity activity;
	
	
	public MainController(MainFragmentActivity activity){
		context = activity;
		activity.setListener(mainListsner);
		
	}


	public void init() {
		FileManagerApplication.getInstance().getDownloadFileManager().resume();
		
	}
	
	MainListsner mainListsner = new MainListsner() {
		
	};


	@Override
	public void dispose() {
		if(activity != null)
			activity.setListener(null);
		
	}
	
}
