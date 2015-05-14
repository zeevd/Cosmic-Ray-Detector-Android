package seniordesign.cosmicraydetector;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.cengalabs.flatui.FlatUI;

/**
 * Created by zeevd_000 on 3/29/2015.
 */
public class HelpActivity extends ActionBarActivity {
    ///LOGGING TAG///
    private static final String TAG = "HelpActivity";

    //TODO: LOGGING

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Initializing HelpActivity.java");
        super.onCreate(savedInstanceState);

        Log.i(TAG, "Init FlatUI");
        FlatUI.initDefaultValues(this);
        FlatUI.setDefaultTheme(FlatUI.BLOOD);
        setContentView(R.layout.activity_help);
        getSupportActionBar().setBackgroundDrawable(FlatUI.getActionBarDrawable(this, FlatUI.BLOOD, false, 2));
    }

}
