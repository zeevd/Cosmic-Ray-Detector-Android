package seniordesign.cosmicraydetector;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cengalabs.flatui.FlatUI;
import com.dropbox.sync.android.DbxAccountManager;

public class DropboxActivity extends ActionBarActivity {
    ///LOGGING TAG///
    private static final String TAG = "DropboxActivity";

    private DbxAccountManager mDbxAcctMgr;

    private TextView dropboxStatus;
    private TextView linkedAccount;
    private Button buttonLinkDropbox;

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
        Log.i(TAG, "Getting Dropbox account manager from MainActivity");
        //Init account manager
        mDbxAcctMgr = MainActivity.mDbxAcctMgr;
        Log.i(TAG, "Initializing DropboxActivity View objects");
        //Init View components
        dropboxStatus = (TextView) findViewById(R.id.textview_dropbox_status);
        linkedAccount = (TextView) findViewById(R.id.textview_linked_account);
        buttonLinkDropbox = (Button) findViewById(R.id.button_link_dropbox);
    }

    public boolean isLinked(){
        Log.i(TAG, "isLinked called");
        return mDbxAcctMgr.hasLinkedAccount();
    }

    private void updateLinkStatus(){
        Log.i(TAG,"Updating Dropbox link status");
        if (isLinked()){
            Log.i(TAG, "Dropbox is LINKED");
            dropboxStatus.setText("LINKED");
            dropboxStatus.setTextColor(Color.GREEN);
            linkedAccount.setText(mDbxAcctMgr.getLinkedAccount().getAccountInfo().displayName + "   " + mDbxAcctMgr.getLinkedAccount().toString());
            buttonLinkDropbox.setText("Link to New Dropbox");
        }
        else{
            Log.i(TAG, "Dropbox is NOT LINKED");
            dropboxStatus.setText("NOT LINKED");
            dropboxStatus.setTextColor(Color.RED);
            linkedAccount.setText("None");
            buttonLinkDropbox.setText("Link Dropbox");
        }
    }


    public void onClickRefreshLinkStatus(View view) {
        Log.i(TAG,"onClickRefreshLinkStatus called");
        updateLinkStatus();
        Toast.makeText(this,"Dropbox status refreshed", Toast.LENGTH_SHORT).show();
    }

    public void onClickLinkDropbox(View view) {
        Log.i(TAG, "Attempting to link Dropbox");
        mDbxAcctMgr.startLink((Activity) this, MainActivity.DROPBOX_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MainActivity.DROPBOX_REQUEST_CODE) {
            Log.i(TAG, "Returned from Dropbox linking activity");
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
