package com.example.pc.ffmpegrecord;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FFmpeg fFmpeg = new FFmpeg();
        ((TextView)findViewById(R.id.click)).setText(fFmpeg.test123());
        findViewById(R.id.click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoRecorder.start(MainActivity.this);

            }
        });
    }
}
