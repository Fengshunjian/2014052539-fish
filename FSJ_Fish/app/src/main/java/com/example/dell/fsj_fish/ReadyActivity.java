package com.example.dell.fsj_fish;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ReadyActivity extends AppCompatActivity {
    private Button kaishi;
    private Button jieshu;
    private SoundPool pool=null;//声音播放池
    private int sound_dianji=0;
    private int sound_bgm=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready);
        kaishi = (Button) findViewById(R.id.kaishi);
        jieshu = (Button) findViewById(R.id.jieshu);
        pool=new SoundPool(10, AudioManager.STREAM_SYSTEM,0);
        sound_dianji=pool.load(getApplicationContext(),R.raw.dianji,1);
        sound_bgm=pool.load(getApplicationContext(),R.raw.ready_bgm,1);

        pool.play(sound_bgm,1,1,0,1,1);
        kaishi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pool.play(sound_dianji,1,1,1,0,1);
                Intent intent = new Intent(ReadyActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        jieshu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pool.play(sound_dianji,1,1,1,0,1);
                AlertDialog.Builder alert=new AlertDialog.Builder(ReadyActivity.this);
                alert.setTitle("你要退出吗？");
                alert.setNeutralButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.create().show();
            }
        });
    }
}
