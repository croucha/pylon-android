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
import com.pylon.android.utilities.Websocket;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import com.pylon.android.timers.RelativeTimer;

public class MainActivity extends Activity {
    private static final String TAG = "Main Activity";

    private Websocket websocket;

    private Fragment placeholderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Invoke this method with super since it's being overridden
        super.onCreate(savedInstanceState);
        // Set frame layout to display, which is the name of the actual layout xml document
        this.setContentView(R.layout.activity_main);
        // Instantiate fragments
        this.instantiateFragments(savedInstanceState);
        // Define websocket and make web socket connect
        this.websocket = new Websocket(this, "192.168.1.105:8080/pylon-ws/chat/java").connect();
        // Start timer to update elapsed time in text views every 30 seconds
        new Timer().schedule(new RelativeTimer(this), 0, 30000);
        // For now, prevent screen rotation
        // See method onRestoreInstanceState
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentManager manager = getFragmentManager();
        manager.putFragment(outState, PlaceholderFragment.TAG, this.placeholderFragment);
    }

    // @TODO: come up with way to restore textview chats for screen rotation.
    // They need to be saved in method onSaveInstanceState.
    // Saving the fragment isn't enough to save the textviews.
    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        instantiateFragments(inState);
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

    /**
     * Setup the fragment_main.xml over the frame layout
     *
     * @param inState
     */
    private void instantiateFragments(Bundle inState) {
        FragmentManager manager = this.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        // Restore previous fragment if state not null
        if (inState != null) {
            this.placeholderFragment = manager.getFragment(
                inState,
                PlaceholderFragment.TAG
            );
        }
        // Create new fragment
        else {
            this.placeholderFragment = new PlaceholderFragment();
            transaction.add(
                R.id.container,
                this.placeholderFragment,
                PlaceholderFragment.TAG
            );
            transaction.commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final String TAG = "PlaceholderFragment";

        public PlaceholderFragment() {}

        @Override
        public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    /**
     * Send message from this Android's app interface to the websocket server.
     * The fragment_main.xml holds the onClick parameter for calling this method.
     *
     * @param view
     */
    public void sendMessage(View view) {
        EditText editText = (EditText)findViewById(R.id.message);
        Map<String,String> message = new LinkedHashMap<String,String>();
        message.put("sender", "Doe");
        message.put("message", editText.getText().toString());
        message.put("received", "");
        this.websocket.sendTextMessage(new Gson().toJson(message));
        editText.setText("");
    }
}