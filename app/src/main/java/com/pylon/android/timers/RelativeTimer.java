package com.pylon.android.timers;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.pylon.R;
import com.pylon.android.utilities.Utils;
import java.util.TimerTask;

/**
 * Used to update relative time in text view.
 * Created by croucha on 2/11/15.
 */
public class RelativeTimer extends TimerTask {

    private final Activity activity;

    public RelativeTimer(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void run() {
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
            // Define linear layout of messages
            LinearLayout layout = (LinearLayout) activity.findViewById(R.id.chatMessageContainer);
            // Check for null
            if (layout != null) {
                // Loop through layout's children
                for (int i = 0; i < layout.getChildCount(); i++) {
                    // Define child view
                    View view = layout.getChildAt(i);
                    // Determine if instance before cast
                    if (view instanceof TextView) {
                        // Cast
                        TextView textView = (TextView) view;
                        // Determine if text view is set, text isn't empty and tag is set
                        if (textView != null &&
                                textView.getText() != "" &&
                                textView.getTag() != null
                        ) {
                            String received = (String) textView.getTag();
                            // Convert to relative time
                            String relativeTime = Utils.convertIso8601ToRelativeTime(received);
                            // Set original text
                            String text = textView.getText().toString();
                            // Update relative time
                            String updatedRelativeTimeChat = text.replaceAll("\\(.+\\)", "(" + relativeTime + ")");
                            // Invalidate views (required before updating text view)
                            textView.invalidate();
                            layout.invalidate();
                            // Set received tag again
                            textView.setTag(received);
                            // Set chat with new relative time again
                            textView.setText(updatedRelativeTimeChat);
                        }
                    }
                }
            }
            }
        });
    }
}