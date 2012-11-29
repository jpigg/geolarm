package csci498.jpigg.geolarm;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class GeoLarm extends ListActivity {
	
	public final static String ID_EXTRA = "csci498.jpigg.geolarm._ID";
	Cursor model;
	AlarmAdapter adapter = null;
	EditText name = null;
	EditText description = null;
	AlarmHelper helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geolarm);
        
        helper = new AlarmHelper(this);
        model = helper.getAll();
        startManagingCursor(model);
        adapter = new AlarmAdapter(model);
        setListAdapter(adapter);
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	
    	helper.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_geolarm, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getItemId() == R.id.add) {
    		startActivity(new Intent(GeoLarm.this, DetailForm.class));
    		return(true);
    	}
    	
    	return super.onOptionsItemSelected(item);
    }
    
    public void onListItemClick(ListView list, View view, int position, long id) {
    	Intent i = new Intent(GeoLarm.this, DetailForm.class);
    	
    	i.putExtra(ID_EXTRA, String.valueOf(id));
    	startActivity(i);
    }
    
	
	
	class AlarmAdapter extends CursorAdapter {
		
		AlarmAdapter(Cursor c) {
			super(GeoLarm.this, c);
		}
		
		@Override
		public void bindView(View row, Context ctxt, Cursor c) {
			AlarmHolder holder = (AlarmHolder)row.getTag();
			
			holder.populateFrom(c, helper);
		}
		
		@Override
		public View newView(Context ctxt, Cursor c, ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.row, parent, false);
			AlarmHolder holder = new AlarmHolder(row);
			row.setTag(holder);
			
			return row;
		}
		
	}
	
	static class AlarmHolder {
		private TextView name = null;
		private TextView description = null;
		
		AlarmHolder(View row) {
			name = (TextView)row.findViewById(R.id.name);
			description = (TextView)row.findViewById(R.id.description);
			
		}
		
		void populateFrom(Cursor c, AlarmHelper helper) {
			name.setText(helper.getName(c));
			description.setText(helper.getDescription(c));
		}
	}
}
