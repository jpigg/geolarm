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
	
	int intIsActive = 0;
	int intUseLocation = 0;
	
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
		location.setText(helper.getLocation(c));
		time.setCurrentHour(helper.getHour(c));
		time.setCurrentMinute(helper.getMinute(c));
		if(helper.getIsActive(c) == 1) {
			is_active.setChecked(true);
		}
		else {
			is_active.setChecked(false);
		}
		if(helper.getUseLocation(c) == 1) {
			use_location.setChecked(true);
		}
		else {
			use_location.setChecked(false);
		}
		
		c.close();
	}
	
	
	
	private View.OnClickListener onSave = new View.OnClickListener() {

		private void setCheckBoxes() {
			if(is_active.isChecked()) {
				intIsActive = 1;
			}
			else {
				intIsActive = 0;
			}
			if(use_location.isChecked()) {
				intUseLocation = 1;
			}
			else {
				intUseLocation = 0;
			}
		}
		//String name, String description, int is_active, int use_location, String location, int hour, int minute
		public void onClick(View v) {
			//Need to check for required info
			setCheckBoxes();
			if (alarmId == null) {
				helper.insert(name.getText().toString(),
						description.getText().toString(),
						intIsActive,
						intUseLocation,
						location.getText().toString(),
						time.getCurrentHour(),
						time.getCurrentMinute());
			}
			else {
				helper.update(alarmId,
						name.getText().toString(),
						description.getText().toString(),
						intIsActive,
						intUseLocation,
						location.getText().toString(),
						time.getCurrentHour(),
						time.getCurrentMinute());
			}
			finish();
		}
	};
	
}
