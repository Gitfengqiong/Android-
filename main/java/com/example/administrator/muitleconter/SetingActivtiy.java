package com.example.administrator.muitleconter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.content.pm.PermissionInfoCompat;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.widget.GridLayout.spec;

//import static com.example.administrator.muitleconter.MainActivity.Internets;


public class SetingActivtiy extends TabActivity {
    private static VData vdata ;
    private Vibrator vibrator ;
    private GridLayout id;
    private  GridLayout ido;
    private GridLayout Scenel;
   // private  MyUdpIo Internets ;
    private Message msg;
    protected static final int Msid1 = 0x130;
    protected static final int Mrid1 = 0x140;
    protected static MyUdpIo Internets ;
    protected static final int Mstatue = 0x230;
    protected static Handler Statue ;
    private LinearLayout buttonlayot;

    @SuppressLint("HandlerLeak")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seting);
        TabHost gettab = getTabHost();
        TabSpec sp = gettab.newTabSpec("tab1").setIndicator("视频布局").setContent(R.id.Tab1v);
        gettab.addTab(sp);
        TabSpec sp1 = gettab.newTabSpec("tab2").setIndicator("场景设置").setContent(R.id.Tab2v);
        gettab.addTab(sp1);
        id = (GridLayout) findViewById(R.id.Tab1in);
        ido = (GridLayout) findViewById(R.id.Tab1out);
        Scenel = findViewById(R.id.Scenel);
        buttonlayot = findViewById(R.id.blayout);
         VData Datas = (VData)getApplication();
        Datas.setInClikeoff(false);
        Datas.setSetInOk(false);
        Datas.setWaiteIn(false);
        Datas.ReCode = "00";
        vdata =Datas;
        Createview(vdata.inleng,vdata.outleng);
        CreateScene(vdata.Scenenum);
       SeekBar seekBar = (SeekBar) findViewById(R.id.progress);
       final TextView textView = (TextView) findViewById(R.id.text1);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private int time ;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 当拖动条的滑块位置发生改变时触发该方法,在这里直接使用参数progress，即当前滑块代表的进度值
                time = progress+1 ;
                textView.setText("Timer:" + Integer.toString(time)+"S");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
               // Log.e("------------", "开始滑动！");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Log.e("------------", "停止滑动！"+time);
                StringBuffer codes ;
                codes =new StringBuffer();
                codes.append(getBaseContext().getResources().getString(R.string.Ncode_Begin));
                codes.append(vdata.devaddrid);
                codes.append(getBaseContext().getResources().getString(R.string.Ncode_Order_SceneLoop_Timer));
                codes.append("03");
                codes.append("00");
                if (time <16){
                    String hexnum = Integer.toString(time,16);
                    codes.append("0");
                    codes.append(hexnum);
                }else if ( time >15){
                    String hexnum = Integer.toString(time,16);
                    codes.append(hexnum);
                }
                codes.append("00");
                SendCodes(codes.toString());//Set Scene Timer
               // Log.d("Timer::",codes.toString());
                Toast.makeText(getBaseContext(),"循环间隔："+String.valueOf(time),Toast.LENGTH_SHORT).show();
                vdata.ReCode="null";
                SendStatue();
            }
        });

        Spinner Savelist  = findViewById(R.id.SaveScene);
        Spinner Calllist = findViewById(R.id.CallScene);
        List<String> Scenelist  = new ArrayList<String>();
        for (int i=1 ;i<=vdata.Scenenum; i++){
            Scenelist.add("场景"+String.valueOf(i));
        }
        Scenelist.add("请选择：");
        SceneArrayAdapter<String> adapter;
        adapter = new SceneArrayAdapter<String>(this,R.layout.spinner_view, R.id.txtvwSpinner , Scenelist);
        adapter.setDropDownViewResource(R.layout.spinner_drop);
        Savelist.setAdapter(adapter);
        Calllist.setAdapter(adapter);
        Savelist.setSelection(Scenelist.size()-1,true);
        Calllist.setSelection(Scenelist.size()-1,true);
        Savelist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                position+=1;
                StringBuffer codes ;
                String leng = "03";
                codes=new StringBuffer();
                codes.append(getBaseContext().getResources().getString(R.string.Ncode_Begin));
                codes.append(vdata.devaddrid);
                codes.append(getBaseContext().getResources().getString(R.string.Ncode_Order_SceneSave));
                codes.append(leng);
                codes.append("00");
                if (position <16){
                    String hexnum = Integer.toString(position,16);
                    codes.append("0");
                    codes.append(hexnum);
                }else if ( position >15){
                    String hexnum = Integer.toString(position,16);
                    codes.append(hexnum);
                }
                codes.append("00");
                Log.d("Save Scence:",codes.toString());
                SendCodes(codes.toString());//Set Scene

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        Calllist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                position+=1;
                StringBuffer codes ;
                String leng = "03";
                codes=new StringBuffer();
                codes.append(getBaseContext().getResources().getString(R.string.Ncode_Begin));
                codes.append(vdata.devaddrid);
                codes.append(getBaseContext().getResources().getString(R.string.Ncode_Order_SceneGet));
                codes.append(leng);
                codes.append("00");
                if (position <16){
                    String hexnum = Integer.toString(position,16);
                    codes.append("0");
                    codes.append(hexnum);
                }else if ( position >15){
                    String hexnum = Integer.toString(position,16);
                    codes.append(hexnum);
                }
                codes.append("00");
                Log.d("Call Scence:",codes.toString());
                SendCodes(codes.toString());//Call Scene

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        MyHandler handler = new MyHandler(this);
       Internets = new MyUdpIo(handler,vdata.Thisip,Mrid1,Msid1);
       new Thread(Internets).start();
        vibrator = (Vibrator)this.getSystemService(this.VIBRATOR_SERVICE);
        new Thread(String.valueOf( Statue = new Handler() {
            @Override
            public void handleMessage(Message msg)  {
                synchronized (this) {
                    if (msg.what == Mstatue) {
                        try {
                            wait(1000);
                            if (vdata.ReCode.equals("55")) {
//
                                Toast.makeText(getBaseContext(),"指令执行成功",Toast.LENGTH_SHORT).show();

                            } else if(vdata.ReCode.equals("null")){

                        Toast.makeText(getBaseContext(),"设备无响应",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getBaseContext(),"执行错误："+vdata.ReCode,Toast.LENGTH_LONG).show();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
        })).start();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.finish();
    }

    public static class MyHandler extends Handler {
        private WeakReference<TabActivity> reference;
        public MyHandler(TabActivity activity) {
            reference = new WeakReference<TabActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (reference.get() != null) {
                switch (msg.what ) {
                    case Mrid1:
                        // do something...
                    {
                        //Log.d("set收到数据", msg.obj.toString());
                     //   Toast.makeText(,msg.obj.toString(),Toast.LENGTH_LONG);
                        String data = msg.obj.toString();
                        vdata.LoginOk = true;
                        ParseCode.Parsecode(data);
                        vdata.ReCode = vdata.code4;
                      //  Log.d("Re",vdata.ReCode);
                        break;
                    }
                    default: {
                        // do something...
                        break;
                    }
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void Createview(int insum, int outsum ){

        for (int i = 0; i < insum; i++) {
            final   Mybutton is = new Mybutton(getBaseContext());
            is.setText(i+1+"");
            @android.support.annotation.IdRes int ids =600+i ;
            is.setId(ids);
            ((Mybutton) is).SetButtonid(ids);
            ((Mybutton) is).setMyId(i+1);
            GridLayout.LayoutParams param= new GridLayout.LayoutParams(spec(
                    GridLayout.UNDEFINED,GridLayout.FILL,1f),
                    spec(GridLayout.UNDEFINED,GridLayout.FILL,1f));
            param.setMargins(3,3,3,3);
            is.setLayoutParams(param);
            is.setHeight(200);
            is.setTextSize(30);
          //  is.setBackgroundColor(Color.parseColor("#ff1111"));
            is.setBackground(getResources().getDrawable(R.drawable.button_shap2));
            is.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          vibrator.vibrate(70);
                                          ((Mybutton) is).Clickstart(vdata);
                                      }
                                  }
            );
            is.setOnLongClickListener(new View.OnLongClickListener() {
                                          @Override
                                          public boolean onLongClick(View v) {
                                              vibrator.vibrate(70);
                                              ((Mybutton) is).setOnLong(vdata );
                                              return true;
                                          }
                                      }
            );
            is.setTextColor(Color.parseColor("#ee2233"));
            ((Mybutton) is).setStatu(1);
            id.addView(is);

        }

        for (int i = 0; i < outsum; i++) {
            final Mybutton is1 = new Mybutton(getBaseContext());
            is1.setText(i+1+"");
           // @android.support.annotation.IdRes int ids = View.generateViewId();
            @android.support.annotation.IdRes int ids = 800+i;
            ((Mybutton) is1).SetButtonid(ids);
            is1.setId(ids);
            ((Mybutton) is1).setMyId(i+1);
            GridLayout.LayoutParams param= new GridLayout.LayoutParams(spec(
                    GridLayout.UNDEFINED,GridLayout.FILL,1f),
                    spec(GridLayout.UNDEFINED,GridLayout.FILL,1f));
            param.setMargins(3,3,3,3);
            is1.setLayoutParams(param);
            is1.setTextSize(30);
            is1.setHeight(200);
            //is1.setBackgroundColor(Color.parseColor("#ff1111"));
            is1.setBackground(getResources().getDrawable(R.drawable.button_shap2));
           // is1.setTextAlignment();
            is1.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           vibrator.vibrate(70);
                                           ((Mybutton) is1).Clickstart(vdata);
                                           //SendCodes();
                                       }
                                   }
            );
            ((Mybutton) is1).setStatu(0);
            is1.setTextColor(Color.parseColor("#ee2233"));
            ido.addView(is1);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void CreateScene(int insum) {

        for (int i = 0; i < insum; i++) {
            final SceneButton is = new SceneButton(getBaseContext());
            is.setText(i + 1 + "");
            @android.support.annotation.IdRes int ids = 1000 + i;
            is.setId(ids);
            is.id = i+1;
            
            GridLayout.LayoutParams param = new GridLayout.LayoutParams(spec(
                    GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                    spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f));
            param.setMargins(3, 3, 3, 3);
            is.setLayoutParams(param);
            is.setHeight(200);
            is.setTextSize(30);
           // is.setBackgroundColor(Color.parseColor("#ff1111"));
            is.setBackground(getResources().getDrawable(R.drawable.button_shap2));
            is.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          vibrator.vibrate(70);
                                           is.Clickstart(vdata );
                                      }
                                  }
            );
            is.setTextColor(Color.parseColor("#ee2233"));
            Scenel.addView(is);

        }
    }

    public void SceneStart(View view) throws InterruptedException {
        vdata.SceneStart = true ;
        int Snum = vdata.Scenenum ;
        vibrator.vibrate(70);
        int d1=0 , d2=0;
        for (int i=0 ; i<Snum ; i++){
            SceneButton isscen = findViewById(1000+i);

            if (isscen.Onstatu ){
                if (isscen.id <9){
                    switch (isscen.id){
                        case 1 : {
                            d1 = d1 | 0x00000001;
                            continue;
                        }
                        case 2:{
                            d1 = d1 | 0x00000002;
                            continue;
                        }
                        case 3:{
                            d1 = d1 | 0x00000004;
                            continue;
                        }
                        case 4:{
                            d1 = d1 | 0x00000008;
                            continue;
                        }
                        case 5:{
                            d1 = d1 | 0x00000010;
                            continue;
                        }
                        case 6:{
                            d1 = d1 | 0x00000020;
                            continue;
                        }
                        case 7:{
                            d1 = d1 | 0x00000040;
                            continue;
                        }
                        case 8:{
                            d1 = d1 | 0x00000080;
                            continue;
                        }
                    }
                    }
                if (isscen.id <17){
                    switch (isscen.id){
                        case 9 : {
                            d2 = d2 | 0x00000001;
                            continue;
                        }
                        case 10:{
                            d2 = d2 | 0x00000002;
                            continue;
                        }
                        case 11:{
                            d2 = d2 | 0x00000004;
                            continue;
                        }
                        case 12:{
                            d2 = d2 | 0x00000008;
                            continue;
                        }
                        case 13:{
                            d2 = d2 | 0x00000010;
                            continue;
                        }
                        case 14:{
                            d2 = d2 | 0x00000020;
                            continue;
                        }
                        case 15:{
                            d2 = d2 | 0x00000040;
                            continue;
                        }
                        case 16:{
                            d2 = d2 | 0x00000080;
                            continue;
                        }
                    }
                }
            }

        }

        StringBuffer codes ;
        String leng = "04";
        codes=new StringBuffer();
        codes.append(this.getResources().getString(R.string.Ncode_Begin));
        codes.append(vdata.devaddrid);
        codes.append(this.getResources().getString(R.string.Ncode_Order_SceneLoop));
        codes.append(leng);
        codes.append("00");
        if (d2 <16){
            String hexnum = Integer.toString(d2,16);
            codes.append("0");
            codes.append(hexnum);
        }else if ( d2 >15){
            String hexnum = Integer.toString(d2,16);
            codes.append(hexnum);
        }
        if (d1 <16){
            if ((d1+d2) < 1) d1 =1 ;
            String hexnum1 = Integer.toString(d1,16);
            codes.append("0");
            codes.append(hexnum1);
        }else if (d1 >15) {
            String hexnum1 = Integer.toString(d1, 16);
            codes.append(hexnum1);
        }

        codes.append("00");
       // Log.d("Scence:",codes.toString());
        SendCodes(codes.toString());//Set Scene Lopper Number
        synchronized (this) {
            try {
                wait(500);
                codes.delete(0, codes.length());
                codes.append(getBaseContext().getResources().getString(R.string.Ncode_Begin));
                codes.append(vdata.devaddrid);
                codes.append(getBaseContext().getResources().getString(R.string.Ncode_Order_SceneLoop_Timer));
                codes.append("03");
                codes.append("00");
                codes.append("03");
                codes.append("00");
                SendCodes(codes.toString());//Set Scene UDF Timer ***3S****
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        synchronized (this){
            wait(500);
            codes.delete(0,codes.length());
            codes.append(this.getResources().getString(R.string.Ncode_Begin));
            codes.append(vdata.devaddrid);
            codes.append(this.getResources().getString(R.string.Ncode_Order_SceneLoop_on));
            codes.append("03");
            codes.append("000000");
            SendCodes(codes.toString());//Start Scene Lopper
        }
        vdata.SceneStart =true ;
        Toast.makeText(getBaseContext(),"场景循环启动...",Toast.LENGTH_SHORT).show();
        vdata.ReCode="null";
        SendStatue();
    }
    public static   void SendCodes(String codes){

     //   Log.d("getip::",Internets.getIp());
        try {
            Message msg = new Message();
            msg.what = Msid1;
            msg.obj = codes;
            assert (Internets.rehandler != null);

            Internets.rehandler.sendMessage(msg);
        }catch (Exception e){e.printStackTrace();}

    }
    private static void SendStatue(){
        Message msg = new Message();
        msg.what = Mstatue;
        Statue.sendMessage(msg);
    }
    public  void  SceneStop(View view){
        StringBuffer codes ;
        codes =new StringBuffer();
        codes.append(this.getResources().getString(R.string.Ncode_Begin));
        codes.append(vdata.devaddrid);
        codes.append(this.getResources().getString(R.string.Ncode_Order_SceneLoop_on));
        codes.append("03");
        codes.append("00FF00");
        SendCodes(codes.toString());//Stop Scene Lopper
        vdata.SceneStart=false;
        Toast.makeText(getBaseContext(),"场景循环关闭...",Toast.LENGTH_SHORT).show();
        vdata.ReCode="null";
        SendStatue();
    }


}
