package csci498.jpigg.geolarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AlarmHelper extends SQLiteOpenHelper{
	
	//schema version 1:: CREATETABLE = "CREATE TABLE alarms (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT);"
	//schema version 2:: CREATETABLE = "CREATE TABLE alarms (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, is_active INTEGER, time TEXT, use_location INTEGER, location TEXT);"
	
	private static final String GETALL = "SELECT _id,  name, description, is_active, use_location, location, hour, minute FROM alarms ORDER BY name";
	private static final String GETBYID = "SELECT _id,  name, description, is_active, use_location, location, hour, minute FROM alarms WHERE _ID=?";
	private static final String CREATETABLE = "CREATE TABLE alarms (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, is_active INTEGER, use_location INTEGER, location TEXT, hour INTEGER, minute INTEGER);";
	private static final String DATABASE_NAME = "alarms.db";
	private static final int SCHEMA_VERSION = 3;
	
	public AlarmHelper(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}
	
	public void insert(String name, String description, int is_active, int use_location, String location, int hour, int minute) {
		ContentValues cv = new ContentValues();
		
		cv.put("name", name);
		cv.put("description", description);
		cv.put("is_active", is_active);
		cv.put("use_location", use_location);
		cv.put("location", location);
		cv.put("hour", hour);
		cv.put("minute", minute);
		
		getWritableDatabase().insert("alarms", "name", cv);
	}
	
	public void update(String id, String name, String description, int is_active, int use_location, String location, int hour, int minute) {
		ContentValues cv = new ContentValues();
		String[] args = {id};
		
		cv.put("name", name);
		cv.put("description", description);
		cv.put("is_active", is_active);
		cv.put("use_location", use_location);
		cv.put("location", location);
		cv.put("hour", hour);
		cv.put("minute", minute);
		
		getWritableDatabase().update("alarms", cv, "_ID=?", args);
	}
	
	public void delete(String id) {
		
		String[] args = {id};
		
		getWritableDatabase().delete("alarms", "_ID=?", args);
	}
	
	public Cursor getAll() {
		return(getReadableDatabase().rawQuery(GETALL, null));
	}
	
	public String getName(Cursor c) {
		return(c.getString(1));
	}
	
	public String getDescription(Cursor c) {
		return(c.getString(2));
	}
	
	public int getIsActive(Cursor c) {
		return c.getInt(3);
	}
	
	public int getUseLocation(Cursor c) {
		return c.getInt(4);
	}
	
	public String getLocation(Cursor c) {
		return c.getString(5);
	}
	
	public int getHour(Cursor c) {
		return c.getInt(6);
	}
	
	public int getMinute(Cursor c) {
		return c.getInt(7);
	}
	
	public Cursor getById(String id) {
		String[] args = {id};
		
		return(getReadableDatabase().rawQuery(GETBYID, args));
	}
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATETABLE);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion < 2) {
			db.execSQL("ALTER TABLE alarms ADD COLUMN is_active INTEGER");
			db.execSQL("ALTER TABLE alarms ADD COLUMN time TEXT");
			db.execSQL("ALTER TABLE alarms ADD COLUMN use_location INTEGER");
			db.execSQL("ALTER TABLE alarms ADD COLUMN location TEXT");
		}
		//deleting time column to replace it with minute and hour columns
		if (oldVersion <3) {
			db.execSQL("DROP TABLE IF EXISTS alarms;");
			db.execSQL(CREATETABLE);
		}
		
	}

}
