package com.kaon.test_app;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.WorkSource;
import android.service.controls.ControlsProviderService;
import android.util.DisplayMetrics;
import android.util.JsonReader;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;


public class mainActivity extends AppCompatActivity {

    public static final String KEY_TAG  = "D/key";
    public static final int KEYCODE_DPAD_UP = 19 ;
    public static final int KEYCODE_DPAD_DOWN = 20 ;
    public static final int KEYCODE_DPAD_LEFT = 21;
    public static final int KEYCODE_DPAD_RIGHT = 22 ;
    public static final int KEYCODE_DPAD_OK = 23 ;
    public static final int KEYCODE_DPAD_forward = 186 ;
    public static final int KEYCODE_BACK = 4 ;

    public static final String APP_DEBUG = "KAON";
    public static final String IMG_TAG = "D/imgBitmap";




    DisplayManager displayManager;
    ImageView imgView;

    Integer[] imgAddr ={R.drawable.worldtime_1080p,R.drawable.worldtime_2160p,
                        R.drawable.island_1080p,R.drawable.island_2160p,
                        R.drawable.port_1080p,R.drawable.port_2160p,
                        R.drawable.road_1080p,R.drawable.road_2160p,
                        R.drawable.snow_1080p,R.drawable.snow_2160p};
    String[] nameTag = {"worldtime", "island", "port", "road","snow"};
    Integer curNameIndexor = 0 ;
    Integer curResolIndexor = 0 ;

    Boolean switcher ;
    //modified : photo , Pair : drawable address ( 1060p) , drawable address ( 2160p)
    HashMap<String, Pair<Integer,Integer>> output_data = new HashMap<String,Pair<Integer,Integer>>();
    private static Handler loophandler;
    final Integer MDPI = 160,HDPI = 240,XHDPI = 320,XXHDPI = 480,XXXHDPI = 640 ;
    BitmapFactory.Options opt;
    Bitmap bitmap;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgView = (ImageView)findViewById(R.id.imgPicture);
        Log.d(APP_DEBUG, "---------------------- onCreate -------------------- ");
//        txtView = (TextView)findViewById(R.id.tvConfig);
//        txtDPI = (TextView)findViewById(R.id.tvDPIChecker);
//        btnToggle = (Button)findViewById(R.id.btnToggle);
//        btnOff = (Button)findViewById(R.id.btnOff);
        //init
        curNameIndexor = 0;
        switcher= true ;
        setHashMap(output_data);

        //Display dps;
        // searchFile();

        ActivityOptions aoptions = ActivityOptions.makeBasic();
        int tmp =aoptions.getLaunchDisplayId();

        displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);

        Log.d("DPI","DPI DISPLAY ID : " + tmp + " , Display Flag : "+Display.FLAG_PRESENTATION + " \nLaunch Display ID : "+ aoptions.setLaunchDisplayId(Display.DEFAULT_DISPLAY));


        //Log.d("DPI","DPI DISPLAY ID : " + tmp + " , Display Flag : "+Display.FLAG_PRESENTATION + " \nLaunch Display ID : "+ aoptions.setLaunchDisplayId(-1));
//        loophandler = new Handler(){
//            @Override
//            public void handleMessage(@NonNull Message msg) {
//                int addrIndexor = 0 ;
//                if(msg.obj!=null){
//                   switcher = !switcher;
//               }
//
//                super.handleMessage(msg);
//                String resolution ="";
//
//
//                Resources res =getResources();
//                imgView.setImageResource(0);
//
//
//                Log.d("initiative", "Make object about BimtmapFactory");
//
//
//                if(switcher){
//                    addrIndexor = 1080;
//
//                    Log.d("Resolution", "---------change 108p img-------------");
//
//                }else{
//                    addrIndexor = 2160;
//                    Log.d("Resolution", "---------change 2160p img-------------");
//
//                }
//
//                bitmap = BitmapFactory.decodeResource(res,output_data.get(addrIndexor).first);
//                imgView.setImageBitmap(bitmap);
//
//                resolution = getResolution(opt,output_data.get(addrIndexor).first,getResources());
//
//
//                // resolution =Integer.toString(opt.outHeight);
//                String result_text = "\n Img Resolution : " +resolution ;
//
//                txtView.setText(result_text);
//
//                switcher = !switcher;
//            }
//        };



//
//        LoopStartThread loopStartThread = new LoopStartThread();
//        Thread thread = new Thread(loopStartThread);
//        thread.start();
//

/*
        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        btnToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Resources res = getResources();
                Pair<Integer,Integer> imgSize ;

                //BitmapFactory.Options options;
                int addr;
                if(switcher){
                    addr = output_data.get(1080).first;
                    opt = getBitmapSize(res,addr);
                    //bitmap = BitmapFactory.decodeResource(res,addr,opt);
                    imgSize = getResolution(addr,res);
                    switcher= !switcher;
                }else{
                    addr = output_data.get(2160).first;
                    opt = getBitmapSize(res,addr);
                    imgSize = getResolution(addr,res);
                    switcher= !switcher;
                }

                    Log.v("SIZE", "width : " + imgSize.first + "  height : "+imgSize.second);
                    txtView.setText("width : " + imgSize.first + "height : "+imgSize.second);
                    bitmap = BitmapFactory.decodeResource(res,addr,opt);
                    bitmap = Bitmap.createScaledBitmap(bitmap,imgSize.second,imgSize.first,true);

                imgView.setImageBitmap(bitmap);
                //imgView.setImageBitmap();
            }
        });
*/
        Log.d(APP_DEBUG, "---------------------- onCreate DONE -------------------- ");
    }



    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LifeCycle", "This Activity is resume.");
        int dpi = getDeviceDPI();
        Log.d("Device DPI", "Device DPI is "+ dpi);
        Pair<Integer,Integer> resol ;
        resol = getDeviceResolution();
        Resources res =getResources();
        //Load Device DPI and Select their DPI

        Integer addr_img = resolutionSelector(getDeviceResolution().first);

        Log.d(APP_DEBUG,"Switcher Status : " + switcher) ;
        Pair<Integer,Integer> imgSize = getResolution(addr_img,res);
        bitmap = BitmapFactory.decodeResource(res,addr_img,opt);
//      bitmap = Bitmap.createScaledBitmap(bitmap,imgSize.second,imgSize.first,true);
//
        imgView.setImageBitmap(bitmap);
//


        String result_text = "\n Img width : " +getResolution(opt,addr_img,getResources()).first +
                             "  img Height : " +getResolution(opt,addr_img,getResources()).second;

//        txtView.setText(result_text);

//        txtDPI.setText("Device DPI :" + Integer.toString(dpi) +"dp"+
//               "\n  Width : " + resol.first + " height : " + resol.second);

        //화면 해상도
//        txtDPI.setText(" - Device size info - "+
//                "\nWidth : " + resol.first + " height : " + resol.second);



    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){

            case MotionEvent.ACTION_UP:
                return true;
        }

        return super.onTouchEvent(event);
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d("LifeCycle", "This Activity is paused.");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("LifeCycle", "This Activity is stop.");
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        Log.d("Key","this Remote Key code is " + Integer.toString(keyCode));
        boolean useSwitch = false;
        switch(keyCode){

            case KEYCODE_DPAD_LEFT:
                Log.d(KEY_TAG,"LEFT KEY LEFT -- CODE  : " + keyCode );
                if(curNameIndexor>0){
                 --curNameIndexor;
                }
                useSwitch = true;

                imgView.setImageBitmap(setImageVeiwSet(getResources(),curNameIndexor));
                break;

            case KEYCODE_DPAD_RIGHT:
                Log.d(KEY_TAG,"LEFT KEY RIGHT -- CODE  : " + keyCode );
                if(curNameIndexor<4){
                    ++curNameIndexor;
                }
                useSwitch = true;

                imgView.setImageBitmap(setImageVeiwSet(getResources(),curNameIndexor));
                break;

            case KEYCODE_DPAD_UP:
                switcher=!switcher;
                useSwitch = true;
                imgView.setImageBitmap(setImageVeiwSet(getResources(),curNameIndexor));
                break;

            case KEYCODE_DPAD_DOWN:
                Log.d(KEY_TAG,"LEFT KEY DOWN -- CODE  : " + keyCode );
                switcher=!switcher;
                useSwitch = true;
                imgView.setImageBitmap(setImageVeiwSet(getResources(),curNameIndexor));
                break;

            case KEYCODE_DPAD_OK:
                Log.d(KEY_TAG,"LEFT KEY OK -- CODE  : " + keyCode );
                Pair<Integer,Integer> d_info = getDeviceResolution();
                Pair<Integer,Integer> i_info ;
                if(switcher){
                    i_info = getResolution(output_data.get(nameTag[curNameIndexor]).first,getResources());
                }else{
                    i_info = getResolution(output_data.get(nameTag[curNameIndexor]).second,getResources());
                }


                Toast.makeText(this.getApplicationContext(),"Device Info : " + d_info.first+
                        " * " +d_info.second+"\n"+
                        "Img info : " + i_info.first + " * " + i_info.second,Toast.LENGTH_SHORT ).show();
                useSwitch = true;
                //Toast.makeText(this.getApplicationContext(),"switcher : "+curNameIndexor ,Toast.LENGTH_SHORT).show();

                break;

            case  KEYCODE_DPAD_forward:



                Toast.makeText(this.getApplicationContext(),"KeyCode :" + keyCode+"\n"+
                       "display count : "+  displayManager.getDisplays().length ,Toast.LENGTH_SHORT).show();

        }





        if (useSwitch){
            return true;
        }else{
         return super.onKeyUp(keyCode, event);
        }

    }



    public static class LoopStartThread implements Runnable{
        //1st single-ton

        private static LoopStartThread instance;
        private static Boolean play = true;
        private LoopStartThread(){}


        public static LoopStartThread getInstance(){

            if (instance == null){
                synchronized (LoopStartThread.class){
                    if (instance==null){
                        instance = new LoopStartThread();
                    }
                }
            }

            return instance;
        }

        public void setToggle(){

            if(!play){
                play = !play;
                run();
            }
            play = !play;
        }

        //2nd single - ton
        /*
        private LoopStartThread(){

        }

        private static class holder{
            public static final LoopStartThread instance = new LoopStartThread();
        }

        public static LoopStartThread getInstance(){
            return holder.instance;
        }
        */

        @Override
        public void run() {
            while(play){

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                loophandler.sendEmptyMessage(0);
            }
        }


    }

    //bitmap option 설정
    private BitmapFactory.Options getBitmapSize(Resources res ,int imgAddr) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        options.inScaled=true;
        //options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res,imgAddr, options);
        return options;
    }

    //왜만듬 ?
    private Pair<Integer,Integer> getResolution (BitmapFactory.Options opt, int imgAddr,Resources res){
        Pair<Integer,Integer> img_resol;
        opt = getBitmapSize(res,imgAddr);

        return new Pair<Integer, Integer>(opt.outWidth,opt.outHeight);
    }

    //img resolution
    private Pair<Integer,Integer> getResolution (int imgAddr,Resources res){
        Pair<Integer,Integer> resolution ;
        opt = getBitmapSize(res,imgAddr);

    //opt sampling 사이즈에 따라 보상값 적용
        resolution= new Pair<Integer, Integer>(opt.outHeight*opt.inSampleSize,opt.outWidth*opt.inSampleSize);
        return resolution;
    }


    private Bitmap setImageVeiwSet (Resources res, int imgIdx){
        Log.d(IMG_TAG,"world curIDX : " + curNameIndexor + " local imgIDX : " + imgIdx);

        int addr ;

        if(switcher){       //2160
            addr = output_data.get(nameTag[curNameIndexor]).first;
        }else{              //1080
            addr = output_data.get(nameTag[curNameIndexor]).second;
        }

        return BitmapFactory.decodeResource(res,addr,getBitmapSize(res,addr));
    }

    private void setHashMap(HashMap<String,Pair<Integer,Integer>> output){

        for (int i = 0 ; i < nameTag.length ; i++){
            int j = (i) *2  +1 ;
            output.put(nameTag[i],Pair.create(imgAddr[j-1],imgAddr[j]));
            Log.d("HashMap","Key : "+ nameTag[i] + " Pair second: " + imgAddr[j]);
            Log.d("i confirm", "i : " + i+" | j : " + j);
        }
        Log.d("setHashmp", "setHashMap is complete  ");
    }


    private int getDeviceDPI(){
        Activity context = mainActivity.this;
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metrics);
        Log.d("DPI", "This Device DeviceDPI: " + metrics.densityDpi);
        getDPtoPX(metrics.densityDpi);

//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        int deviceWidth = metrics.widthPixels;
//
//        int deviceHeight = metrics.heightPixels;
//
//        Log.d("DPI", "This Device Device Density(new method) : " + metrics.density+
//                " Device Width : " + deviceWidth +
//                " Device Height : " + deviceHeight);
        return metrics.densityDpi;
    }

    //Return Pair<Integer,Integer> : width(resol),height
    private Pair<Integer, Integer> getDeviceResolution(){

        int width,height;
        Display display = null;


        Log.d("DISPLAY ","DISPLAY " +
                displayManager.getDisplays().toString());

        if(displayManager.getDisplays().length>1){
            for(Display dis :displayManager.getDisplays())
            {
                Log.d("DISPLAY ID" , Integer.toString(dis.getDisplayId()));
            }
        }else {
                display = displayManager.getDisplay(0);
        }

        Point size = new Point();
        display.getSize(size);

        width = size.x;
        height = size.y;


        Log.d("DPI","Display Resolution :" + width + "x" + height);

        Pair<Integer,Integer> resolution = new Pair<>(height,width);

        return resolution;
    }

    //죽일 메소드
    private void getDPtoPX(Integer dp){
        String[] output_px = new String[5];

        Double dp_f = Double.valueOf(Integer.toString(dp));


        output_px[0] =Double.toString( dp_f * (MDPI/160));
        output_px[1] =Double.toString( dp_f * (HDPI/160.0));
        output_px[2] =Double.toString( dp_f * (XHDPI/160));
        output_px[3] =Double.toString( dp_f * (XXHDPI/160));
        output_px[4] =Double.toString( dp_f * (XXXHDPI/160));

        Log.d("DPI",
                "DP TO PX INFORMAION----------------------  " + "\n"+
                        "current DP : " + dp +"dp \n" +

                "mdpi : " + output_px[0] +"px \n"+
                "hdpi : " + output_px[1] +"px \n"+
                "xhdpi : " + output_px[2] +"px \n"+
                "xxhdpi : " + output_px[3] +"px \n"+
                        "xxxhdpi : " + output_px[4] +"px \n");

    }

    private Integer resolutionSelector(Integer reso){

        Log.d("DPI","Calculating dp... In Current state,over XXHDPI only provide 2160p.");
        Integer img_address =0;
        Log.d("DPI","output_data.get(NameTag) : " +   output_data.get(nameTag[curNameIndexor]));
            if(reso >= 2160){
                img_address = output_data.get(nameTag[curNameIndexor]).second;
                switcher = false;
            }else{
                img_address = output_data.get(nameTag[curNameIndexor]).first;
                switcher = true;
            }
        Log.d("DPI","Selector done ");
        return img_address;
    }


//    private void searchFile(){
//
//        Log.d("Directory","----------\n");
//
//        Log.d("Directory", "Data Dir : "+Environment.getDataDirectory().getAbsolutePath()
//                +"\n , Root dir : "+ Environment.getRootDirectory()
//                +"\n , External dir : " + Environment.getExternalStorageState());
//
//        String state= Environment.getExternalStorageState();
//
//
//        if(Environment.MEDIA_MOUNTED.equals(state)){
//            Log.d("Directory","Accessible to External Area");
//
//            String target_dir =" /mnt/sdcard/testDir ";
//            String target_data = target_dir+"/test.json";
//            Log.d("Directory","Taget Dir : " + target_dir);
//            Log.d("Directory","Taget Dir by getDir : " + getFilesDir());
//
//            File file = new File(target_dir,target_dir);
//
//            //JsonReader reader = new JsonReader(new InputStreamReader(,"UTF-0"));
//
//            //JsonReader reader = new JsonReader(new InputStreamReader(, "UTF-8"));
//
//        }else{
//            Log.d("Directory","cannot Access to External Area");
//        }

}




