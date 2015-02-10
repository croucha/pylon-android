package com.pylon.android.activities;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketConnectionHandler;
import de.tavendo.autobahn.WebSocketException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pylon.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity {
    private static final String TAG = "Websocket Test";

    private final WebSocketConnection webSocketConnection = new WebSocketConnection();

    private Timer timer = new Timer();

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
        // Start timer to update elapsed time in text view
        //this.startTimer();
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
                    // Define parser
                    JsonParser jsonParser = new JsonParser();
                    // Parse results
                    JsonObject results = jsonParser.parse(payload).getAsJsonObject();
                    // Define sender
                    String sender = results.get("sender").getAsString();
                    // Define message
                    String message = results.get("message").getAsString();
                    // Define received
                    String received = results.get("received").getAsString();
                    // Define date format
                    // @TODO, fix format
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                    // Define date
                    Date date = new Date();
                    // Attempt to parse
                    try {
                        date = formatter.parse(received);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    // Format time
                    CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(
                        date.getTime(),
                        System.currentTimeMillis(),
                        DateUtils.SECOND_IN_MILLIS
                    );
                    // Define text view
                    TextView textView = (TextView) findViewById(R.id.chatMessage);
                    // Determine if text view is empty
                    if(textView.getText() != "") {
                        // Append line separator
                        textView.append(System.getProperty("line.separator"));
                    }
                    // Append message to chat
                    // @TODO look into list view and append list items with hidden tags
                    // of the date to be parsed and updated for time elapsed
                    textView.append(sender + ": " + message + " (" + relativeTime + ")");
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

    // @TODO, update to parse each list item in a list view and get the date as a hidden
    // tag and update
    private void startTimer() {
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                // Define text view
                TextView textView = (TextView) findViewById(R.id.chatMessage);
                // Determine if text view is empty
                if(textView != null && textView.getText() != "") {
                    Pattern pattern = Pattern.compile("\\((.*?)\\)", Pattern.DOTALL);
                    Matcher match = pattern.matcher(textView.getText());
                    List<String> matches = new ArrayList<String>();
                    while(match.find()){
                        matches.add(match.group());
                    }
                    Log.i(TAG, matches.toString());
                }
            }
        }, 0, 1000);
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