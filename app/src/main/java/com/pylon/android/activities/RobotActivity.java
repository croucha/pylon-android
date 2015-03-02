package com.pylon.android.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.google.gson.Gson;
import com.pylon.R;
import com.pylon.android.utilities.RobotSocket;
import com.pylon.android.utilities.Websocket;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import com.pylon.android.timers.RelativeTimer;

public class RobotActivity extends Activity {
    private static final String TAG = "Robot Activity";

    private RobotSocket websocket;

    private Fragment placeholderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Invoke this method with super since it's being overridden
        super.onCreate(savedInstanceState);
        // Set frame layout to display, which is the name of the actual layout xml document
        this.setContentView(R.layout.console);
        // Define websocket and make web socket connect
        this.websocket = new RobotSocket(this, "192.168.1.105:8080/pylon-ws/remote/lego-nxt").connect();
        // Start timer to update elapsed time in text views every 30 seconds
        new Timer().schedule(new RelativeTimer(this, R.id.messages), 0, 30000);
        // Prevent screen rotation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}