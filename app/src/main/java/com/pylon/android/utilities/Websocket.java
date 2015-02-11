package com.pylon.android.utilities;

import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pylon.R;
import java.util.LinkedHashMap;
import java.util.Map;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketConnectionHandler;
import de.tavendo.autobahn.WebSocketException;

/**
 * Created by croucha on 2/11/15.
 */
public class Websocket {

    private static final String TAG = "Websocket Connection";

    private final Activity activity;

    private String uri;

    private final WebSocketConnection webSocketConnection = new WebSocketConnection();

    public Websocket(Activity activity, String uri) {
        this.activity = activity;
        this.uri = uri;
    }

    /**
     * Define method that will make websocket connection.
     *
     * @return Websocket
     */
    public Websocket connect() {
        final String wsUri = "ws://" + this.uri;
        // Attempt to connect
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
                    // Convert to relative time
                    CharSequence relativeTime = Utils.convertIso8601ToRelativeTime(received);
                    // Set final linear layout (makes it available in runnable)
                    final LinearLayout layout = (LinearLayout) activity.findViewById(R.id.chatMessageContainer);
                    // Define final text view (makes it available in runnable)
                    final TextView textView = new TextView(activity);
                    // Set params
                    textView.setLayoutParams(
                        new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                    );
                    // Determine if text view is empty
                    if(textView.getText() != "") {
                        // Append line separator
                        textView.append(System.getProperty("line.separator"));
                    }
                    // Append message to view
                    textView.append(sender + ": " + message + " (" + relativeTime + ")");
                    textView.setTag(received);
                    // When adding another view, make sure you do it on the UI thread.
                    if(layout != null) {
                        layout.post(new Runnable() {
                            public void run() {
                            // Add view
                            layout.addView(textView);
                            }
                        });
                    }
                }

                @Override
                public void onClose(int code, String reason) {
                    Log.d(TAG, "Connection lost.");
                }
            });
        } catch (WebSocketException e) {
            Log.d(TAG, e.toString());
        }
        return this;
    }

    /**
     * Send text message using websocket connection.
     *
     * @param text
     * @return Websocket
     */
    public Websocket sendTextMessage(String text) {
        this.webSocketConnection.sendTextMessage(text);
        return this;
    }
}