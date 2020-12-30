package com.example.alzheimersapp;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;




import javax.inject.Inject;

import com.example.alzheimersapp.tracklocation.TrackLocationViewModel;
import com.example.alzheimersapp.sharelocation.ShareLocationViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private ShareLocationViewModel mShareLocationViewModel;
    private TrackLocationViewModel mTrackLocationViewModel;

    @Inject
    public ViewModelFactory(ShareLocationViewModel shareLocationViewModel,
                            TrackLocationViewModel trackLocationViewModel) {
        mShareLocationViewModel = shareLocationViewModel;
        mTrackLocationViewModel = trackLocationViewModel;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ShareLocationViewModel.class)) {
            return (T) mShareLocationViewModel;
        } else if (modelClass.isAssignableFrom(TrackLocationViewModel.class)) {
            return (T) mTrackLocationViewModel;
        }

        throw new IllegalArgumentException("Unknown view model type");
    }
}
