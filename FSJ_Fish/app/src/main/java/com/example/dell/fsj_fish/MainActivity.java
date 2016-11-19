package com.example.dell.fsj_fish;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements DialogInterface.OnClickListener{
    private GameView view;
    private SoundPool pool=null;//声音播放池
    private int sound_bgm = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      view = new GameView(this);
        setContentView(view);
        pool = new SoundPool(10, AudioManager.STREAM_SYSTEM,0);
        sound_bgm=pool.load(getApplicationContext(),R.raw.bgm,1);
        /*if(view.getsiwang() == true){
            Intent intent = new Intent(MainActivity.this,EndActivity.class);
            startActivity(intent);
            finish();
        }*/

        pool.play(sound_bgm,0.3f,0.3f,0,1,1);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            view.stop();
            AlertDialog.Builder alert=new AlertDialog.Builder(this);
            alert.setTitle("你要退出吗？");
            alert.setNeutralButton("退出",this);
            alert.setNegativeButton("取消",this);
            alert.create().show();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == -2){
            view.start();
        }else{
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

}
