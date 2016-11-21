package com.example.dell.fsj_fish;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by DELL on 2016/11/10.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback,Runnable,android.view.View.OnTouchListener{

    private Bitmap my;//自己的鱼
    private Bitmap my1;//自己的鱼,向左
    private Bitmap my2;//自己的鱼,100分
    private Bitmap bg;//背景
    private Bitmap play;//暂停标志
    private Bitmap play1;//暂停标志
    private Bitmap jingzhi;//静止标志
    private Bitmap qingping;//清屏标志
    private Bitmap diren;//敌人
    private Bitmap diren1;//敌人,向右
    private Bitmap diren3;//敌人1
    private Bitmap diren4;//敌人1,向右
    private Bitmap erjihuancun;//二级缓存
    //private WindowManager windoeManager;
    private int display_w;
    private int display_h;
    private ArrayList<GameImage> gameImages = new ArrayList<>();
    private ArrayList<direnImage> gameImages_diren = new ArrayList<>();
    //private ArrayList<zantingImage> gameImages_zanting = new ArrayList<>();
    private boolean state = false;
    private SurfaceHolder holder;
    private int my_width;
    private int my_height;
    private int huadong;
    private long fenshu=0;
    private int direnspeed = 5;
    private int bgspeed = 3;
    private int jingzhishijian =0;
    private boolean state1=false;
    private SoundPool pool=null;//声音播放池
    private int sound_bgm = 0 ;
    private int sound_chi=0;
    private int sound_dianji=0;
    private boolean siwangstate=false;
    private MainActivity mainActivity=new MainActivity();

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        this.setOnTouchListener(this);//事件注册



    }
    public void init(){
        my = BitmapFactory.decodeResource(getResources(),R.drawable.my);
        my1 = BitmapFactory.decodeResource(getResources(),R.drawable.my1);
       // my2 = BitmapFactory.decodeResource(getResources(),R.drawable.my2);
        bg = BitmapFactory.decodeResource(getResources(),R.drawable.bg);
        diren = BitmapFactory.decodeResource(getResources(),R.drawable.diren);
        diren1 = BitmapFactory.decodeResource(getResources(),R.drawable.diren1);
        diren3 = BitmapFactory.decodeResource(getResources(),R.drawable.diren3);
        diren4 = BitmapFactory.decodeResource(getResources(),R.drawable.diren4);
        play = BitmapFactory.decodeResource(getResources(),R.drawable.play);
        play1 = BitmapFactory.decodeResource(getResources(),R.drawable.play1);
        jingzhi = BitmapFactory.decodeResource(getResources(),R.drawable.jingzhi);

        qingping = BitmapFactory.decodeResource(getResources(),R.drawable.qingping);
        //生产二级缓存照片
        erjihuancun = Bitmap.createBitmap(display_w,display_h, Bitmap.Config.ARGB_8888);

        gameImages.add(new bgImage(bg));
        //gameImages.add(new direnImage(diren));
        gameImages.add(new myImage(my,my1));
        gameImages.add(new zantingImage(play,play1));
        jingzhiImage jingzhiimage = new jingzhiImage(jingzhi);
        gameImages.add(jingzhiimage);
        qingpingImage qingpingimage = new qingpingImage(qingping);
        gameImages.add(qingpingimage);
       //加载声音
        pool = new SoundPool(10, AudioManager.STREAM_SYSTEM,0);
        //sound_bgm=pool.load(getContext(),R.raw.bgm,1);
        sound_chi=pool.load(getContext(),R.raw.chi,1);
        sound_dianji=pool.load(getContext(),R.raw.dianji_game,1);
    }
    //暂停
    private boolean stopState = false;
    public void stop(){
        stopState = true;
    }
    //开始
    public void start(){
        stopState = false;
        thread.interrupt();
    }
//判断是否死亡
   /* public boolean getsiwang(){
        return siwangstate;
    }
    public void setsiwang(boolean siwang){
        this.siwangstate = siwang;
    }*/

    //声音播放线程
    private class sound_play extends Thread{
        int i=0;
        int l=0;
        public sound_play(int i,int l){
            this.i=i;
            this.l=l;
        }
        public void run(){
            pool.play(i,1,1,1,l,1);
        }
    }


    //速度设置
    public void setdirenspeed(int ds){
        this.direnspeed = ds;
    }
    public void setbgspeed(int bs){
        this.bgspeed = bs;
    }
    public int getdirenspeed(){return direnspeed;}
    public int getbgspeed(){return bgspeed;}
    public void run() {
        Paint p1 = new Paint();
        int diren_num = 0;
        Paint p2 = new Paint();
        p2.setColor(Color.RED);
        p2.setTextSize(65);
        p2.setDither(true);
        p2.setAntiAlias(true);
        try{
            while(state){
                while (stopState){
                    try {
                        Thread.sleep(1000000);
                    }catch (Exception e){

                    }
                }

                Canvas newCanvas = new Canvas(erjihuancun);
                for(GameImage image : (List<GameImage>)gameImages.clone()){
                    if(image instanceof myImage){
                        ((myImage) image).shoudaogongji(gameImages_diren);
                    }
                    newCanvas.drawBitmap(image.getBitmap(),image.getX(),image.getY(),p1);
                }

                for(GameImage image_diren : (List<GameImage>)gameImages_diren.clone()){
                    newCanvas.drawBitmap(image_diren.getBitmap(),image_diren.getX(),image_diren.getY(),p1);
                }

                /*for(GameImage image_zanting:(List<GameImage>)gameImages_zanting.clone()){
                    newCanvas.drawBitmap(image_zanting.getBitmap(),image_zanting.getX(),image_zanting.getY(),p1);
                }*/
                //分数
                newCanvas.drawText("分数："+fenshu,100,65,p2);

                if (fenshu>50&&getdirenspeed()!=0) {
                    if (diren_num == 120) {
                        gameImages_diren.add(new direnImage(diren, diren1,1));
                        gameImages_diren.add(new direnImage(diren3, diren4,0));
                        diren_num = 0;
                    }
                    if (diren_num == 80) {
                        gameImages_diren.add(new direnImage(diren3, diren4,1));
                        gameImages_diren.add(new direnImage(diren, diren1,0));
                    }
                    diren_num++;
                }else if(getdirenspeed()!=0){
                    if (diren_num == 50) {
                        gameImages_diren.add(new direnImage(diren, diren1,0));
                        gameImages_diren.add(new direnImage(diren3, diren4,0));
                        diren_num = 0;
                    }
                    diren_num++;
                }
                if(getdirenspeed()==0||getbgspeed()==0){
                    jingzhishijian++;
                    if (jingzhishijian==150){
                        setdirenspeed(5);
                        setbgspeed(1);
                        jingzhishijian = 0;
                    }
                }
                Canvas canvas = holder.lockCanvas();
                canvas.drawBitmap(erjihuancun,0,0,p1);
                holder.unlockCanvasAndPost(canvas);
                Thread.sleep(0);
            }
        }catch (Exception e){

        }

    }

    //点击按中战机
    myImage selectFish;
    zantingImage selectzanting=null;
    //jingzhiImage jingzhiimg = (jingzhiImage) gameImages.get(3);
    private boolean zuoState=false;
    private int panduan = display_w/2;
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            for(GameImage game:gameImages) {
                if (game instanceof myImage) {
                    myImage myfish = (myImage) game;
                    if (myfish.getX() < event.getX()
                            && myfish.getY() < event.getY()
                            && myfish.getX() + myfish.getmy_width() + myfish.getkuan() > event.getX()
                            && myfish.getY() + myfish.getmy_height() + myfish.getgao() > event.getY()) {
                        selectFish = myfish;
                        // Toast.makeText(getContext(),"选中",Toast.LENGTH_LONG).show();
                    } else {
                        selectFish = null;
                    }
                    break;
                }
            }
            for(GameImage game:gameImages){
                if(game instanceof zantingImage){
                    zantingImage ztI=(zantingImage)game;
                    if(ztI.getX()<event.getX()
                            &&ztI.getY()<event.getY()
                            &&ztI.getX()+play.getWidth()>event.getX()
                            &&ztI.getY()+play.getHeight()>event.getY()){
                        selectzanting = ztI;
                        if(stopState==false){
                            new sound_play(sound_dianji,0).start();
                           // pool.play(sound_dianji,1,1,1,0,1);
                            selectzanting.setZantingshu(1);
                            stop();
                        }else{
                            new sound_play(sound_dianji,0).start();
                            //pool.play(sound_dianji,1,1,1,0,1);
                            selectzanting.setZantingshu(0);
                            start();
                        }
                        //Toast.makeText(getContext(),"暂停",Toast.LENGTH_LONG).show();
                    }else {
                        selectzanting=null;
                    }
                    break;
                }
            }
            if (display_w-jingzhi.getWidth()<event.getX()
                    &&10<event.getY()
                    &&display_w>event.getX()
                    &&10+jingzhi.getHeight()>event.getY()){
                new sound_play(sound_dianji,0).start();
                //pool.play(sound_dianji,1,1,1,0,1);
                setbgspeed(0);
                setdirenspeed(0);
            }
            if (display_w-qingping.getWidth()-jingzhi.getWidth()-20<event.getX()
                    &&10<event.getY()
                    &&display_w-jingzhi.getWidth()-20>event.getX()
                    &&10+qingping.getHeight()>event.getY()){
                new sound_play(sound_dianji,0).start();
                //pool.play(sound_dianji,1,1,1,0,1);
                for(GameImage diren_d : (List<GameImage>) gameImages_diren.clone()){
                    direnImage diren_dd = (direnImage) diren_d;
                    gameImages_diren.remove(diren_dd);
                    fenshu+=10*gameImages_diren.size();
                }
            }

        }

        if (event.getAction()==MotionEvent.ACTION_MOVE){
            if (selectFish!=null){
                if(selectFish.getX()<panduan&&zuoState == false){
                    selectFish.setmyimg(my1);

                    zuoState = true;
                }else if (selectFish.getX()>panduan&&zuoState == true){
                    selectFish.setmyimg(my);

                    zuoState = false;
                }
                panduan = selectFish.getX();
                selectFish.setX((int)event.getX()-(selectFish.getmy_width()+selectFish.getkuan())/2);
                selectFish.setY((int)event.getY()-(selectFish.getmy_height()+selectFish.getgao())/2);

            }
        }
        if (event.getAction()==MotionEvent.ACTION_UP){
            selectFish=null;


        }
        return true;
    }

    private interface GameImage{
        public Bitmap getBitmap();
        public int getX();
        public int getY();
    }
    //暂停开始
    private class zantingImage implements GameImage{

       private Bitmap play;
        private Bitmap play1;
        private Bitmap playBitmap;

        private Paint p=new Paint();
        private int zantingshu=0;
        public zantingImage(Bitmap play,Bitmap play1) {
            this.play = play;
            this.play1 = play1;
            play1 = Bitmap.createBitmap(play,0,0,play1.getWidth(),play1.getHeight());
            play = Bitmap.createBitmap(play,0,0,play.getWidth(),play.getHeight());
        }

        public Bitmap getBitmap() {
            if (getZantingshu()==1){
                playBitmap=play1;
            }else if(getZantingshu()==0){
                playBitmap=play;
            }

           // Canvas canvas = new Canvas(playBitmap);
           // canvas.drawBitmap(playBitmap,new Rect(0,0,playBitmap.getWidth(),playBitmap.getHeight()),new Rect(getX(),getY(),getX()+playBitmap.getWidth(),getY()+playBitmap.getHeight()),p);
            return playBitmap;

        }
        public void setZantingshu(int zanshu){
            this.zantingshu = zanshu;
        }
        public int getZantingshu(){return zantingshu;}
        public int getX() {
            return 10;
        }


        public int getY() {
            return 10;
        }
    }

    //敌人照片处理
    private class direnImage implements GameImage{
        private Bitmap diren = null;
        private Bitmap diren0 = null;
        private Bitmap diren1 = null;
        private int x=0;
        private int y=0;
        private Bitmap direnBitmap;
        private Paint p =new Paint();
        private int daxiao=0;
        private  int r = 0;
        private direnImage(Bitmap diren0,Bitmap diren1,int i){
            this.diren0=diren0;
            this.diren1=diren1;
            setdiren(diren0);
            Random ran = new Random();
            Random ran1 = new Random();
            Random ran2 = new Random();
            r = 0;
            r = ran2.nextInt(2);
            if (r==1){
                x = display_w;
                setdiren(diren0);
            }else{
                x = 0-getdirenw();
                setdiren(diren1);
            }
            if (i==1) {
                setdaxiao((int) (fenshu/10)*ran1.nextInt(50));
                if(getdaxiao()>(display_h/5)*3){
                    setdaxiao(0);
                }
            } else {
                setdaxiao(0);
            }
            y=ran.nextInt(display_h-getdirenh());
        }
        public Bitmap getBitmap() {

            if (r == 1) {
                x-=getdirenspeed();
                if (x<0-diren.getWidth()){
                    gameImages.remove(this);
                    this.diren=getdiren();
                }
            }else{
                x+=getdirenspeed();
                if (x>display_w){
                    gameImages.remove(this);
                    this.diren=getdiren();
                }
            }
            direnBitmap = Bitmap.createBitmap(diren.getWidth() + getdaxiao(), diren.getHeight() + getdaxiao(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(direnBitmap);
            canvas.drawBitmap(diren, new Rect(0, 0, diren.getWidth(), diren.getHeight()), new Rect(0, 0, diren.getWidth() + getdaxiao(), diren.getHeight() + getdaxiao()), p);
            return direnBitmap;
        }
        public int getdaxiao(){return daxiao;}
        public void setdaxiao(int daxiao){this.daxiao=daxiao;}

        public Bitmap getdiren(){return diren;}
        public void setdiren(Bitmap diren){
            this.diren = diren;
        }
        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
        public int getdirenw(){
            return diren.getWidth()+getdaxiao();
        }
        public int getdirenh(){
            return diren.getHeight()+getdaxiao();
        }
    }

    //负责自己照片处理
    private class myImage implements GameImage{

        private Bitmap my;
        private Bitmap my1;
        private Bitmap my0;
        private Bitmap myBitmap = null;
        private int x=0;
        private int y=0;
        private int kuan=0;
        private int gao=0;
        private boolean shougongjiState = false;
        private Paint p = new Paint();
        private myImage(Bitmap my0,Bitmap my1){
            this.my0 = my0;
            this.my1= my1;
            setmyimg(my0);
            //myBitmap = Bitmap.createBitmap(my.getWidth()+getkuan(),my.getHeight()+getgao(), Bitmap.Config.ARGB_8888);
            //得到自己鱼的宽高
            my_width = my.getWidth();
            my_height = my.getHeight();

            x=display_w/2;
            y=display_h/2;
        }

        public Bitmap getBitmap() {
            this.my=getmyimg();
            myBitmap = Bitmap.createBitmap(my.getWidth()+getkuan(),my.getHeight()+getgao(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(myBitmap);
            canvas.drawBitmap(my,new Rect(0,0,my.getWidth(),my.getHeight()),new Rect(0,0,getw(),geth()),p);

            return myBitmap;
        }

        //受到攻击

        public void shoudaogongji(ArrayList<direnImage> direns){
            //if(!shougongjiState){
            int d =0;
                if(getw()>display_w||geth()>display_h){
                    Intent intent = new Intent(getContext(), EndActivity.class);
                    intent.putExtra("fenshu",fenshu);
                    intent.putExtra("jieguo","恭喜你成为海王霸主");
                    getContext().startActivity(intent);
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
                for (GameImage diren_d : (List<GameImage>) direns.clone()) {
                    direnImage diren_dd = (direnImage) diren_d;
                    d=(int)(diren_dd.getdirenw()*0.4);
                    if ((diren_dd.getX() > x + d && diren_dd.getY() > y + d
                            && diren_dd.getX() < x + getw()
                            && diren_dd.getY() < y + geth())
                            ) {
                        new sound_play(sound_chi,0).start();
                        if (getw() * geth() < diren_dd.getdirenw() * diren_dd.getdirenh()) {
                            if(getdirenspeed()!=0){
                            gameImages.remove(this);
                            Intent intent = new Intent(getContext(), EndActivity.class);
                                intent.putExtra("fenshu",fenshu);
                                intent.putExtra("jieguo","游戏失败");
                            getContext().startActivity(intent);
                            android.os.Process.killProcess(android.os.Process.myPid());

                           //     pool.play(sound_chi,1,1,1,0,1);
                            // Toast.makeText(getContext(),"被击中",Toast.LENGTH_LONG).show();
                            //stop();
                            }
                        } else {
                            gameImages_diren.remove(diren_dd);
                            fenshu += 10;
                            setkuan(getkuan() + (int) (diren_dd.getX() * 0.03));
                            setgao(getgao() + (int) (diren_dd.getY() * 0.03));
                            //shougongjiState = true;
                        }
                        break;
                    } else if (diren_dd.getX() + diren_dd.getdirenw() > x + d && diren_dd.getY() + diren_dd.getdirenh() > y + d
                            && diren_dd.getX() + diren_dd.getdirenw() < x + getw()
                            && diren_dd.getY() + diren_dd.getdirenh() < y + geth()) {
                        new sound_play(sound_chi,0).start();
                        if (getw() * geth() < diren_dd.getdirenw() * diren_dd.getdirenh()) {
                            if(getdirenspeed()!=0) {
                                gameImages.remove(this);
                                Intent intent = new Intent(getContext(), EndActivity.class);
                                intent.putExtra("fenshu",fenshu);
                                intent.putExtra("jieguo","游戏失败");
                                getContext().startActivity(intent);
                                android.os.Process.killProcess(android.os.Process.myPid());

                                // pool.play(sound_chi,1,1,1,0,1);
                                // Toast.makeText(getContext(),"被击中",Toast.LENGTH_LONG).show();
                                //stop();
                            }
                        } else {
                            gameImages_diren.remove(diren_dd);
                            fenshu += 10;
                            setkuan(getkuan() + (int) (diren_dd.getX() * 0.03));
                            setgao(getgao() + (int) (diren_dd.getY() * 0.03));
                        }
                        break;
                    } else if (diren_dd.getX() + diren_dd.getdirenw() > x + d && diren_dd.getY() > y + d
                            && diren_dd.getX() + diren_dd.getdirenw() < x + getw()
                            && diren_dd.getY() < y + geth()) {
                        new sound_play(sound_chi,0).start();
                        if (getw() * geth() < diren_dd.getdirenw() * diren_dd.getdirenh()) {
                            if(getdirenspeed()!=0) {
                                gameImages.remove(this);
                                Intent intent = new Intent(getContext(), EndActivity.class);
                                intent.putExtra("fenshu",fenshu);
                                intent.putExtra("jieguo","游戏失败");
                                getContext().startActivity(intent);
                                android.os.Process.killProcess(android.os.Process.myPid());

                               // pool.play(sound_chi,1,1,1,0,1);
                                // Toast.makeText(getContext(),"被击中",Toast.LENGTH_LONG).show();
                                //stop();
                            }
                        } else {
                            gameImages_diren.remove(diren_dd);
                            fenshu += 10;
                            setkuan(getkuan() + (int) (diren_dd.getX() * 0.03));
                            setgao(getgao() + (int) (diren_dd.getY() * 0.03));
                        }
                        break;
                    } else if (diren_dd.getX() > x + d && diren_dd.getY() + diren_dd.getdirenh() > y + d
                            && diren_dd.getX() < x + getw()
                            && diren_dd.getY() + diren_dd.getdirenh() < y + geth()) {
                        new sound_play(sound_chi,0).start();
                        if (getw() * geth() < diren_dd.getdirenw() * diren_dd.getdirenh()) {
                            if(getdirenspeed()!=0) {
                                gameImages.remove(this);
                                Intent intent = new Intent(getContext(), EndActivity.class);
                                intent.putExtra("fenshu",fenshu);
                                intent.putExtra("jieguo","游戏失败");
                                getContext().startActivity(intent);
                                android.os.Process.killProcess(android.os.Process.myPid());

                                //pool.play(sound_chi,1,1,1,0,1);
                                // Toast.makeText(getContext(),"被击中",Toast.LENGTH_LONG).show();
                                //stop();
                            }
                        } else {
                            gameImages_diren.remove(diren_dd);
                            fenshu += 10;
                            setkuan(getkuan() + (int) (diren_dd.getX() * 0.03));
                            setgao(getgao() + (int) (diren_dd.getY() * 0.03));
                        }
                        break;
                    }
                }

            //}
        }
        //public boolean getshougongjiState(){return shougongjiState;}
        public int getmy_width() {
            return my_width;
        }
        public int getmy_height() {
            return my_height;
        }
        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
        public void setX(int x) {
            this.x = x;
        }
        public void setY(int y) {
            this.y = y;
        }

        public int getkuan() {
            return kuan;
        }
        public int getgao() {
            return gao;
        }
        public void setkuan(int kuan) {
            this.kuan = kuan;
        }
        public void setgao(int gao) {
            this.gao = gao;
        }

        public Bitmap getmyimg(){return my;}
        public void setmyimg(Bitmap myimg){this.my = myimg;}
        public int getw(){
            return my_width+getkuan();
        }
        public int geth(){
            return my_height+getgao();
        }
    }

    //负责背景照片处理
    private class bgImage implements GameImage{
        private Bitmap bg;
        private Bitmap newBitmap = null;
        private Paint p = new Paint();

        private bgImage(Bitmap bg){

            this.bg = bg;
            newBitmap = Bitmap.createBitmap(display_w,display_h, Bitmap.Config.ARGB_8888);
        }

        public Bitmap getBitmap() {

            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(bg,new Rect(0,0,bg.getWidth(),bg.getHeight()),new Rect(-huadong,0,display_w-huadong,display_h),p);
            canvas.drawBitmap(bg,new Rect(0,0,bg.getWidth(),bg.getHeight()),new Rect(display_w-huadong,0,2*display_w-huadong,display_h),p);
            huadong+=getbgspeed();
            if(huadong==display_w){
                huadong = 0;
            }
            return newBitmap;
        }

        public int getX() {
            return 0;
        }

        public int getY() {
            return 0;
        }

    }

    //静止功能图片处理
    public class jingzhiImage implements GameImage{
        private Bitmap jingzhi;
        private jingzhiImage(Bitmap jingzhi){
            this.jingzhi = jingzhi ;
        }
        public Bitmap getBitmap() {

            return jingzhi;
        }

        public int getX() {
            return display_w-jingzhi.getWidth();
        }

        public int getY() {
            return 10;
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {

    }
    //清屏处理
    public class qingpingImage implements GameImage{

      Bitmap qingping;
        private qingpingImage(Bitmap qingping){this.qingping = qingping;}
        public Bitmap getBitmap() {

            return qingping;
        }


        public int getX() {
            return display_w-qingping.getWidth()-jingzhi.getWidth()-20;
        }


        public int getY() {
            return 10;
        }
    }


    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        display_w = width;
        display_h = height;
        init();
        this.holder = holder;
        state = true;
        thread = new Thread(this);
        thread.start();
    }
    Thread thread = null;
    public void surfaceDestroyed(SurfaceHolder holder) {
        state = false;
    }


}
