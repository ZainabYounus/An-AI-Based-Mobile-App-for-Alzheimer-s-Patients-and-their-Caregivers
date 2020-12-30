package com.example.alzheimersapp.PatientModule.Chatbot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alzheimersapp.PatientModule.PatientDashboard;
import com.example.alzheimersapp.R;

import java.util.ArrayList;
import java.util.List;

import io.kommunicate.KmConversationBuilder;
import io.kommunicate.Kommunicate;
import io.kommunicate.callbacks.KmCallback;

public class chatbot extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Kommunicate.init(getApplicationContext(), "1bf1baf8cfe7e8856d84f4100b5364ddb");

        List<String> agentList = new ArrayList();
        agentList.add("dialogflow-qvyslj@chatbot-xlnn.iam.gserviceaccount.com"); //add your agentID
        List<String> botList = new ArrayList();
        botList.add("doby-qbndn"); //enter your integrated bot Id

        new KmConversationBuilder(this)
                .launchConversation(new KmCallback() {
                    @Override
                    public void onSuccess(Object message) {
                        Log.d("Conversation", "Success : " + message);
                    }

                    @Override
                    public void onFailure(Object error) {
                        Log.d("Conversation", "Failure : " + error);
                    }
                });

    }
}
