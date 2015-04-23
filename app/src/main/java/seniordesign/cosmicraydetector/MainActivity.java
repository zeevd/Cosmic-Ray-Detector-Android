package seniordesign.cosmicraydetector;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import seniordesign.cosmicraydetector.androidplot.AndroidPlotXYActivity;
import seniordesign.cosmicraydetector.dropbox.DropboxActivity;

import static java.lang.Thread.sleep;


public class MainActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        runStartupActivities();

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
        switch(id){
            case R.id.action_about:
                launchAboutActivity();
                break;
            case R.id.action_help:
                launchHelpActivity();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickDropboxActivity(View view) {
        launchDropboxActivity();
    }
    public void launchDropboxActivity(){
        Intent myIntent = new Intent(MainActivity.this, DropboxActivity.class);
        MainActivity.this.startActivity(myIntent);
    }
    public void launchAboutActivity(){
        Intent myIntent = new Intent(MainActivity.this, AboutActivity.class);
        MainActivity.this.startActivity(myIntent);
    }
    public void launchHelpActivity(){
        Intent myIntent = new Intent(MainActivity.this, HelpActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    public void runStartupActivities() {
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun){
            new AlertDialog.Builder(this)
                    .setTitle("Welcome to the Cosmic Ray Detector Application")
                    .setMessage("Please sync your Dropbox to the applcation by clicking \""
                            + getString(R.string.button_dropbox_settings) + "\" followed by \"" +
                    getString(R.string.button_link_dropbox) + "\"")
                    .setNeutralButton("Will Do!",null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).apply();
        }
        else{
            //Show loading dialog
            final ProgressDialog progress = new ProgressDialog(this);
            progress.setTitle("Loading");
            progress.setMessage("Loading data from the cloud...");
            progress.show();

            final Handler handler = new Handler();
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    //Do some stuff while loading screen showing
                    try {
                        sleep(7000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                public void onPostExecute(Void result){
                    handler.post(new Runnable(){
                        @Override
                        public void run(){
                            //When done loading, dismiss loading screen
                            progress.dismiss();
                        }
                    });
                }
            }.execute();
        }
    }

    public void onClickAndroidPlotTest(View view) {
        Intent myIntent = new Intent(MainActivity.this, AndroidPlotXYActivity.class);
        MainActivity.this.startActivity(myIntent);
    }
}
