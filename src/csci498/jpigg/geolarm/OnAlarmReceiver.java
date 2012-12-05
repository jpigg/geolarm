package csci498.jpigg.geolarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OnAlarmReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context ctxt, Intent intent) {
		Log.i("GeoLarm", "made it to OnAlarmReceiver");
		Intent i = new Intent(ctxt, AlarmActivity.class);
		
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		ctxt.startActivity(i);
	}

}