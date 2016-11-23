package com.example.dell.fsj_fish;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EndActivity extends AppCompatActivity {
    private Button rekaishi;
    private Button tuichu;
    private SoundPool pool=null;//声音播放池
    private int sound_dianji=0;
   // private int sound_bgm=0;
    private long fenshu=0;
    private String jieguo = null;
    private TextView fenshu_text = null;
    private TextView jieguo_text = null;
    private Button fenxiang;
    private EditText number_e;
    private EditText content_e;
    private String content;
    private String number;
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
       // sound_bgm=pool.load(getApplicationContext(),R.raw.ready_bgm,1);
        fenxiang = (Button) findViewById(R.id.fenxiang);
        Intent intent = getIntent();
        fenshu = intent.getLongExtra("fenshu",-1);
        jieguo = intent.getStringExtra("jieguo");

        if (jieguo!=null){
            jieguo_text.setText(jieguo);
        }
        if (fenshu!=-1){
            fenshu_text.setText("获得分数： "+fenshu);
        }
       //pool.play(sound_bgm,1,1,0,1,1);
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
        fenxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LayoutInflater inflater = getLayoutInflater();
                final View layout = inflater.inflate(R.layout.duanxinfenxiang,
                        (ViewGroup) findViewById(R.id.duanxinfenxiang) );
                number_e = (EditText)layout.findViewById(R.id.number);
                content_e = (EditText)layout.findViewById(R.id.content);
                AlertDialog.Builder alert = new AlertDialog.Builder(EndActivity.this);
                alert.setTitle("分享");
                alert.setView(layout);
                alert.setNeutralButton("确定", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        number = number_e.getText().toString();
                        content = content_e.getText().toString();
                        /*if (!number.equals(null)&&!content.equals(null)) {
                            faduanxin(number, content);
                        }else{
                            Toast.makeText(getApplicationContext(),"信息不能为空",Toast.LENGTH_LONG).show();
                        }*/
                        faduanxin(number, content);
                    }
                });
                alert.setNegativeButton("取消", null);
                alert.create().show();
            }
        });
    }
    //发短信函数
    public void faduanxin(String phone,String context){
        SmsManager manager = SmsManager.getDefault();
        ArrayList<String> list = manager.divideMessage(context);  //因为一条短信有字数限制，因此要将长短信拆分
        for (String text : list) {
            manager.sendTextMessage(phone, null, text, null, null);
        }

        Toast.makeText(getApplicationContext(), "发送完毕", Toast.LENGTH_SHORT).show();
    }
}
