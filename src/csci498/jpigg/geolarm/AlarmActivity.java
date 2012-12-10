package csci498.jpigg.geolarm;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AlarmActivity extends Activity  implements MediaPlayer.OnPreparedListener {
	
	TextView alarmName = null;
	TextView alarmTime = null;
	MediaPlayer player = new MediaPlayer();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_activity);
		
		alarmName = (TextView)findViewById(R.id.alarm_name);
		alarmTime = (TextView)findViewById(R.id.alarm_time);
		Button dismiss = (Button)findViewById(R.id.dismiss);
		Button snooze = (Button)findViewById(R.id.snooze);
		
		String name = "null";
		String id = getIntent().getStringExtra(OnBootReceiver.ID_EXTRA);
		if(id.equals("-1")) {
			//Snoozed alarm
			name = getResources().getString(R.string.snooze_alarm_name);
		}
		else if (id.equals("-2")) {
			//Forced fake alarm
			name = "Fake Alarm";
		}
		else {
			AlarmHelper helper = new AlarmHelper(this);
			Cursor c = helper.getById(id);
			c.moveToFirst();
			name = helper.getName(c);
			c.close();
		}
		
		alarmTime.setText(buildTimeString());
		alarmName.setText(name);
		dismiss.setOnClickListener(onDismiss);
		snooze.setOnClickListener(onSnooze);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String sound = prefs.getString("alarm_ringtone", null);
		
		if(sound != null) {
			player.setAudioStreamType(AudioManager.STREAM_ALARM);
			
			try {
				player.setDataSource(sound);
				player.setOnPreparedListener(this);
				player.prepareAsync();
			} catch (Exception e) {
				Log.e("GeoLarm", "Error in playing the ringtone", e);
			}
		}
	}
	
	@Override
	public void onPause() {
		if(player.isPlaying()) {
			player.stop();
		}
		
		super.onPause();
	}
	
	public void onPrepared(MediaPlayer player) {
		player.start();
	}
	
	String buildTimeString() {
		StringBuilder builder = new StringBuilder();
		
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		
		if(hour < 12) {
			if(hour == 0) {
				builder.append("12");
			}
			else {
				builder.append(hour);
			}
		}
		else if(hour == 12) {
			builder.append("12");
		}
		else {
			builder.append(hour-12);
		}
		builder.append(":");
		if(minute < 10) {
			builder.append("0");
		}
		builder.append(minute);
		
		if(hour < 12) {
			builder.append("AM");
		}
		else {
			builder.append("PM");
		}
		
		return builder.toString();
	}
	
	private View.OnClickListener onDismiss = new View.OnClickListener() {

		public void onClick(View v) {
			//Dismisses the alarm, doesn't perform any actions
			finish();
		}
	};
	
	private View.OnClickListener onSnooze = new View.OnClickListener() {

		public void onClick(View v) {
			AlarmManager mgr = (AlarmManager)AlarmActivity.this.getSystemService(Context.ALARM_SERVICE);
			Calendar cal = Calendar.getInstance();
			
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AlarmActivity.this);
			int snoozeLength = Integer.parseInt(prefs.getString("snooze_length", "10"));
			
			int minute = cal.get(Calendar.MINUTE);
			
			minute += snoozeLength;
			
			cal.set(Calendar.MINUTE, minute);
			
			Intent i = new Intent(AlarmActivity.this, OnAlarmReceiver.class);
			i.putExtra(OnBootReceiver.ID_EXTRA, "-1");
			PendingIntent pendInt = PendingIntent.getBroadcast(AlarmActivity.this, -1 , i, 0);
			
			mgr.set(mgr.RTC_WAKEUP, cal.getTimeInMillis(), pendInt);
			
			//Should add a notification that can dismiss the pending snoozed alarm (id = -1)
			
			finish();
		}
	};
	
}
