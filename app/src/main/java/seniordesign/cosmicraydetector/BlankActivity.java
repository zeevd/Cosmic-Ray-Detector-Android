package seniordesign.cosmicraydetector;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

//Dummy activity, used only for its positive result

//TODO: LOGGING

public class BlankActivity extends ActionBarActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(Activity.RESULT_OK);
        finish();
    }
}
