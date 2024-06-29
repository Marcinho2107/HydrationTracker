package com.example.hydrationtracker_git;

import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class ProgressActivity extends AppCompatActivity {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        Intent intent = getIntent();
        if (intent != null) {
            mParam1 = intent.getStringExtra(ARG_PARAM1);
            mParam2 = intent.getStringExtra(ARG_PARAM2);
        }
    }

    public static Intent newIntent(Context context, String param1, String param2){
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(ARG_PARAM1, param1);
        intent.putExtra(ARG_PARAM2, param2);
        return intent;
    }
}