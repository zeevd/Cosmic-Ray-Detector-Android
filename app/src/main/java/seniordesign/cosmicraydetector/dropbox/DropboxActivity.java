package seniordesign.cosmicraydetector.dropbox;

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

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

import java.io.IOException;

import seniordesign.cosmicraydetector.R;

public class DropboxActivity extends ActionBarActivity {
    private DbxAccountManager mDbxAcctMgr;
    private DbxFileSystem dbxFs;

    private TextView linkStatus;
    private Button buttonLinkDropbox;
    final static int dropboxRequestCode=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dropbox);
        init();
        updateLinkStatus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dropbox, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_dropbox_account_info) {
            String info;
            if (!isLinked()) info = "Dropbox account not linked";
            else{
                info = mDbxAcctMgr.getLinkedAccount().getAccountInfo().displayName+"\n"
                        +mDbxAcctMgr.getLinkedAccount().toString();
            }
            new AlertDialog.Builder(this)
                    .setTitle("Dropbox Information")
                    .setMessage(info)
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void init(){
        //Init account manager
        mDbxAcctMgr = DbxAccountManager.getInstance(getApplicationContext(), "ogamnznpp7actyg", "98jkkfgplqciq9l");
        //Init View components
        linkStatus = (TextView) findViewById(R.id.textview_link_status);
        buttonLinkDropbox = (Button) findViewById(R.id.button_link_dropbox);
    }

    public boolean isLinked(){
        return mDbxAcctMgr.hasLinkedAccount();
    }

    private void updateLinkStatus(){
        if (isLinked()){
            linkStatus.setText("Linked account: " + mDbxAcctMgr.getLinkedAccount().getAccountInfo().displayName);
            linkStatus.setTextColor(Color.GREEN);
            buttonLinkDropbox.setVisibility(View.INVISIBLE);
        }
        else{
            linkStatus.setText("Not Linked");
            linkStatus.setTextColor(Color.RED);
            buttonLinkDropbox.setVisibility(View.VISIBLE);
        }
    }


    public void onClickRefreshLinkStatus(View view) {
        updateLinkStatus();
    }

    public void onClickLinkDropbox(View view) {
        mDbxAcctMgr.startLink((Activity) this, dropboxRequestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == dropboxRequestCode) {
            if (resultCode == Activity.RESULT_OK) {
                Toast t = Toast.makeText(this, "Dropbox account linked successfully", Toast.LENGTH_LONG);
                t.show();
                updateLinkStatus();
            } else {
                Toast t = Toast.makeText(this, "Failed to link Dropbox account", Toast.LENGTH_LONG);
                t.show();
                updateLinkStatus();
              }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

//    private void tempReadContent() {
//        try {
//            dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
//            DbxPath path = new DbxPath("/System.txt");
//            DbxFile testFile = dbxFs.open(path);
//            String contents = testFile.readString();
//            Log.d("Dropbox Test", "File contents: " + contents);
//
//        } catch (DbxException.Unauthorized unauthorized) {
//            unauthorized.printStackTrace();
//        } catch (DbxException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            // testFile.close();
//        }
//    }
}
