package com.example.alzheimersapp.sharelocation;

import android.app.Application;
import android.location.Location;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.alzheimersapp.data.DeviceLocationDataStore;
import com.github.abdularis.rxlocation.RxLocation;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Observable;

public class ShareLocationViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> mSharingState;
    private Location mLastLocation;
    private Flowable<Location> mLocationUpdatesObserver;
    private DeviceLocationDataStore mDeviceLocationDataStore;

    @Inject
    public ShareLocationViewModel(Application application,
                                  DeviceLocationDataStore deviceLocationDataStore) {
        super(application);
        mDeviceLocationDataStore = deviceLocationDataStore;
        mSharingState = new MutableLiveData<>();
    }

    public void startSharingLocation() {
        mSharingState.setValue(true);
        mDeviceLocationDataStore.shareMyLocation(getLocationUpdates());
    }

    public void stopSharingLocation() {
        if (isSharing()) {
            mSharingState.setValue(false);
            mDeviceLocationDataStore.stopShareMyLocation().subscribe();
        }
    }

    public Flowable<Location> getLocationUpdates() {
        if (mLocationUpdatesObserver != null) return mLocationUpdatesObserver;

        mLocationUpdatesObserver = RxLocation.getLocationUpdatesBuilder(getApplication().getApplicationContext())
                .setInterval(1000)
                .build()
                .doOnNext(location -> {
                    mLastLocation = location;
                });
        return mLocationUpdatesObserver;
    }

    public MutableLiveData<Boolean> getSharingStateLiveData() {
        return mSharingState;
    }

    public boolean isSharing() {
        return mSharingState.getValue() != null && mSharingState.getValue();
    }

    public Observable<String> getDeviceIdObservable() {
        return mDeviceLocationDataStore.getDeviceId();
    }

    public Location getLastCachedLocation() {
        return mLastLocation;
    }
}
