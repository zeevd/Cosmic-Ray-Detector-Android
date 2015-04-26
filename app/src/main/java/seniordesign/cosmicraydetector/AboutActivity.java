package seniordesign.cosmicraydetector;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;


public class AboutActivity extends ActionBarActivity{
    ///LOGGING TAG///
    private static final String TAG = "AboutActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    protected void onStop() {
        Log.w(TAG, "onStop was called. Terminating applicaiton");
        super.onStop();
        finish();
    }
}
