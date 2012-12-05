package csci498.jpigg.geolarm;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class AlarmActivity extends Activity {
	
	//This class will need an overhaul later
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		long currentTime = System.currentTimeMillis();
		Log.i("GeoLarm", "Alarm activity was called at time " + currentTime);
		setContentView(R.layout.alarm_activity);
	}

}
