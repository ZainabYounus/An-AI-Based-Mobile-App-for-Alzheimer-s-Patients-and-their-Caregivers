package com.example.alzheimersapp.di.module;

import android.app.Application;
import android.content.Context;



import com.example.alzheimersapp.ViewModelFactory;
import com.example.alzheimersapp.data.DeviceLocationDataStore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import com.example.alzheimersapp.sharelocation.ShareLocationViewModel;
import com.example.alzheimersapp.tracklocation.TrackLocationViewModel;

@Module
public class AppModule {

    public Application mApplication;


    public AppModule(Application app) {
        mApplication = app;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    ShareLocationViewModel provideShareLocationViewModel() {
        return new ShareLocationViewModel(mApplication, new DeviceLocationDataStore());
    }

    @Provides
    @Singleton
    TrackLocationViewModel provideTrackLocationViewModel() {
        return new TrackLocationViewModel(new DeviceLocationDataStore());
    }

    @Provides
    @Singleton
    ViewModelFactory provideViewModelFactory(
            ShareLocationViewModel shareLocationViewModel,
            TrackLocationViewModel trackLocationViewModel) {
        return new ViewModelFactory(shareLocationViewModel, trackLocationViewModel);
    }

}
