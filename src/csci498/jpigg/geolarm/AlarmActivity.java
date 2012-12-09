package csci498.jpigg.geolarm;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AlarmActivity extends Activity  implements MediaPlayer.OnPreparedListener {
	
	EditText alarmName = null;
	EditText alarmTime = null;
	MediaPlayer player = new MediaPlayer();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		long currentTime = System.currentTimeMillis();
		Log.i("GeoLarm", "Alarm activity was called at time " + currentTime);
		setContentView(R.layout.alarm_activity);
		
		Button dismiss = (Button)findViewById(R.id.dismiss);
		Button snooze = (Button)findViewById(R.id.snooze);
		
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
	
	private View.OnClickListener onDismiss = new View.OnClickListener() {

		public void onClick(View v) {
			//I don't think onDismiss needs to do anything since the alarm is already set to repeat the next day
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
			PendingIntent pendInt = PendingIntent.getBroadcast(AlarmActivity.this, -1 , i, 0);
			
			mgr.set(mgr.RTC_WAKEUP, cal.getTimeInMillis(), pendInt);
			
			Log.i("GeoLarm", "hit snooze");
			
			finish();
			
		}
		
	};
	

}
