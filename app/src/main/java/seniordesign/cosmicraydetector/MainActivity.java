package seniordesign.cosmicraydetector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cengalabs.flatui.FlatUI;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;


public class MainActivity extends ActionBarActivity {
    ///LOGGING TAG///
    private static final String TAG = "MainActivity";

    ///DROPBOX RELATED CONSTANTS///
    public static final int DROPBOX_REQUEST_CODE = 100;
    private static final String APP_KEY = "ogamnznpp7actyg";
    private static final String APP_SECRET = "98jkkfgplqciq9l";

    ///GLOBAL VARIABLES///
    public static DbxAccountManager mDbxAcctMgr;
    public static TreeMap<Long, SensorData> sensorDataMap = new TreeMap<Long, SensorData>();
    public static ArrayList<Long> sensorDataList = new ArrayList<Long>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Initializing MainActivity.java");
        super.onCreate(savedInstanceState);

        Log.i(TAG, "Init FlatUI");
        FlatUI.initDefaultValues(this);
        FlatUI.setDefaultTheme(FlatUI.BLOOD);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(FlatUI.getActionBarDrawable(this, FlatUI.BLOOD, false, 2));


        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);

        if (isFirstRun) {
            Log.i(TAG, "First run detected");
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).apply();
            showWelcomeDialog();    //Also initializes Dropbox
        } else {
            Log.i(TAG, "Not first run detected");
            initDropbox();
        }


    }

    public void initDropbox() {
        Log.i(TAG, "Getting Dropbox account manager");
        mDbxAcctMgr = DbxAccountManager.getInstance(getApplicationContext(), APP_KEY, APP_SECRET);
        if (mDbxAcctMgr.hasLinkedAccount()) {
            Log.i(TAG, "App is already linked to Dropbox");
            Intent myIntent = new Intent(MainActivity.this, BlankActivity.class);
            startActivityForResult(myIntent, DROPBOX_REQUEST_CODE);
            //Execution resumes @ onActivityResult()
        } else {
            Log.i(TAG, "Linking Dropbox via browser");
            mDbxAcctMgr.startLink((Activity) this, DROPBOX_REQUEST_CODE);
            //Execution resumes @ onActivityResult()
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DROPBOX_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i(TAG, "Dropbox account linked");

                new showLoadandReadDropbox().execute();

            } else {
                Log.e(TAG, "Failed to link Dropbox account");
                Toast.makeText(this, "Failed to link Dropbox account", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void readFilesFromDropbox() throws DbxException {
        Log.i(TAG, "Getting Dropbox filesystem");
        DbxFileSystem dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
        DbxPath path = new DbxPath(getString(R.string.dropbox_path));
        List<DbxFileInfo> dropboxFileList = dbxFs.listFolder(path);

        Log.i(TAG, "Files on Dropbox:\n");
        for (DbxFileInfo info : dropboxFileList) {
            Log.i(TAG, "    " + info.path + ", " + info.modifiedTime + '\n');
        }

        Log.i(TAG, "Iterating through Dropbox files");
        Iterator<DbxFileInfo> dropboxFileIterator = dropboxFileList.iterator();
        while (dropboxFileIterator.hasNext()) {
            DbxFileInfo fileInfo = dropboxFileIterator.next();
            if (fileInfo.isFolder) continue;    //Only interested in files, not folders

            DbxFile currentFile = dbxFs.open(fileInfo.path);
            Log.i(TAG, "Opening file: " + currentFile.getPath());
            try {
                String currentFileContents = currentFile.readString();
                Log.v(TAG, "File: " + fileInfo.path + " Contents: " + currentFileContents);

                Log.i(TAG, "Converting file contents to List of SensorData objects");
                List<SensorData> currentFileData = SensorData.parseData(currentFileContents);

                for (SensorData data : currentFileData) {
                    Log.v(TAG, "Adding entry to map: " + data.getDate().toString());
                    sensorDataMap.put(data.getDate().getTime(), data);//Key=unix time, Value=SensorData
                }

            } catch (IOException e) {
                Log.e(TAG, "Failed to read file " + currentFile.getPath());
                e.printStackTrace();
            } finally {
                Log.i(TAG, "Closing file " + currentFile.getPath());
                currentFile.close();
            }
        }
        Log.i(TAG, "Copying keyset to sensorDataList");
        Iterator<Long> i = sensorDataMap.keySet().iterator();
        while (i.hasNext()) sensorDataList.add(i.next());

    }

    private void showWelcomeDialog() {
        Log.i(TAG, "Displaying welcome dialog box");

        //Listener called after user dismisses the welcome dialog box
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                initDropbox();
            }
        };

        //Pop-up welcome dialog on first run
        new AlertDialog.Builder(this)
                .setTitle("Welcome to the Cosmic Ray Detector Application")
                .setMessage("You will now be sent to the Dropbox website. Please enter your account information " +
                        "to allow the app to sync to your Dropbox.")
                .setPositiveButton("Will Do!", listener)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_contact:
                Log.i(TAG, "Launching ContactActivity.java");
                Intent myIntent = new Intent(MainActivity.this, ContactActivity.class);
                MainActivity.this.startActivity(myIntent);
                break;
            case R.id.action_about:
                Log.i(TAG, "Launching AboutActivity.java");
                Intent myIntent1 = new Intent(MainActivity.this, AboutActivity.class);
                MainActivity.this.startActivity(myIntent1);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickDropboxActivity(View view) {
        Log.i(TAG, "Launching DropboxActivity.java");
        Intent myIntent = new Intent(MainActivity.this, DropboxActivity.class);

        MainActivity.this.startActivity(myIntent);
    }

    public void onClickGraphData(View view) {
        Log.i(TAG, "Launching GraphActivity.java");
        Intent myIntent = new Intent(MainActivity.this, GraphActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    private class showLoadandReadDropbox extends AsyncTask<Void, Void, Integer>{
        private ProgressDialog loadingDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute(){
            Log.i(TAG, "Displaying loading from Dropbox dialog");
            loadingDialog.setMessage("Loading data from Dropbox. Please wait...");
            loadingDialog.show();
        }

        @Override
        protected Integer doInBackground(Void... params){
            try {
                Log.i(TAG, "Reading files from Dropbox");
                readFilesFromDropbox();

            } catch (DbxException e) {
                Log.e(TAG, "Failed to access files on Dropbox " + e.getMessage());
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result){
            if(result==0)
                Toast.makeText(MainActivity.this, "Finished loading data from Dropbox", Toast.LENGTH_LONG).show();

            Log.i(TAG, "Dismissing loading from Dropbox dialog");
            loadingDialog.dismiss();
        }
    }
}



