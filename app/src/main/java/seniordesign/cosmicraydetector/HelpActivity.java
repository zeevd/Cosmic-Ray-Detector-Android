package seniordesign.cosmicraydetector;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

/**
 * Created by zeevd_000 on 3/29/2015.
 */
public class HelpActivity extends ActionBarActivity {
    ///LOGGING TAG///
    private static final String TAG = "HelpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }

    @Override
    protected void onStop() {
        Log.w(TAG, "onStop was called. Terminating applicaiton");
        super.onStop();
        finish();
    }
}
