package com.wqz.vistamanager;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;

import com.wqz.base.BaseImmersiveActivity;
import com.wqz.view.MyVideoView;

public class SplashActivity extends BaseImmersiveActivity
{

    MyVideoView splashVideo;

    @Override
    protected void onInitUI()
    {
        setContentView(R.layout.activity_splash);
        splashVideo = (MyVideoView)findViewById(R.id.vv_splash);
    }

    @Override
    protected void onSetListener()
    {
        splashVideo.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" +R.raw.qidong));
        splashVideo.start();
        splashVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                startActivity(new Intent(SplashActivity.this,
                        LoginActivity.class));
                overridePendingTransition(R.anim.fade, R.anim.hold);
                SplashActivity.this.finish();
            }
        });
    }

    @Override
    protected void onSetDataLogic()
    {

    }
}
