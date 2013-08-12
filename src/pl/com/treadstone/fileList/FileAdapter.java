package pl.com.treadstone.fileList;

import java.util.List;

import pl.com.treadstone.model.DownoadFile;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.filemanagerapp.R;

public class FileAdapter extends ArrayAdapter<DownoadFile>{

	
	private Context context;
	private List<DownoadFile> downoadFiles;
	
	public FileAdapter(Context context, int textViewResourceId,
			List<DownoadFile> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
      this.downoadFiles = objects;
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder view;
 
        if(convertView==null)
        {
            view = new ViewHolder();
 
        	LayoutInflater li = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        	convertView = li.inflate(R.layout.list_entry, parent, false);
        	
            view.title = (TextView)convertView.findViewById(R.id.entryTitle);
            convertView.setTag(view);
        }
        else
        {
            view = (ViewHolder) convertView.getTag();
        }
 
        view.title.setText(downoadFiles.get(position).filename);
        
        
        return convertView;
	}

	public static class ViewHolder
	{
		public TextView title;
	}
	
}
