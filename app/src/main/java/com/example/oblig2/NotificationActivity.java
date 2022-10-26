package com.example.oblig2;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvDate = findViewById(R.id.tvDate);
        TextView tvTime = findViewById(R.id.tvTime);
        TextView tvPlace = findViewById(R.id.tvPlace);
        TextView tvMsg = findViewById(R.id.tvMsg);

        String title = getIntent().getStringExtra("EXTRA_TITLE");
        String date = getIntent().getStringExtra("EXTRA_DATE");
        String time = getIntent().getStringExtra("EXTRA_TIME");
        String place = getIntent().getStringExtra("EXTRA_PLACE");
        String msg = getIntent().getStringExtra("EXTRA_MSG");

        tvTitle.setText(title);
        tvDate.setText(date);
        tvTime.setText(time);
        tvPlace.setText(place);
        tvMsg.setText(msg);
    }
}
