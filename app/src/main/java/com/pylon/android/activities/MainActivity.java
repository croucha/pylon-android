package com.pylon.android.activities;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketConnectionHandler;
import de.tavendo.autobahn.WebSocketException;

import com.google.gson.Gson;
import com.pylon.R;

import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends Activity {
    private static final String TAG = "Websocket Test";

    private final WebSocketConnection webSocketConnection = new WebSocketConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(this.getLocalClassName(), "On Create Called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectWebSocket();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    /**
     * Define method that will make websocket connection.
     *
     * @return void
     */
    private void connectWebSocket() {
        final String wsUri = "ws://192.168.1.105:8080/pylon-ws/chat/java";

        try {
            webSocketConnection.connect(wsUri, new WebSocketConnectionHandler() {

                @Override
                public void onOpen() {
                    Log.d(TAG, "Status: Connected to " + wsUri);
                    Map<String,String> message = new LinkedHashMap<String,String>();
                    message.put("sender", "Doe");
                    message.put("message", "Pylon Android application connected");
                    message.put("received", "");
                    webSocketConnection.sendTextMessage(new Gson().toJson(message));
                }

                @Override
                public void onTextMessage(String payload) {
                    Log.d(TAG, "Got echo: " + payload);
                }

                @Override
                public void onClose(int code, String reason) {
                    Log.d(TAG, "Connection lost.");
                }
            });
        } catch (WebSocketException e) {
            Log.d(TAG, e.toString());
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

        webSocketConnection.sendTextMessage(new Gson().toJson(message));
        editText.setText("");
    }
}