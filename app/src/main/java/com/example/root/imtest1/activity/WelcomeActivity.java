package com.example.root.imtest1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AnimationUtils;

import com.example.root.imtest1.R;
import com.example.root.imtest1.view.SecretTextView;

public class WelcomeActivity extends AppCompatActivity {
    private SecretTextView welcome_title;

    //欢迎界面,打开ａｐｐ会进入的界面
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        welcome_title = (SecretTextView) findViewById(R.id.welcome_title);
        findViewById(R.id.welcome_title).startAnimation(AnimationUtils.loadAnimation(this, R.anim.welcome_hint_bottom_in));
        welcome_title.setmDuration(1500);
        welcome_title.toggle();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                WelcomeActivity.this.finish();
            }
        }, 2000);
    }
}
