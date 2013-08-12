package pl.com.treadstone.database;

import java.util.ArrayList;
import java.util.List;

import pl.com.treadstone.database.FileManagerContract.FileEntry;
import pl.com.treadstone.model.DownoadFile;
import pl.com.treadstone.utils.StringUtils;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.filemanagerapp.BuildConfig;

public class FileManagerHelper extends SQLiteOpenHelper {

	
	private static FileManagerHelper _instance = null;
	
	public static FileManagerHelper getInstance(Context context){
		
		if(_instance == null){
			_instance = new FileManagerHelper(context);
			return _instance;
		}
 
		return _instance;
	}
	
	private static final String FileManagerHelperTag = "FileManagerHelper";
	
	public FileManagerHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "db.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER = " INTEGER";
    private static final String NUMERIC = " NUMERIC";
    
    private static final String COMMA_SEP = ",";
    
    private static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + FileEntry.TABLE_NAME + " (" +
        		FileEntry._ID + " INTEGER PRIMARY KEY," +
        		
        		// nazwa pliku
        		FileEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
        		
        		// type filiku
        		FileEntry.COLUMN_NAME_MIMETYPE + TEXT_TYPE + COMMA_SEP +
        		
        		// rozmiar pliku
        		FileEntry.COLUMN_NAME_SIZE + INTEGER + COMMA_SEP +
        		
        		//url do pliku
        		FileEntry.COLUMN_NAME_URL + TEXT_TYPE + COMMA_SEP +
        		
        		// status pliku
        		FileEntry.COLUMN_NAME_STATUS + NUMERIC + COMMA_SEP +
        		
        		// sciezka do pliku
        		FileEntry.COLUMN_NAME_PATH + TEXT_TYPE + " );";

    private static final String SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS " + FileEntry.TABLE_NAME;
    
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db	, int oldVersion, int newVersion) {

		db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
		
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

	
	/**
	 * Sprawdza czy plik jest w bazie czy nie
	 * 
	 * @return
	 */
	public boolean checkFile(String fileName) {
		
		boolean exist = false;
		Cursor c = null;
		SQLiteDatabase db = null;
		try{
			db = getReadableDatabase();
			c = db.query(FileEntry.TABLE_NAME,null, null, null, null, null, null);
			
			if(c!=null){
    	   		if(c.moveToFirst()){
    	   			do{
    	   				if(fileName.equals(c.getString(c.getColumnIndex(FileEntry.COLUMN_NAME_TITLE))))
    	   					exist = true;
    	   				
    	   			} while (c.moveToNext());
    	   		}
    	   	}
			
		} catch (Exception exception){
			Log.e(FileManagerHelperTag, "Error: " + exception);
		} finally {
			if(c != null)
				if(!c.isClosed())
					c.close();
			if(db != null)
      		    db.close();
		}
		return exist;
	}
	
	
	public boolean putFileIntoDatabase(String url, String mimeType, long size, String path) {
		
		SQLiteDatabase db = null;
    	
		try {
    		db = getWritableDatabase();
			ContentValues newFileValue = new ContentValues();
			newFileValue.put(FileEntry.COLUMN_NAME_TITLE, StringUtils.getFileName(url));
			newFileValue.put(FileEntry.COLUMN_NAME_MIMETYPE, mimeType);
			newFileValue.put(FileEntry.COLUMN_NAME_PATH, path);
			newFileValue.put(FileEntry.COLUMN_NAME_SIZE, size);
			newFileValue.put(FileEntry.COLUMN_NAME_URL, url);
			db.insert(FileEntry.TABLE_NAME, null, newFileValue);
			return true;
		} catch (Exception e) {
			if(!BuildConfig.DEBUG)
				Log.e(FileManagerHelperTag, "Cannot put file into database. Error: " + e);
		} finally{
			if(db != null)
      		    db.close();
		}
		
		return false;
	}
	
	public List<DownoadFile> getDownloadFiles(){
		
		List<DownoadFile> list = new ArrayList<DownoadFile>();
		
		SQLiteDatabase db = this.getReadableDatabase();
    	
        Cursor cursor = db.query(FileEntry.TABLE_NAME, null, null, null, null, null, null);
        
        if (cursor != null){
            if(cursor.moveToFirst() && cursor.getCount() >= 1){
	            
            	DownoadFile downoadFile = new DownoadFile();
            	downoadFile.filename = cursor.getString(1);
            	downoadFile.mimetype = cursor.getString(2);
            	downoadFile.url = cursor.getString(4);
            	downoadFile.path = cursor.getString(6);
            	
	            list.add(downoadFile);
            }
            cursor.close();
        }
        
       
        closeDB(db);
        
        return list;
	}
	
	private void closeDB(SQLiteDatabase database){
		if(database.isOpen())
			database.close();
	}
}
