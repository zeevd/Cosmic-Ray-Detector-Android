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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

import java.io.IOException;

import seniordesign.cosmicraydetector.MainActivity;
import seniordesign.cosmicraydetector.R;

public class DropboxActivity extends ActionBarActivity {
    ///LOGGING TAG///
    private static final String TAG = "DropboxActivity";

    private DbxAccountManager mDbxAcctMgr;
    private DbxFileSystem dbxFs;

    private TextView linkStatus;
    private Button buttonLinkDropbox;
    private Button buttonDisplayContent;
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
        mDbxAcctMgr = MainActivity.mDbxAcctMgr;
        //Init View components
        linkStatus = (TextView) findViewById(R.id.textview_link_status);
        buttonLinkDropbox = (Button) findViewById(R.id.button_link_dropbox);
        buttonDisplayContent = (Button) findViewById(R.id.button_show_raw_file_content);
    }

    public boolean isLinked(){
        return mDbxAcctMgr.hasLinkedAccount();
    }

    private void updateLinkStatus(){
        if (isLinked()){
            linkStatus.setText("Linked account: " + mDbxAcctMgr.getLinkedAccount().getAccountInfo().displayName);
            linkStatus.setTextColor(Color.GREEN);
            buttonLinkDropbox.setVisibility(View.INVISIBLE);
            buttonDisplayContent.setVisibility(View.VISIBLE);
        }
        else{
            linkStatus.setText("Not Linked");
            linkStatus.setTextColor(Color.RED);
            buttonLinkDropbox.setVisibility(View.VISIBLE);
            buttonDisplayContent.setVisibility(View.INVISIBLE);
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

    public void onClickShowFileContent(View view) {
        String[] contents = tempReadContent().split("\n");

        ListView listView = (ListView) findViewById(R.id.listView);
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,contents);
        listView.setAdapter(adapter);

    }

    //TODO: REMOVE
    private String tempReadContent() {
        try {
            dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
        } catch (DbxException.Unauthorized unauthorized) {
            unauthorized.printStackTrace();
        }
        DbxPath path = new DbxPath("/System.txt");
        DbxFile testFile = null;
        try {
            testFile = dbxFs.open(path);
        } catch (DbxException e) {
            e.printStackTrace();
        }
        String contents = null;
        try {
            contents = testFile.readString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("Dropbox Test", "File contents: " + contents);
        testFile.close();
        return contents;
    }

    @Override
    protected void onStop() {
        Log.w(TAG, "onStop was called. Terminating applicaiton");
        super.onStop();
        finish();
    }
}
