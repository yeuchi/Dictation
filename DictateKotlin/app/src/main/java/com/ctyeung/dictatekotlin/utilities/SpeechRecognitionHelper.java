// ==================================================================
//https://software.intel.com/en-us/articles/developing-android-applications-with-voice-recognition-features
//About the Authors
//
// Stanislav works in the Software & Service Group at Intel Corporation. 
// He has 10+ years of experience in software development. 
// His main interest is optimization of performance, power consumption, 
// and parallel programming. 
// In his current role as an Application Engineer providing technical 
// support for Intel-based devices, Stanislav works closely with software 
// developers and SoC architects to help them achieve the best possible 
// performance on Intel platforms. Stanislav holds a Master's degree in 
// Mathematical Economics from the National Research University Higher 
// School of Economics.
//
// Mikhail is co-author of this blog and an Intel summer intern, who is 
// studying computer science at Lobachevsky University. He likes to deep 
// dive in to math and do Android programming tricks.
// Intel and the Intel logo are trademarks of Intel Corporation in the U.S. and/or other countries.
// Copyright © 2013 Intel Corporation. All rights reserved.
// ==================================================================
package com.ctyeung.dictatekotlin.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.speech.RecognizerIntent;

import java.util.List;


public class SpeechRecognitionHelper {

    public void run(Activity ownerActivity) {
        if (isSpeechRecognitionActivityPresented(ownerActivity) == true) {
            // if yes Ð running recognition
            startRecognitionActivity(ownerActivity);
        } else {
            // start installing process
            installGoogleVoiceSearch(ownerActivity);
        }
    }

    private boolean isSpeechRecognitionActivityPresented(Activity ownerActivity) {
        try {
            // getting an instance of package manager
            PackageManager pm = ownerActivity.getPackageManager();
            // a list of activities, which can process speech recognition Intent
            List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);

            if (activities.size() != 0) {    // if list not empty
                return true;                // then we can recognize the speech
            }
        } catch (Exception e) {

        }

        return false; // we have no activities to recognize the speech
    }

    /**
     * Send an Intent with request on speech
     *
     * @param callerActivity - Activity, that initiated a request
     */
    private void startRecognitionActivity(Activity ownerActivity) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        // giving additional parameters:
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Select an application");    // user hint
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);    // setting recognition model, optimized for short phrases Ð search queries
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);    // quantity of results we want to receive
        //choosing only 1st -  the most relevant

        // start Activity ant waiting the result
        ownerActivity.startActivityForResult(intent, 1234);
    }

    /**
     * Asking the permission for installing Google Voice Search.
     * If permission granted Ð sent user to Google Play
     *
     * @param callerActivity Ð Activity, that initialized installing
     */
    private void installGoogleVoiceSearch(final Activity ownerActivity) {

        // creating a dialog asking user if he want
        // to install the Voice Search
        Dialog dialog = new AlertDialog.Builder(ownerActivity)
                .setMessage("For recognition itÕs necessary to install \"Google Voice Search\"")    // dialog message
                .setTitle("Install Voice Search from Google Play?")    // dialog header
                .setPositiveButton("Install", new DialogInterface.OnClickListener() {    // confirm button

                    // Install Button click handler
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            // creating an Intent for opening applications page in Google Play
                            // Voice Search package name: com.google.android.voicesearch
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.voicesearch"));
                            // setting flags to avoid going in application history (Activity call stack)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                            // sending an Intent
                            ownerActivity.startActivity(intent);
                        } catch (Exception ex) {
                            // if something going wrong
                            // doing nothing
                        }
                    }
                })

                .setNegativeButton("Cancel", null)    // cancel button
                .create();

        dialog.show();    // showing dialog
    }
}