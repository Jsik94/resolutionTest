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
import android.service.controls.ControlsProviderService;
import android.util.DisplayMetrics;
import android.util.JsonReader;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
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

    DisplayManager displayManager;
    ImageView imgView;
    TextView txtView,txtDPI;
    Button btnToggle,btnOff;
    Integer[] imgAddr ={R.drawable.worldtime_1080p,R.drawable.worldtime_2160p,
                        R.drawable.island_1080p,R.drawable.island_2160p,
                        R.drawable.port_1080p,R.drawable.port_2160p,
                        R.drawable.road_1080p,R.drawable.road_2160p,
                        R.drawable.snow_1080p,R.drawable.snow_2160p};
    Boolean switcher ;

    //modified : photo , Pair : drawable address ( 1060p) , drawable address ( 2160p)

    HashMap<Integer, Pair<Integer,Integer>> output_data = new HashMap<Integer,Pair<Integer,Integer>>();
    private static Handler loophandler;

    final Integer MDPI = 160,HDPI = 240,XHDPI = 320,XXHDPI = 480,XXXHDPI = 640 ;

    BitmapFactory.Options opt;
    Bitmap bitmap;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgView = (ImageView)findViewById(R.id.imgPicture);
//        txtView = (TextView)findViewById(R.id.tvConfig);
//        txtDPI = (TextView)findViewById(R.id.tvDPIChecker);
//        btnToggle = (Button)findViewById(R.id.btnToggle);
//        btnOff = (Button)findViewById(R.id.btnOff);

        switcher= true ;

        btnToggle.setEnabled(true);
        setHashMap(output_data);
        Display dps;

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



    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("LifeCycle", "This Activity is start");
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
        Integer addr = resolutionSelector(getDeviceResolution().first,output_data);
        Pair<Integer,Integer> imgSize = getResolution(addr,res);
        bitmap = BitmapFactory.decodeResource(res,addr,opt);
        bitmap = Bitmap.createScaledBitmap(bitmap,imgSize.second,imgSize.first,true);
//
        imgView.setImageBitmap(bitmap);
//

        // resolution =Integer.toString(opt.outHeight);
//        String result_text = "\n Img Resolution : " +getResolution(opt,addr,getResources()); ;
//
//        txtView.setText(result_text);

//        txtDPI.setText("Device DPI :" + Integer.toString(dpi) +"dp"+
//               "\n  Width : " + resol.first + " height : " + resol.second);

        //화면 해상도
        txtDPI.setText(" - Device size info - "+
                "\nWidth : " + resol.first + " height : " + resol.second);



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

    private BitmapFactory.Options getBitmapSize(Resources res ,int imgAddr) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        options.inScaled=true;
        //options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res,imgAddr, options);
        return options;
    }

    private String getResolution (BitmapFactory.Options opt, int imgAddr,Resources res){
        String resolution ="";
        opt = getBitmapSize(res,imgAddr);

        resolution = Integer.toString(opt.outHeight);
        return resolution;
    }

    private Pair<Integer,Integer> getResolution (int imgAddr,Resources res){
        Pair<Integer,Integer> resolution ;
        opt = getBitmapSize(res,imgAddr);

        resolution= new Pair<Integer, Integer>(opt.outHeight*opt.inSampleSize,opt.outWidth*opt.inSampleSize);
        return resolution;
    }

    private void setHashMap(HashMap<Integer,Pair<Integer,Integer>> output){

        for (int i = 0 ; i < imgAddr.length ; i++){
            output.put(1,Pair.create(imgAddr[i++],imgAddr[i]));
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

        Pair<Integer,Integer> resolution = new Pair<>(width,height);

        return resolution;
    }


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


    private Integer resolutionSelector(Integer reso,HashMap<Integer,Pair<Integer,String>> output_px){

        Log.d("DPI","Calculating dp... In Current state,over XXHDPI only provide 2160p.");
        Integer resolution =0;

            if(reso >= 1000){
                resolution = 2160;
                switcher = true;
            }else{
                resolution = 1080;
                switcher =false;
            }

            Pair<Integer,String> result;
            result = output_px.get(resolution);

        return result.first;
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




