package csci498.jpigg.geolarm;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class GeoLarm extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geolarm);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_geolarm, menu);
        return true;
    }
}
