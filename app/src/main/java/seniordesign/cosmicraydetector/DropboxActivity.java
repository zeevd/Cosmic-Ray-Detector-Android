package seniordesign.cosmicraydetector;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

import java.io.IOException;

public class DropboxActivity extends ActionBarActivity {
    private DbxAccountManager mDbxAcctMgr;
    private DbxFileSystem dbxFs;

    private TextView linkStatus;
    final static int dropboxRequestCode=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dropbox);
        init();
        updateLinkStatus();
    }

    public void init(){
        //Init account manager
        mDbxAcctMgr = DbxAccountManager.getInstance(getApplicationContext(), "ogamnznpp7actyg", "98jkkfgplqciq9l");
        //Init View components
        linkStatus = (TextView) findViewById(R.id.textview_link_status);
    }

    public boolean isLinked(){
        return mDbxAcctMgr.hasLinkedAccount();
    }

    private void updateLinkStatus(){
        if (isLinked()){
            linkStatus.setText("Linked account: " + mDbxAcctMgr.getLinkedAccount().getAccountInfo().userName);
            linkStatus.setTextColor(Color.GREEN);
        }
        else{
            linkStatus.setText("Not Linked");
            linkStatus.setTextColor(Color.RED);
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
