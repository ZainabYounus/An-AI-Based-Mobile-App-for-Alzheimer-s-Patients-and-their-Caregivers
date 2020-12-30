package com.example.alzheimersapp.tracklocation;

import android.app.AppComponentFactory;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.uiwidgets.ApplozicApplication;
import com.example.alzheimersapp.App;
import com.example.alzheimersapp.CaregiverModule.CaregiverDashboard;
import com.example.alzheimersapp.R;
import com.example.alzheimersapp.ViewModelFactory;
import com.example.alzheimersapp.data.rxfirestore.errors.DocumentNotExistsException;
import com.example.alzheimersapp.di.component.AppComponent;
import com.example.alzheimersapp.di.module.AppModule;
import com.example.alzheimersapp.model.SharedLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.MapView;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrackLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Marker mMyLocMarker;
    private GoogleMap mGoogleMap;
    private static final float DEFAULT_ZOOM_LEVEL = 16f;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.text_dev_id)
    EditText mTextDevId;
    @BindView(R.id.btn_track)
    Button mBtnTrack;
    @BindView(R.id.text_loc_stat)
    TextView mTextLocStat;

    @Inject
    ViewModelFactory mViewModelFactory;
    TrackLocationViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_location);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.track_location);
        }

//
        ((App) getApplicationContext()).getAppComponent().inject(this);
        initViewModel();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.stopTracking();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
    }

    public void onStartTrackClick(View view) {
        if (mViewModel.isTracking()) {
            mViewModel.stopTracking();
            return;
        }

        String devId = mTextDevId.getText().toString();
        if (devId.isEmpty()) {
            Toast.makeText(this, "Please insert valid Device Id", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Start tracking...", Toast.LENGTH_SHORT).show();
        mViewModel.getLocationUpdate(devId)
                .subscribe(this::locationUpdated, throwable -> {
                    if (throwable instanceof DocumentNotExistsException) {
                        Toast.makeText(TrackLocationActivity.this, "Data not found/Disconnected", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void locationUpdated(SharedLocation sharedLocation) {
        LatLng pos = new LatLng(sharedLocation.getLocation().latitude, sharedLocation.getLocation().longitude);
        if (mGoogleMap != null && mMyLocMarker == null) {
            MarkerOptions options = new MarkerOptions()
                    .title(sharedLocation.getDevId())
                    .position(pos);
            mMyLocMarker = mGoogleMap.addMarker(options);
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, DEFAULT_ZOOM_LEVEL));
        } else if (mMyLocMarker != null) {
            mMyLocMarker.setPosition(pos);
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(pos));
        }

        mTextLocStat.setText(pos.toString());
    }



    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(TrackLocationViewModel.class);
        mViewModel.getTrackingState().observe(this, tracking -> {
            if (tracking != null && tracking) {
                mTextDevId.setEnabled(false);
                mBtnTrack.setBackground(getResources().getDrawable(R.drawable.bg_btn_stop));
                mBtnTrack.setText(R.string.stop);
                mTextLocStat.setText(R.string.fetching);
            } else {
                mTextDevId.setEnabled(true);
                mBtnTrack.setBackground(getResources().getDrawable(R.drawable.bg_btn_start));
                mBtnTrack.setText(R.string.start);
                mTextLocStat.setText(R.string.disconnected);
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        startActivity(new Intent(this, CaregiverDashboard.class));
//        finish();
//    }



}
