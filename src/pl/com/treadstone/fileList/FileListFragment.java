package pl.com.treadstone.fileList;

import pl.com.treadstone.fileList.FilesLoader.Result;
import pl.com.treadstone.filemanagerapp.FileManagerApplication;
import tparys.signals.SignalListener;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.filemanagerapp.R;

public class FileListFragment extends Fragment{

	public static interface FileListListener{
		public void onLoadList();
	}
	
	private FileListListener listener;
	
	public void setListener(FileListListener listener){
		this.listener = listener;
	}
	
	private ListController controller;
	private ListView fileListView;
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        
    }
    
	
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
       
        
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.file_list_fragment, container, false);
		
		controller = new ListController(this);
		
		
		
		fileListView = (ListView)view.findViewById(R.id.fileListView);
				
		listener.onLoadList();
		
		FileManagerApplication.getInstance().getFileListManager().onLoadFiles.add(new SignalListener<FilesLoader.Result>() {
			
			@Override
			public void onSignal(Result result) {
				loadList(result);
				
			}
		});
		
		return view;
    }
    
    protected void loadList(final Result result) {
    	FileAdapter fileAdapter = new FileAdapter(getActivity(), R.layout.list_entry, result.downoadFiles);
    	
    	fileListView.setAdapter(fileAdapter);
    	
    	fileListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,int position, long id) {
				Intent intent = new Intent();
				intent.setType(result.downoadFiles.get(position).mimetype);
				intent.setDataAndType(Uri.parse("file:/" + result.downoadFiles.get(position).path), result.downoadFiles.get(position).mimetype);
				getActivity().startActivityForResult(intent, 0);

			}
		});
		
	}


	@Override
    public void onDestroy(){
    	super.onDestroy();
    	
    	FileManagerApplication.getInstance().getFileListManager().onLoadFiles.removeAll();
    	
    	if(controller != null)
    		controller.dispose();
    }
}
