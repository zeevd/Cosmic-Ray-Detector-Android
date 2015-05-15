package seniordesign.cosmicraydetector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cengalabs.flatui.FlatUI;
import com.dropbox.sync.android.DbxAccountManager;

import seniordesign.cosmicraydetector.MainActivity;
import seniordesign.cosmicraydetector.R;

public class DropboxActivity extends ActionBarActivity {
    ///LOGGING TAG///
    private static final String TAG = "DropboxActivity";

    private DbxAccountManager mDbxAcctMgr;

    private TextView dropboxStatus;
    private TextView linkedAccount;
    private Button buttonLinkDropbox;
    private final static int dropboxRequestCode=100;

    //TODO: LOGGING

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Initializing DropboxActivity.java");
        super.onCreate(savedInstanceState);

        Log.i(TAG, "Init FlatUI");
        FlatUI.initDefaultValues(this);
        FlatUI.setDefaultTheme(FlatUI.BLOOD);
        setContentView(R.layout.activity_dropbox);
        getSupportActionBar().setBackgroundDrawable(FlatUI.getActionBarDrawable(this, FlatUI.BLOOD, false, 2));


        init();
        updateLinkStatus();
    }

    public void init(){
        //Init account manager
        mDbxAcctMgr = MainActivity.mDbxAcctMgr;
        //Init View components
        dropboxStatus = (TextView) findViewById(R.id.textview_dropbox_status);
        linkedAccount = (TextView) findViewById(R.id.textview_linked_account);
        buttonLinkDropbox = (Button) findViewById(R.id.button_link_dropbox);
    }

    public boolean isLinked(){
        return mDbxAcctMgr.hasLinkedAccount();
    }

    private void updateLinkStatus(){
        if (isLinked()){
            dropboxStatus.setText("LINKED");
            dropboxStatus.setTextColor(Color.GREEN);
            linkedAccount.setText(mDbxAcctMgr.getLinkedAccount().getAccountInfo().displayName + "   " + mDbxAcctMgr.getLinkedAccount().toString());
            buttonLinkDropbox.setText("Link to New Dropbox");
        }
        else{
            dropboxStatus.setText("NOT LINKED");
            dropboxStatus.setTextColor(Color.RED);
            linkedAccount.setText("None");
            buttonLinkDropbox.setText("Link Dropbox");
        }
    }


    public void onClickRefreshLinkStatus(View view) {
        updateLinkStatus();
        Toast.makeText(this,"Dropbox status refreshed", Toast.LENGTH_SHORT).show();
    }

    public void onClickLinkDropbox(View view) {
        mDbxAcctMgr.startLink((Activity) this, dropboxRequestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == dropboxRequestCode) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Dropbox account linked successfully", Toast.LENGTH_LONG).show();
                updateLinkStatus();
            } else {
                Toast.makeText(this, "Failed to link Dropbox account", Toast.LENGTH_LONG).show();
                updateLinkStatus();
              }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
