package pl.com.treadstone.downloadFile;


import pl.com.treadstone.downloadFile.CheckFileTask.Result;
import pl.com.treadstone.filemanagerapp.FileManagerApplication;
import tparys.download.DownloadManager;
import tparys.download.DownloadRequestStatus;
import tparys.signals.SignalListener;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filemanagerapp.R;

public class DownloadFileFragment extends Fragment{

	
	public static interface DownloadFileListener{
		public void onCheckClick(String url);
		public void onDownloadClick(String url);
		public void pause();
		public void resume();
		public void clearAfterCrash();
		public DownloadRequestStatus wasCrashed();
	}
	
	private DownloadFileListener listener;
	public void setListsner(DownloadFileListener listener){
		this.listener = listener;
	}
	
	private EditText urlEditText;
	private Button downloadButton;
	private ProgressBar progressBar;
	private SeekBar seekBar;
	private TextView progressText;
	
	private Button resume, pause;
	
	private DownloadFileController controller ;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
		
		controller = new DownloadFileController(this);
		
		View view = inflater.inflate(R.layout.download_fragment, container, false);
		
		urlEditText = (EditText)view.findViewById(R.id.editText1);
		downloadButton = (Button)view.findViewById(R.id.button1);
		progressBar = (ProgressBar)view.findViewById(R.id.progressBar1);
		seekBar = (SeekBar)view.findViewById(R.id.seekBar1);
		progressBar.setVisibility(View.INVISIBLE);
		seekBar.setVisibility(View.INVISIBLE);
		progressText = (TextView)view.findViewById(R.id.textView2);
		progressText.setVisibility(View.INVISIBLE);
		
		resume = (Button)view.findViewById(R.id.resume);
		resume.setVisibility(View.INVISIBLE);
		pause = (Button)view.findViewById(R.id.pause);
		pause.setVisibility(View.INVISIBLE);
		
		resume.setOnClickListener(resumeListener);
		pause.setOnClickListener(pauseListsner);
		
		downloadButton.setOnClickListener(downloadListener);
		
        return view;
    }
	
	@Override
	public void onStart(){
		super.onStart();
		
		DownloadRequestStatus downloadRequestStatus = listener.wasCrashed();
		if(downloadRequestStatus != null)
			if(downloadRequestStatus == DownloadRequestStatus.RUNNING){
				listener.clearAfterCrash();
				resetWidgets();
			}
		
		FileManagerApplication.getInstance().getDownloadFileManager().onCheckFileSignal.add(new SignalListener<CheckFileTask.Result>() {
			
			@Override
			public void onSignal(final Result result) {
				if(result.status == Result.Status.BEGIN_DOWNLOAD)
					showWidgets();
				else if(result.status == Result.Status.EXIST_IN_DATABASE){
					
					// plik istnieje w bazie danych, czy naspisaç
					final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create(); //Read Update
					alertDialog.setTitle(getActivity().getResources().getString(R.string.overide_file));
					
					alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getActivity().getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							listener.onDownloadClick(result.url);
							
							showWidgets();
							alertDialog.dismiss();
							
						}
					});
					
					alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getActivity().getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							alertDialog.dismiss();
							resetWidgets();
						}
					});
					
					alertDialog.show();
					
				} else if(result.status == Result.Status.INTERNET_CONNECTION_PROBLEM){
					
					//problem z po∏aczeniem internetowym
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.connection_problem), Toast.LENGTH_SHORT).show();
				} else if(result.status == Result.Status.INVALID_MIMETYPE){
					
					//niew∏aÊciwy typ mime pliku
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.invalid_mimetype), Toast.LENGTH_SHORT).show();
				} else if(result.status == Result.Status.INVALID_URL){
					
					//b∏´dny url
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.invalid_url), Toast.LENGTH_SHORT).show();
				} else if(result.status == Result.Status.CANCELED){
					
					//rozpoczynam pobieranie
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.canceled), Toast.LENGTH_SHORT).show();
				}
				resetWidgets();
			}
		});
		
		FileManagerApplication.getInstance().getDownloadFileManager().onDownlaodFile.add(new SignalListener<DownloadRequestStatus>() {
			
			@Override
			public void onSignal(DownloadRequestStatus status) {
				if(status == DownloadRequestStatus.SUCCESS){
					Toast.makeText(getActivity(), "Plik pobrano", Toast.LENGTH_SHORT).show();
					urlEditText.setText("");
					resetWidgets();
				} else if(status == DownloadRequestStatus.FAILED){
					failed(urlEditText.getText() + "");
				}  else if(status == DownloadRequestStatus.UNKONWN_HOST_EXCEPTION){
					unknownHost();
				}
				
			}
		});
		
		FileManagerApplication.getInstance().getDownloadFileManager().onPogress.add(new SignalListener<Float>() {
			
			@Override
			public void onSignal(Float progress) {
				
				publishProgress((int)((float)progress));
				
			}
		});
		
		FileManagerApplication.getInstance().getDownloadFileManager().resetSignal.add(new SignalListener<Boolean>() {
			
			@Override
			public void onSignal(Boolean signal) {
				resetWidgets();
				
			}
		});
	}
	
	protected void unknownHost() {
		final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(getActivity().getResources().getString(R.string.unknown_host));

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getActivity().getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				resetWidgets();
				alertDialog.dismiss();
				
			}
		});
        
        alertDialog.show();
		
	}
	
	private void setWidgets() {
		if(urlEditText.isEnabled())
			urlEditText.setEnabled(false);
		if(downloadButton.isEnabled())
			downloadButton.setEnabled(false);
		pause.setVisibility(View.VISIBLE);
		resume.setVisibility(View.INVISIBLE);
		seekBar.setVisibility(View.VISIBLE);
		seekBar.setProgress(DownloadManager.getInstance(getActivity()).getDownlaodRequestProgress());
		progressBar.setVisibility(View.INVISIBLE);
	}

	private void failed(final String url){
		final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create(); //Read Update
        alertDialog.setTitle(getActivity().getResources().getString(R.string.download_interupted));

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getActivity().getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.onCheckClick(url);
				
				showWidgets();
				alertDialog.dismiss();
				
			}
		});
        
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getActivity().getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
        	
        	@Override
        	public void onClick(DialogInterface dialog, int which) {
        		alertDialog.dismiss();
        		resetWidgets();
        	}
        });
        
        alertDialog.show();
	}
	
	private void publishProgress(int progress){
		
		if(pause.getVisibility() != View.VISIBLE)
			pause.setVisibility(View.VISIBLE);
			
		if(downloadButton.isEnabled())
			downloadButton.setEnabled(false);
		if(urlEditText.isEnabled())
			urlEditText.setEnabled(false);
		
		progressBar.setVisibility(View.VISIBLE);
		seekBar.setVisibility(View.VISIBLE);
		progressText.setVisibility(View.VISIBLE);
		seekBar.setProgress(progress);
		progressText.setText(progress + " % / 100%");
		
		if(progress >= 100)
			resetWidgets();
	}
	
	private void resetWidgets(){
		progressBar.setVisibility(View.INVISIBLE);
		seekBar.setProgress(0);
		seekBar.setVisibility(View.INVISIBLE);
		progressText.setVisibility(View.INVISIBLE);
		downloadButton.setEnabled(true);
		urlEditText.setEnabled(true);
		pause.setVisibility(View.INVISIBLE);
		resume.setVisibility(View.INVISIBLE);
	}
	
	private void showWidgets(){
		progressBar.setVisibility(View.VISIBLE);
		seekBar.setProgress(0);
		seekBar.setVisibility(View.VISIBLE);
		downloadButton.setEnabled(false);
	}
	
	@Override
	public void onStop(){
		super.onStop();

		// usuwam nas∏uchy w fragmencie
		FileManagerApplication.getInstance().getDownloadFileManager().onCheckFileSignal.removeAll();
		FileManagerApplication.getInstance().getDownloadFileManager().onDownlaodFile.removeAll();
		FileManagerApplication.getInstance().getDownloadFileManager().onPogress.removeAll();
	}
	
	@Override
	public void onDestroyView (){
		super.onDestroyView();
		
		
		if(controller != null){
			controller.dispose();
			controller = null;
		}
		
	}
	
	OnClickListener downloadListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			listener.onCheckClick(urlEditText.getText().toString());
		}
	};
	
	
	OnClickListener pauseListsner = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			listener.pause();
			pause.setVisibility(View.INVISIBLE);
			resume.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.INVISIBLE);
			
		}
	};
	
	OnClickListener resumeListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			listener.resume();
			pause.setVisibility(View.VISIBLE);
			resume.setVisibility(View.INVISIBLE);
			progressBar.setVisibility(View.VISIBLE);
			
		}
	};
	
}
