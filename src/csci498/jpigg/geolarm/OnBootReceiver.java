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
	
	public final static String ID_EXTRA = "csci498.jpigg.geolarm._ID";
	
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
		
		int hour = helper.getHour(c);
		int minute = helper.getMinute(c);
		
		c.close();
		
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		Log.i("GeoLarm", String.valueOf("Setting an alarm at " + hour + ":" + minute +"."));
		
		if(cal.getTimeInMillis() < System.currentTimeMillis()) {
			cal.add(Calendar.DAY_OF_YEAR, 1);
		}
		
		mgr.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, getPendingIntent(ctxt, alarmId));
	}
	
	//Needs to be called when the use alarm is unchecked, or the time is changed
	public static void cancelAlarm(Context ctxt, String alarmId) {
		AlarmManager mgr = (AlarmManager)ctxt.getSystemService(Context.ALARM_SERVICE);
		
		mgr.cancel(getPendingIntent(ctxt, alarmId));
	}
	
	//returns the pending intent based of the alarmId
	private static PendingIntent getPendingIntent(Context ctxt, String alarmId) {
		Intent i = new Intent(ctxt, OnAlarmReceiver.class);
		i.putExtra(ID_EXTRA, alarmId);
		return(PendingIntent.getBroadcast(ctxt, Integer.parseInt(alarmId) , i, 0));
	}

}
