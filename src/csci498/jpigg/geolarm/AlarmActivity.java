package csci498.jpigg.geolarm;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class AlarmActivity extends Activity {
	
	//This class will need an overhaul later
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("GeoLarm", "Alarm activity was called");
		setContentView(R.layout.alarm_activity);
	}

}
