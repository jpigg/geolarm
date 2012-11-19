package csci498.jpigg.geolarm;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AlarmList extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_alarm_list, menu);
        return true;
    }
}
