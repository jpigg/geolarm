package csci498.jpigg.geolarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AlarmHelper extends SQLiteOpenHelper{
	
	private static final String GETALL = "SELECT _id,  name, description FROM alarms ORDER BY name";
	private static final String GETBYID = "SELECT _id,  name, description FROM alarms WHERE _ID=?";
	private static final String CREATETABLE = "CREATE TABLE alarms (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT);";
	
	private static final String DATABASE_NAME = "alarms.db";
	private static final int SCHEMA_VERSION = 1;
	
	public AlarmHelper(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}
	
	public void insert(String name, String description) {
		ContentValues cv = new ContentValues();
		
		cv.put("name", name);
		cv.put("description", description);
		
		getWritableDatabase().insert("alarms", "name", cv);
	}
	
	public void update(String id, String name, String description) {
		ContentValues cv = new ContentValues();
		String[] args = {id};
		
		cv.put("name", name);
		cv.put("description", description);
		
		getWritableDatabase().update("alarms", cv, "_ID=?", args);
	}
	
	public void delete(String id) {
		
		String[] args = {id};
		
		getWritableDatabase().delete("alarms", "_ID=?", args);
	}
	
	public Cursor getAll()
	{
		return(getReadableDatabase().rawQuery(GETALL, null));
	}
	
	public String getName(Cursor c)
	{
		return(c.getString(1));
	}
	
	public String getDescription(Cursor c)
	{
		return(c.getString(2));
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
		//Only one version so far
		
	}

}
