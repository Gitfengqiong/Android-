package com.example.administrator.muitleconter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.TabActivity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import static android.widget.GridLayout.spec;

//import static com.example.administrator.muitleconter.MainActivity.Internets;


public class SetingActivtiy extends TabActivity {
    private static VData vdata ;
    private Vibrator vibrator ;
    private GridLayout id;
    private  GridLayout ido;
   // private  MyUdpIo Internets ;
    private Message msg;
    protected static final int Msid1 = 0x130;
    protected static final int Mrid1 = 0x140;
    protected static MyUdpIo Internets ;
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
         VData Datas = (VData)getApplication();
        Datas.setInClikeoff(false);
        Datas.setSetInOk(false);
        Datas.setWaiteIn(false);
        Datas.ReCode = "00";
        vdata =Datas;
        Createview(vdata.inleng,vdata.outleng);

       MyHandler handler = new MyHandler(this);
       Internets = new MyUdpIo(handler,vdata.Thisip,Mrid1,Msid1);
       new Thread(Internets).start();
        vibrator = (Vibrator)this.getSystemService(this.VIBRATOR_SERVICE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try { Looper.prepare();
                    synchronized (this){
                        wait(1500);
                        if (vdata.ReCode.equals("55")) {
//
  //                          Toast.makeText(getBaseContext(),"指令执行成功",Toast.LENGTH_LONG).show();

                        }else{

    //                        Toast.makeText(getBaseContext(),"指令执行错误",Toast.LENGTH_LONG).show();

                        }

                    }
                    Looper.loop();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

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
                switch (msg.what) {
                    case Mrid1:
                        // do something...
                    {
                        Log.d("set收到数据", msg.obj.toString());
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
            final   Button is = new Mybutton(getBaseContext());
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
            is.setBackgroundColor(Color.parseColor("#ff1111"));
            is.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          vibrator.vibrate(70);
                                          ((Mybutton) is).Clickstart(vdata );
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
            ((Mybutton) is).setStatu(1);
            id.addView(is);

        }

        for (int i = 0; i < outsum; i++) {
            final Button is1 = new Mybutton(getBaseContext());
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
            is1.setBackgroundColor(Color.parseColor("#ff1111"));
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
            ido.addView(is1);
        }
    }


}