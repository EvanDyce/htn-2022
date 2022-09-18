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

    private TextView trackingStatusLabel;
    private HyperTrack sdkInstance;
    private boolean shouldRequestPermissions = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        TextView deviceId = findViewById(R.id.deviceIdTextView);
        trackingStatusLabel = findViewById(R.id.statusLabel);
        HyperTrack.enableDebugLogging();

        sdkInstance = HyperTrack
                .getInstance(PUBLISHABLE_KEY)
                .addTrackingListener(this);
        sdkInstance.start();

        Button button = findViewById(R.id.button_query_position);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HyperTrackViews hypertrackView = HyperTrackViews.getInstance(getApplicationContext(), Keys.PUBLISHABLE_KEY);

                hypertrackView.getDeviceMovementStatus(sdkInstance.getDeviceID(),
                        new Consumer<MovementStatus>() {
                            @Override
                            public void accept(MovementStatus movementStatus) {
                                com.hypertrack.sdk.views.dao.Location location = movementStatus.location;
                                if (location != null) {
                                    Log.d(TAG, "Got device location " + location +
                                            " with latitude " + location.getLatitude() +
                                            " and longitude " + location.getLongitude());
                                }
                            }
                        });
            }
        });




        deviceId.setText(sdkInstance.getDeviceID());
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
        trackingStatusLabel.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLabelError));
        switch (trackingError.code) {
            case TrackingError.INVALID_PUBLISHABLE_KEY_ERROR:
            case TrackingError.AUTHORIZATION_ERROR:
                trackingStatusLabel.setText(R.string.check_publishable_key);
                break;
            case TrackingError.GPS_PROVIDER_DISABLED_ERROR:
                trackingStatusLabel.setText(R.string.enable_gps);
                break;
            case TrackingError.PERMISSION_DENIED_ERROR:
                trackingStatusLabel.setText(R.string.permissions_not_granted);
                break;
            default:
                trackingStatusLabel.setText(R.string.cant_start_tracking);

        }
    }

    @Override
    public void onTrackingStart() {

        trackingStatusLabel.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLabelActive));
        trackingStatusLabel.setText(R.string.tracking_active);
        Location top_right = new Location("provider-name");
        top_right.setLatitude(43.473410);
        top_right.setLongitude(-80.541037);

        Location bottom_left = new Location("provider-name");
        bottom_left.setLatitude(43.473279);
        bottom_left.setLongitude(-80.539559);

        new ComputingPosition(this, this.sdkInstance).start();

    }

    @Override
    public void onTrackingStop() {

        trackingStatusLabel.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLabelStopped));
        trackingStatusLabel.setText(R.string.tracking_stopped);

    }

}
