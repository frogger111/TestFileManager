package pl.com.treadstone.filemanagerapp;

import pl.com.treadstone.chooseList.ChooseList;
import pl.com.treadstone.downloadFile.DownloadFileFragment;
import pl.com.treadstone.fileList.FileListFragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;

import com.example.filemanagerapp.R;

public class MainFragmentActivity extends FragmentActivity implements ChooseList.OnHeadlineSelectedListener {

	private MainListsner listsner;
	
	public void setListener(MainListsner listsner){
		this.listsner = listsner;
	}
	
	private MainController mainController;
	
	//YOU CAN EDIT THIS TO WHATEVER YOU WANT
    private static final int SELECT_PICTURE = 1;

    private String selectedImagePath;
    //ADDED
    private String filemanagerstring;
	
    private String mimetype ;
    
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (getSupportFragmentManager().findFragmentById(R.id.list_container) == null) {
            ChooseList list = new ChooseList();
            getSupportFragmentManager().beginTransaction().add(R.id.list_container, list).commit();
        }
		
		if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            // Create an instance of ExampleFragment
            DownloadFileFragment downloadFileFragment = new DownloadFileFragment();
            
            downloadFileFragment.setArguments(getIntent().getExtras());
            
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, downloadFileFragment, "download").commit();
        }
		
		
//		
		
//		
//		Button button = new Button(getApplicationContext());
//	
//		FrameLayout root = (FrameLayout) getWindow().getDecorView().getRootView();
//		
//		root.addView(button);
//		
//		button.setOnClickListener(new OnClickListener() {
//
//            public void onClick(View arg0) {
//
//                // in onCreate or any event where your want the user to
//                // select a file
//                Intent intent = new Intent();
//                intent.setType("audio/mpeg3");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,
//                        "Select Picture"), SELECT_PICTURE);
//            	
//            	
//          
//            }
//        });
//		
//		
//		mimetype = Util.getMimeType("http://sphotos-e.ak.fbcdn.net/hphotos-ak-ash4/1000224_10151831307004560_1234253533_n.jpg");
////		mimetype = Util.getMimeType("http://treadstone.com.pl/games/GalaxySaver.apk"); 
//		
//		Log.i("MIME Type", "" + mimetype);
		
		
		
		
		
//	    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/file");
//	    Intent intent = new Intent(Intent.ACTION_VIEW);
//	    intent.setDataAndType(Uri.fromFile(file),”application/file”);
//	    startActivity(intent);
	}


	@Override
	public void onStart(){
		super.onStart();
		
		mainController = new MainController(MainFragmentActivity.this);
		mainController.init();
	}
	
	@Override
	public void onStop(){
		super.onStop();
		
		if(mainController != null){
			mainController.dispose();
			mainController = null;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	public static interface MainListsner{
		
	}
	
	//UPDATED
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();

                //OI FILE Manager
                filemanagerstring = selectedImageUri.getPath();

                //MEDIA GALLERY
                selectedImagePath = getPath(selectedImageUri);

                //DEBUG PURPOSE - you can delete this if you want
                if(selectedImagePath!=null)
                    System.out.println(selectedImagePath);
                else System.out.println("selectedImagePath is null");
                if(filemanagerstring!=null)
                    System.out.println(filemanagerstring);
                else System.out.println("filemanagerstring is null");

                //NOW WE HAVE OUR WANTED STRING
                if(selectedImagePath!=null)
                    System.out.println("selectedImagePath is the right one for you!");
                else
                    System.out.println("filemanagerstring is the right one for you!");
            }
        }
    }

    //UPDATED!
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor!=null)
        {
            //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
    }



	@Override
	public void onPageSelected(int position) {
		if(position == 0){
			if (findViewById(R.id.fragment_container) != null) {

	            // Create an instance of ExampleFragment
	            FileListFragment fileListFragment = new FileListFragment();
	            
	            fileListFragment.setArguments(getIntent().getExtras());
	            
	            getSupportFragmentManager().beginTransaction()
	                    .replace(R.id.fragment_container, fileListFragment).commit();
	        }
			
		} else {
			if (findViewById(R.id.fragment_container) != null) {

	            // Create an instance of ExampleFragment
	            DownloadFileFragment downloadFileFragment = new DownloadFileFragment();
	            
	            downloadFileFragment.setArguments(getIntent().getExtras());
	            
	            getSupportFragmentManager().beginTransaction()
	                    .replace(R.id.fragment_container, downloadFileFragment).commit();
	        }
		}
		
	}
	
}
