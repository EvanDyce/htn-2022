package com.hypertrack.quickstart;

import static androidx.core.content.ContextCompat.startActivity;
import static org.greenrobot.eventbus.EventBus.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.util.Consumer;

import com.hypertrack.sdk.HyperTrack;
import com.hypertrack.sdk.OutageReason;
import com.hypertrack.sdk.Result;
import com.hypertrack.sdk.views.HyperTrackViews;
import com.hypertrack.sdk.views.dao.MovementStatus;

public class ComputingPosition extends Thread {

    private Activity activity;
    private HyperTrack sdkInstance;

    private boolean hasRedirectedMenu = false;
    private boolean hasRedirectedReview = false;
    private boolean insideRoom = false;

    public ComputingPosition(Activity activity, HyperTrack sdkInstance) {
        this.activity = activity;
        this.sdkInstance = sdkInstance;
    }

    @Override
    public void run() {
        super.run();

        HyperTrackViews hypertrackView = HyperTrackViews.getInstance(activity.getApplicationContext(), Keys.PUBLISHABLE_KEY);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            while (this.sdkInstance.isRunning()) {
                hypertrackView.getDeviceMovementStatus(this.sdkInstance.getDeviceID(),
                        new Consumer<MovementStatus>() {
                            @Override
                            public void accept(MovementStatus movementStatus) {
                                com.hypertrack.sdk.views.dao.Location location = movementStatus.location;
                                if (location != null) {
                                    Log.d(TAG, "Got device location " + location +
                                            " with latitude " + location.getLatitude() +
                                            " and longitude " + location.getLongitude());
                                }

                                if (isInsideRoom(location) && !hasRedirectedMenu) {
                                    Intent viewIntent = new Intent("android.intent.action.VIEW",
                                            Uri.parse("https://www.whitespot.ca/menus/bc/dinner/"));
                                    viewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(activity, viewIntent, Bundle.EMPTY);
                                    hasRedirectedMenu = true;
                                }
                            }
                        });
                try {
                    sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }



    private boolean isInsideRoom(com.hypertrack.sdk.views.dao.Location location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        if (latitude <= 43.473144 && latitude >= 43.473046) {
            // longitude is within the bounds
            if (longitude <= -80.539757 && longitude >= -80.539822) {
                // the location is within the bounds
               return true;
            }
        }
        return false;
    }


}
