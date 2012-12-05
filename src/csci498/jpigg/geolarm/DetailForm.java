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
import android.util.Log;

public class DetailForm extends Activity {

	EditText name = null;
	EditText description = null;
	CheckBox is_active = null;
	TimePicker time = null;
	CheckBox use_location = null;
	EditText location = null;
	AlarmHelper helper = null;
	String alarmId = null;
	
	int intWasActive = 0;
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
	
	//called when the use alarm checkbox is clicked
	public void onActivateClick(View view) {
		//not sure if i'll need this or not
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
			intWasActive = 1;
		}
		else {
			is_active.setChecked(false);
			intWasActive = 0;
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
		
		//sets or cancels alarms as needed
		private void doAlarmChecks(Alarm alarm) {
			//Need to call onBootReceiver alarm methods here based on intIsActive and intWasActive
			//if intWasActive == -1 then there was no alarm to load, ie it's a newly created alarm
			//not sure what to do yet if alarmId == null to use for the id for the pending intent in onBootReceiver
			
			if(intWasActive == 1)
			{
				OnBootReceiver.cancelAlarm(DetailForm.this, alarm.getAlarmId());
				if(intIsActive == 1) {
					OnBootReceiver.setAlarm(DetailForm.this, alarm.getAlarmId());
				}
			}
			else { // intWasActive == 0
				
				if(intIsActive == 1) {
					OnBootReceiver.setAlarm(DetailForm.this, alarm.getAlarmId());
				}
			}
			
			
			/*
			//alarm was previously off
			if(intWasActive == 0) {
				//alarm needs to be set
				if(intIsActive == 1) {
					//turning alarm from off to on
					OnBootReceiver.setAlarm(DetailForm.this, alarm.getAlarmId());
				}
				else {
					//alarm stays off
				}
			}
			//alarm was previously on
			else if (intWasActive == 1) {
				if(intIsActive == 0) {
					//turning alarm from on to off
					OnBootReceiver.cancelAlarm(DetailForm.this, alarm.getAlarmId());
				}
				else {
					//alarm stays on
				}
			}
			//alarm is new
			else {
				if(intIsActive == 1) {
					//alarm needs to be set
					OnBootReceiver.setAlarm(DetailForm.this, alarm.getAlarmId());
				}
				
			}
			*/
			
		}
		
		//String name, String description, int is_active, int use_location, String location, int hour, int minute
		public void onClick(View v) {
			//Need to check for required info
			setCheckBoxes();
			
			Alarm alarm = new Alarm();
			
			if (alarmId == null) {
				alarmId = helper.insert(name.getText().toString(),
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
			
			alarm.setName(name.getText().toString());
			alarm.setDescription(description.getText().toString());
			alarm.setIsActive(intIsActive);
			alarm.setUseLocation(intUseLocation);
			alarm.setHour(time.getCurrentHour());
			alarm.setMinute(time.getCurrentMinute());
			alarm.setLocation(location.getText().toString());
			alarm.setAlarmId(alarmId);
			
			doAlarmChecks(alarm);
			
			finish();
		}
	};
	
}
