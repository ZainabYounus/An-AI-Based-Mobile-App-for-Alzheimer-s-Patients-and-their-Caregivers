package com.example.alzheimersapp.di.component;

import android.app.Application;

import com.example.alzheimersapp.di.module.AppModule;
import com.example.alzheimersapp.sharelocation.ShareLocationActivity;


import javax.inject.Singleton;

import dagger.Component;
import com.example.alzheimersapp.tracklocation.TrackLocationActivity;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(ShareLocationActivity client);

    void inject(TrackLocationActivity client);

}
