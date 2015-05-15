package seniordesign.cosmicraydetector;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cengalabs.flatui.FlatUI;


public class ContactActivity extends ActionBarActivity{
    ///LOGGING TAG///
    private static final String TAG = "ContactActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Initializing ContactActivity.java");
        super.onCreate(savedInstanceState);

        Log.i(TAG, "Init FlatUI");
        FlatUI.initDefaultValues(this);
        FlatUI.setDefaultTheme(FlatUI.BLOOD);
        setContentView(R.layout.activity_contact);
        getSupportActionBar().setBackgroundDrawable(FlatUI.getActionBarDrawable(this, FlatUI.BLOOD, false, 2));


    }

    void popupEmailAddress(String emailAddress, String name){
        Log.i(TAG, "Showing AlertDialog for " + name + ": " + emailAddress);
        final TextView message = new TextView(this);
        final SpannableString s = new SpannableString(emailAddress);
        Linkify.addLinks(s, Linkify.EMAIL_ADDRESSES);//Make address clickable
        message.setText(s);
        message.setTextSize(26);
        message.setMovementMethod(LinkMovementMethod.getInstance());

        new AlertDialog.Builder(this)
                .setTitle(name)
                .setCancelable(true)
                .setIcon(android.R.drawable.ic_dialog_email)
                .setView(message)
                .show();

    }


    public void onClickHelio(View view){
        Log.i(TAG, "onClickHelio Called");
        popupEmailAddress("takai@bnl.gov", "Dr. Helio Takai");
    }

    public void onClickMonica(View view) {
        Log.i(TAG, "onClickMonica Called");
        popupEmailAddress("monica.bugallo@stonybrook.edu", "Dr. Monica F. Bugallo");
    }

    public void onClickZeev(View view) {
        Log.i(TAG, "onClickZeev Called");
        popupEmailAddress("zeev.douek@gmail.com", "Zeev Douek");
    }

    public void onClickTochukwu(View view) {
        Log.i(TAG, "onClickTochukwu Called");
        popupEmailAddress("Takujuo@gmail.com", "Tochukwu Akujuo");
    }

    public void onClickSaket(View view) {
        Log.i(TAG, "onClickSaket Called");
        popupEmailAddress("Atisaket@gmail.com", "Saket Ati");
    }


}
