package pl.com.treadstone.database;

import android.provider.BaseColumns;

public final class FileManagerContract {

	public FileManagerContract(){}
	
	
	public static abstract class FileEntry implements BaseColumns{
		
		public static final String TABLE_NAME = "file";
		public static final String COLUMN_NAME_TITLE = "title";
		public static final String COLUMN_NAME_MIMETYPE = "mimetype";
		public static final String COLUMN_NAME_SIZE = "size";
		public static final String COLUMN_NAME_URL = "url";
		public static final String COLUMN_NAME_PATH = "path";
		public static final String COLUMN_NAME_STATUS = "status";
	}
	
}
