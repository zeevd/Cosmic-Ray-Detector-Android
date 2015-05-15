package seniordesign.cosmicraydetector;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

//Dummy activity, used only for its positive result
public class BlankActivity extends ActionBarActivity{
    ///LOGGING TAG///
    private static final String TAG = "BlankActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Initializing BlankActivity.java");
        super.onCreate(savedInstanceState);
        setResult(Activity.RESULT_OK);
        finish();
    }
}
