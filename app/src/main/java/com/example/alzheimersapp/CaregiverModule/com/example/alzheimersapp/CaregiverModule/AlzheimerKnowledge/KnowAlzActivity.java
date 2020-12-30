package com.example.alzheimersapp.CaregiverModule.AlzheimerKnowledge;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.alzheimersapp.CaregiverModule.CaregiverDashboard;
import com.example.alzheimersapp.R;

import androidx.appcompat.app.AppCompatActivity;

public class KnowAlzActivity extends AppCompatActivity {

    TextView sb_one, sb_two, sb_three, sb_four, sb_five, sb_six, sb_seven;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_alz);

        sb_one = findViewById(R.id.stageOneText);
        sb_two = findViewById(R.id.stageTwoText);
        sb_three = findViewById(R.id.stageThreeText);
        sb_four= findViewById(R.id.stageFourText);
        sb_five = findViewById(R.id.stageFiveText);
        sb_six = findViewById(R.id.stageSixText);
        sb_seven = findViewById(R.id.stageSevenText);

        setTexts();
    }

    public void setTexts(){
        sb_one.setText(R.string.first_stage);

        sb_two.setText(R.string.second_stage);

        sb_three.setText(R.string.third_stage);

        sb_four.setText(R.string.fourth_stage);

        sb_five.setText(R.string.fifth_stage);

        sb_six.setText(R.string.sixth_stage);

        sb_seven.setText(R.string.seventh_stage);
    }

//    @Override
//    public void onBackPressed() {
//        startActivity(new Intent(this, CaregiverDashboard.class));
//        finish();
//    }
}