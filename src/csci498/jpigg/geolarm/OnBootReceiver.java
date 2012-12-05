package csci498.jpigg.geolarm;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

public class OnBootReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context ctxt, Intent intent) {
		
	}
	
	//needs to be called when the use alarm is checked, or the time is changed
	public static void setAlarm(Context ctxt, String alarmId) {
		AlarmManager mgr = (AlarmManager)ctxt.getSystemService(Context.ALARM_SERVICE);
		Calendar cal = Calendar.getInstance();
		
		AlarmHelper helper = new AlarmHelper(ctxt);
		Cursor c = helper.getById(alarmId);
		c.moveToFirst();
		
		//hour and minute should be passed in and set somehow..
		int hour = helper.getHour(c);
		int minute = helper.getMinute(c);
		
		cal.set(Calendar.HOUR, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		Log.i("GeoLarm", String.valueOf(hour));
		Log.i("GeoLarm", String.valueOf(minute));
		
		if(cal.getTimeInMillis() < System.currentTimeMillis()) {
			cal.add(Calendar.DAY_OF_YEAR, 1);
		}
		
		//may need to change based on whether the alarm is snoozed or dismissed..
		mgr.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, getPendingIntent(ctxt, alarmId));
		Log.i("GeoLarm", "finished setAlarm");
	}
	
	//Needs to be called when the use alarm is unchecked, or the time is changed
	public static void cancelAlarm(Context ctxt, String alarmId) {
		AlarmManager mgr = (AlarmManager)ctxt.getSystemService(Context.ALARM_SERVICE);
		
		mgr.cancel(getPendingIntent(ctxt, alarmId));
	}
	
	private static PendingIntent getPendingIntent(Context ctxt, String alarmId) {
		Intent i = new Intent(ctxt, OnAlarmReceiver.class);
		Log.i("GeoLarm", "returning pendingIntent with id=" + alarmId);
		return(PendingIntent.getBroadcast(ctxt, Integer.parseInt(alarmId) , i, 0));
	}

}
