package com.hypertrack.quickstart;

import static org.greenrobot.eventbus.EventBus.TAG;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hypertrack.quickstart.android.github.R;
import com.hypertrack.sdk.HyperTrack;
import com.hypertrack.sdk.TrackingError;
import com.hypertrack.sdk.TrackingStateObserver;
import com.hypertrack.sdk.views.HyperTrackViews;
import com.hypertrack.sdk.views.dao.MovementStatus;

public class MainActivity extends AppCompatActivity implements TrackingStateObserver.OnTrackingStateChangeListener {
    private static final String TAG = "MainActivity";
    private static final String PUBLISHABLE_KEY = "_-_s4Di-y9a1-jEHY1flQT-RdG-Oun-8L0M88x5f8OIu8OMhgbQdjgEqcPOwGr6Kc5RRgsZxLQDnFYb0VtQxVQ";

    private HyperTrack sdkInstance;
    private boolean shouldRequestPermissions = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        HyperTrack.enableDebugLogging();

        sdkInstance = HyperTrack
                .getInstance(PUBLISHABLE_KEY)
                .addTrackingListener(this);
        sdkInstance.start();

        Log.d(TAG, "device id is " + sdkInstance.getDeviceID());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (sdkInstance.isRunning()) {
            onTrackingStart();
        } else {
            onTrackingStop();
        }

        if (shouldRequestPermissions) {
            shouldRequestPermissions = false;
            sdkInstance.requestPermissionsIfNecessary();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sdkInstance.removeTrackingListener(this);
    }

    // TrackingStateObserver.OnTrackingStateChangeListener interface methods

    @Override
    public void onError(TrackingError trackingError) {
        Log.d(TAG, "onError: " + trackingError.message);
    }

    @Override
    public void onTrackingStart() {

        new ComputingPosition(this, this.sdkInstance).start();

    }

    @Override
    public void onTrackingStop() {

    }

}
