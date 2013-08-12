package pl.com.treadstone.filemanagerapp;

import tparys.signals.Signal;
import android.os.Bundle;
import android.util.Log;

public class FragmentActivity extends android.support.v4.app.FragmentActivity{

	protected FileManagerApplication application;
	protected String id;
		
	public Signal<String> onStartFailed = new Signal<String>();
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);       
        
        application = FileManagerApplication.getInstance();
        application.registerActivity(this);
        
        Log.e("", "!!!!!!!!!!!!! [" + this.getClass().getSimpleName() + " (method='onCreate')]");
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        application.registerActivity(this);
        Log.e("", "!!!!!!!!!!!!! [" + this.getClass().getSimpleName() + " (method='onStart')]");
    }	

    
    @Override
    protected void onResume(){
    	super.onResume();
    	Log.e("", "!!!!!!!!!!!!! [" + this.getClass().getSimpleName() + " (method='onResume')]");
    }
    
    @Override
    protected void onPause(){
    	super.onPause();
    	Log.e("", "!!!!!!!!!!!!! [" + this.getClass().getSimpleName() + " (method='onPause')]");
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        if (isChangingConfigurations() == false ) 
        	application.unregisterActivity(this);
        Log.e("", "!!!!!!!!!!!!! [" + this.getClass().getSimpleName() + " (method='onStop')]");
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	Log.e("", "!!!!!!!!!!!!! [" + this.getClass().getSimpleName() + " (method='onDestroy')]");
    }
	
}
