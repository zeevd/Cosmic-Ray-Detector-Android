package seniordesign.cosmicraydetector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import com.cengalabs.flatui.FlatUI;

public class AboutActivity extends ActionBarActivity {
    ///LOGGING TAG///
    private static final String TAG = "AboutActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Initializing AboutActivity.java");
        super.onCreate(savedInstanceState);

        Log.i(TAG, "Init FlatUI");
        FlatUI.initDefaultValues(this);
        FlatUI.setDefaultTheme(FlatUI.BLOOD);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setBackgroundDrawable(FlatUI.getActionBarDrawable(this, FlatUI.BLOOD, false, 2));
    }

    public void onClickContactUs(View view) {
        Log.i(TAG, "Launching ContactActivity.java");
        Intent myIntent = new Intent(this, ContactActivity.class);
        this.startActivity(myIntent);
    }
}
