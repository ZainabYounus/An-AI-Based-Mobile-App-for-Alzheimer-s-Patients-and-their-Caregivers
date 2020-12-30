package com.example.alzheimersapp;

import android.app.Application;

import com.example.alzheimersapp.di.component.AppComponent;

import com.example.alzheimersapp.di.component.DaggerAppComponent;
import com.example.alzheimersapp.di.module.AppModule;


public class App extends Application {

    public AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        AppModule appModule = new AppModule(this);
        mAppComponent = DaggerAppComponent.builder().appModule(appModule).build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
