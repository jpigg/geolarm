package csci498.jpigg.geolarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class OnAlarmReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context ctxt, Intent intent) {
		Intent i = new Intent(ctxt, AlarmActivity.class);
		
		Bundle b = intent.getExtras();
		String id = b.getString(OnBootReceiver.ID_EXTRA);
		i.putExtra(OnBootReceiver.ID_EXTRA, id);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		ctxt.startActivity(i);
	}

}
