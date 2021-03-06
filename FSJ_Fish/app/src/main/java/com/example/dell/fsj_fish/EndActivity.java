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
import android.widget.TextView;

public class EndActivity extends AppCompatActivity {
    private Button rekaishi;
    private Button tuichu;
    private SoundPool pool=null;//声音播放池
    private int sound_dianji=0;
    private int sound_bgm=0;
    private long fenshu=0;
    private String jieguo = null;
    private TextView fenshu_text = null;
    private TextView jieguo_text = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        rekaishi = (Button) findViewById(R.id.rekaishi);
        tuichu = (Button) findViewById(R.id.tuichu);
        fenshu_text = (TextView) findViewById(R.id.fenshu);
        jieguo_text = (TextView) findViewById(R.id.jieguo);
        pool=new SoundPool(10, AudioManager.STREAM_SYSTEM,0);
        sound_dianji=pool.load(getApplicationContext(),R.raw.dianji,1);
        sound_bgm=pool.load(getApplicationContext(),R.raw.ready_bgm,1);
        Intent intent = getIntent();
        fenshu = intent.getLongExtra("fenshu",-1);
        jieguo = intent.getStringExtra("jieguo");

        if (jieguo!=null){
            jieguo_text.setText(jieguo);
        }
        if (fenshu!=-1){
            fenshu_text.setText("获得分数： "+fenshu);
        }
       pool.play(sound_bgm,1,1,0,1,1);
        rekaishi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pool.play(sound_dianji,1,1,1,0,1);
                Intent intent = new Intent(EndActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        tuichu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pool.play(sound_dianji,1,1,1,0,1);
                AlertDialog.Builder alert=new AlertDialog.Builder(EndActivity.this);
                alert.setTitle("你要退出吗？");
                alert.setNeutralButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
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
