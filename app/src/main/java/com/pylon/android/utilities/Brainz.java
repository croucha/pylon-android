package com.pylon.android.utilities;

import android.util.Log;

/**
 * Created by croucha on 5/14/15.
 *
 * Ahhhhhhhh!! Brainz!!!!!! grrrrrraaahhh!!
 *           
 *                  ......
 *                  C C  /
 *                 /<   /             
 *  ___ __________/_#__=o             
 * /(- /(\_\________   \              
 * \ ) \ )_      \o     \             
 * /|\ /|\       |'     |             
 *               |     _|             
 *               /o   __\             
 *              / '     |             
 *             / /      |             
 *            /_/\______|             
 *           (   _(    <              
 *            \    \    \             
 *             \    \    |            
 *              \____\____\           
 *              ____\_\__\_\          
 *            /`   /`     o\          
 *            |___ |_______|
 *
 */
public class Brainz {

    private static String command;
    private static String action;
    private static Integer code;
    /**
     * Code action mapping
     */
    private static Integer FORWARD = 38;
    private static Integer BACKWARD = 40;
    private static Integer LEFT = 37;
    private static Integer RIGHT = 39;

    private static final String TAG = "Brainz";

    public static void setCommand(String command) {
        if(!command.equals("") && command != null) {
            Brainz.command = command;
            // Split command
            String[] parts = command.split(":");
            try {
                // Set action
                Brainz.action = parts[0];
                // Set code
                Brainz.code = Integer.parseInt(parts[1]);
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.d(TAG, ": " + e.getMessage());
            }
        }
    }

    /**
     * Determines behavior
     */
    public static void processBehavior() {
        if(Brainz.code != null && Brainz.action != null) {
            if(Brainz.FORWARD == Brainz.code) {
                Brainz.stumbleForward();
            } else if(Brainz.BACKWARD == Brainz.code) {
                Brainz.stumbleBackward();
            } else if(Brainz.RIGHT == Brainz.code) {
                Brainz.stumbleRight();
            } else if(Brainz.LEFT == Brainz.code) {
                Brainz.stumbleLeft();
            }
        }
    }

    /**
     * Methods for controlling robot behavior
     */
    public static void stumbleForward() {
        Log.d(TAG, ": stumbleForward");
        // Determine action
        if(Brainz.action.equals("set")) {
            // Start action
        }
        // Stop action
        else {

        }
    }

    public static void stumbleBackward() {
        Log.d(TAG, ": stumbleBackward");
        // Determine action
        if(Brainz.action.equals("set")) {
            // Start action
        }
        // Stop action
        else {

        }
    }

    public static void stumbleRight() {
        Log.d(TAG, ": stumbleRight");
        // Determine action
        if(Brainz.action.equals("set")) {
            // Start action
        }
        // Stop action
        else {

        }
    }

    public static void stumbleLeft() {
        Log.d(TAG, ": stumbleLeft");
        // Determine action
        if(Brainz.action.equals("set")) {
            // Start action
        }
        // Stop action
        else {

        }
    }
}