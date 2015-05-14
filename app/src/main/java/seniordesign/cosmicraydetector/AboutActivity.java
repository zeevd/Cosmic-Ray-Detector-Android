package seniordesign.cosmicraydetector;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.cengalabs.flatui.FlatUI;


public class AboutActivity extends ActionBarActivity{
    ///LOGGING TAG///
    private static final String TAG = "AboutActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Initializing AboutActivity.java");
        super.onCreate(savedInstanceState);

        Log.i(TAG, "Init FlatUI");
        FlatUI.initDefaultValues(this);
        FlatUI.setDefaultTheme(FlatUI.BLOOD);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setBackgroundDrawable(FlatUI.getActionBarDrawable(this, FlatUI.BLOOD, false, 2));


    }

    //TODO: LOGGING

}
