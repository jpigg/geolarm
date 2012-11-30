package csci498.jpigg.geolarm;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;

public class DetailForm extends Activity {

	EditText name = null;
	EditText description = null;
	CheckBox is_active = null;
	TimePicker time = null;
	CheckBox use_location = null;
	EditText location = null;
	AlarmHelper helper = null;
	String alarmId = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.detail_form);
		
		helper = new AlarmHelper(this);
		
		name = (EditText)findViewById(R.id.name);
		description = (EditText)findViewById(R.id.description);
		location = (EditText)findViewById(R.id.location);
		is_active = (CheckBox)findViewById(R.id.is_active);
		time = (TimePicker)findViewById(R.id.time);
		use_location = (CheckBox)findViewById(R.id.use_location);
		
		Button save = (Button)findViewById(R.id.save);
		
		save.setOnClickListener(onSave);
		
		alarmId = getIntent().getStringExtra(GeoLarm.ID_EXTRA);
		
		if(alarmId!=null) {
			load();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		helper.close();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_detail_form, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getItemId() == R.id.delete) {
    		if(alarmId == null) {
    			//the alarm isn't in the database, basically the same as cancel
    		}
    		else {
    			//alarm is in the database
    			helper.delete(alarmId);
    		}
    		finish();
    		return(true);
    	}
    	
    	return super.onOptionsItemSelected(item);
    }
	
	private void load() {
		Cursor c = helper.getById(alarmId);
		
		c.moveToFirst();
		name.setText(helper.getName(c));
		description.setText(helper.getDescription(c));
		time.setCurrentHour(14);
		time.setCurrentMinute(35);
		
		c.close();
	}
	
	private View.OnClickListener onSave = new View.OnClickListener() {
		
		public void onClick(View v) {
			//Need to check for required info
			if (alarmId == null) {
				//helper.insert(name.getText().toString(), description.getText().toString());
			}
			else {
				//helper.update(alarmId, name.getText().toString(), description.getText().toString());
			}
			finish();
		}
	};
	
}
