package com.example.administrator.muitleconter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;


import com.xm.NetSdk;
import com.xm.javaclass.H264_DVR_DEVICEINFO;
import com.xm.video.MySurfaceView;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static android.widget.GridLayout.spec;
import static com.example.administrator.muitleconter.MainActivity.EnTestView;
import static com.example.administrator.muitleconter.MainActivity.SceneRemark;
import static com.example.administrator.muitleconter.MainActivity.config;
import static com.example.administrator.muitleconter.MainActivity.config_in;
import static com.example.administrator.muitleconter.MainActivity.config_out;
import static com.example.administrator.muitleconter.MainActivity.vdate;
import static com.example.administrator.muitleconter.Startdecode.netSdk;


public class SetingActivtiy extends TabActivity {
    protected static VData vdata ;
    private Vibrator vibrator ;
    private GridLayout id;
    private  GridLayout ido;
    private GridLayout Scenel;
    private Message msg;
    protected static final int Msid1 = 0x130;
    protected static final int Mrid1 = 0x140;
    protected static MyUdpIo Internets ;
    protected static final int Mstatue = 0x230;
    protected static final int MReview = 0x260;
    protected static Handler Statue ;
    private LinearLayout buttonlayot;
    protected static String FileNames ;
    protected static String InFileNames;
    protected static String OutFileNames;
    private static  boolean Keyboardon = false ;
    private static Model checkStatu[]  =new Model[vdate.Scenenum];
    private static Handler review;
    protected static String ChanngRemark_in[] ;
    protected static String ChanngeRemark_out[] ;
    protected   int Outbid = 0 ;
    private MySurfaceView Video[] =new MySurfaceView[4];
    private SurfaceHolder holder[] = new SurfaceHolder[4];
    private   boolean isFull =false ;
    private MySurfaceView sdkView[] =new MySurfaceView[4];
    protected static final int ViewNum = 4  ;
    private GestureDetector gestureDetector ;
    private int Inchangeid ;
    private  Thread threadhalder ;
    protected boolean EnClose = false ;
    protected boolean bEnFull = true ;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seting);
        EnClose = false ;
        TabHost gettab = getTabHost();
        TabSpec sp = gettab.newTabSpec("tab1").setIndicator("视频布局").setContent(R.id.Tab1v);
        gettab.addTab(sp);
        TabSpec sp1 = gettab.newTabSpec("tab2").setIndicator("场景设置").setContent(R.id.Tab2v);
        gettab.addTab(sp1);
        TabSpec sp3 = gettab.newTabSpec("tab3").setIndicator("视频预监").setContent(R.id.Tab3v3);
        gettab.addTab(sp3);
        id = (GridLayout) findViewById(R.id.Tab1in);
        ido = (GridLayout) findViewById(R.id.Tab1out);
        Scenel = findViewById(R.id.Scenel);

        VData Dates = (VData)getApplication();
        Dates.setInClikeoff(false);
        Dates.setSetInOk(false);
        Dates.setWaiteIn(false);
        Dates.ReCode = "OK";
        vdata =Dates;
        ChanngRemark_in = new String[vdata.inleng];
        ChanngeRemark_out = new String[vdata.outleng];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Createview(vdata.inleng,vdata.outleng);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CreateScene(vdata.Scenenum);
        }

        for (int i = 0 ; i<vdata.Scenenum ; i++){
            SceneRemark[i] = "null";
        }
        for (int i = 0 ; i<vdata.inleng ; i++){
            ChanngRemark_in[i] = "null";
        }
        for (int i = 0 ; i<vdata.outleng ; i++){
            ChanngeRemark_out[i] = "null";
        }
        FileNames =  this.getFilesDir().getPath()+String.valueOf(vdata.my_ethernet_address)+".cfg";
        InFileNames =  this.getFilesDir().getPath()+String.valueOf(vdata.my_ethernet_address)+"In.cfg";
        OutFileNames =  this.getFilesDir().getPath()+String.valueOf(vdata.my_ethernet_address)+"Out.cfg";

        config = new File(FileNames);
        if(!config.exists())
        {
            try {
                //文件不存在，就创建一个新文件
                config.createNewFile();
                 //  System.out.println("文件已经创建了");
                Toast.makeText(this,"New Device",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config_in = new File(InFileNames);
        if(!config_in.exists())
        {
            try {
                //文件不存在，就创建一个新文件
                config_in.createNewFile();
                //  System.out.println("文件已经创建了");
                Toast.makeText(this,"New Device",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config_out = new File(OutFileNames);
        if(!config_out.exists())
        {
            try {
                //文件不存在，就创建一个新文件
                config_out.createNewFile();
                //  System.out.println("文件已经创建了");
                Toast.makeText(this,"New Device",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        gestureDetector = new GestureDetector(this, new MyOnGestureListener());
        review = new Handler();
        MyHandler handler = new MyHandler(this);
        Internets = new MyUdpIo(handler,vdata.Thisip,Mrid1,Msid1);
        new Thread(Internets).start();
        vibrator = (Vibrator)this.getSystemService(VIBRATOR_SERVICE);

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
               // vdata.ReCode="null";
                SendStatue();
            }
        });

        Spinner Savelist  = findViewById(R.id.SaveScene);
        Spinner Calllist = findViewById(R.id.CallScene);
        List<String> Scenelist  = new ArrayList<String>();
        for (int i=1 ;i<=vdata.Scenenum; i++){
            Scenelist.add("场景"+String.valueOf(i));
        }

        for (int i=0 ;i<vdata.Scenenum; i++){
            checkStatu[i]= new Model();
            checkStatu[i].setIscheck(false);
        }
        Scenelist.add("请选择：");
        SceneArrayAdapter<String> adapter;
        adapter = new SceneArrayAdapter<String>(this,R.layout.spinner_view, R.id.text1 , Scenelist );
        adapter.setDropDownViewResource(R.layout.spinner_drop);
        SceneArrayAdapter_check<String> adapter1;
        adapter1 = new SceneArrayAdapter_check<String>(this,R.layout.spinner_view, R.id.text1 , Scenelist, checkStatu);
        adapter1.setDropDownViewResource(R.layout.spinner_drop_check);
        Calllist.setAdapter(adapter1);
        Savelist.setAdapter(adapter);
        Savelist.setSelection(Scenelist.size()-1,true);
        Calllist.setSelection(Scenelist.size()-1,true);
        CheckBox cb = findViewById(R.id.ch);

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
                SendStatue();

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


                Log.d("this:",String.valueOf(position));
                for (int i = 0 ;i<16 ; i++){
                    Log.d("v:",String.valueOf(checkStatu[i].ischeck()));
                }
               /**** V1.4 Code**********
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
                SendStatue();

                *******/

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });


        // Init Scene remark
        new Thread(){
            @Override
            public void run() {
                super.run();
                synchronized (this) {
                    ParseCode.parseSceneRemark(FileNames);
                    for (int i = 0; i < vdata.Scenenum; i++) {
                        SceneButton button = findViewById(1000 + i);
                        if (!SceneRemark[i].equals("null") && !SceneRemark[i].equals("")) {
                            button.Remark = SceneRemark[i];
                        }

                    }
                    try {
                        wait(10);
                        ParseCode.parseChanngeRemark(InFileNames, ChanngRemark_in);
                        for (int i = 0; i < vdata.inleng; i++) {
                            Mybutton button = findViewById(600 + i);
                            if (!ChanngRemark_in[i].equals("null") && !ChanngRemark_in[i].equals("")) {
                                button.Remark = ChanngRemark_in[i];
                            }
                        }

                        wait(10);
                        ParseCode.parseChanngeRemark(OutFileNames, ChanngeRemark_out);
                        for (int i = 0; i < vdata.outleng; i++) {
                            Mybutton button = findViewById(800 + i);
                            if (!ChanngeRemark_out[i].equals("null") && !ChanngeRemark_out[i].equals("")) {
                                button.Remark = ChanngeRemark_out[i];
                            }

                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }





                }
            }
        }.start();

        new Thread( new Runnable(){
            @Override
            public void run() {

                synchronized (this) {
                    try {

                        wait(100);
                      //  Log.d("Send get changge","sends");
                        Refresh.GetChanngeStatue(vdata.outleng);
                        wait(100);
                        review.post(Refreshview);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();


        new Thread(String.valueOf( Statue = new Handler() {
            @Override
            public void handleMessage(Message msg)  {
                synchronized (this) {
                    if (msg.what == Mstatue) {
                        try {
                            wait(500);

                             if(vdata.ReCode.equals("null")){
                        Toast.makeText(getBaseContext(),"设备无响应",Toast.LENGTH_SHORT).show();
                            }else if(vdata.ReCode.equals("55")||vdata.ReCode.equals(vdata.vtype)) {
                                 vdata.ReCode ="null";
                                 Toast.makeText(getBaseContext(),"操作成功",Toast.LENGTH_SHORT).show();

                             }else if (vdata.ReCode.equals("OK")){
                                 Toast.makeText(getBaseContext(),"连接成功",Toast.LENGTH_SHORT).show();
                             }else {
                                Toast.makeText(getBaseContext(),"执行错误："+vdata.ReCode,Toast.LENGTH_LONG).show();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                    if (msg.what == MReview){
                        vdata.ReCode = "55";
                        post(Refreshview);
                    }
                }

            }
        })).start();

        if (EnTestView)
        initView(Video,holder,ViewNum);
         else
        {
            try {
                sdkinitView(ViewNum);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    Runnable Refreshview =new Runnable() {
        //   @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void run() {

            int Num = Integer.parseInt(vdata.code[3],16)-2;
          //  Log.d("code5",String.valueOf(vdata.code[5]));
            if (Integer.parseInt(vdata.code[5],16)>vdata.outleng) {
                Refresh.GetChanngeStatue(vdata.outleng);
                synchronized (this) {
                    try {
                        wait(300);
                        if (!(Integer.parseInt(vdata.code[5],16)<0)) {
                            for (int i = 0; i < Num; i++) {
                                synchronized (this) {
                                    try {
                                        wait(5);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    //int Outbid = 0 ;
                                    Outbid = Integer.parseInt(vdata.code[i + 5], 16);
                                    i++;
                                    int InNum = Integer.parseInt(vdata.code[i + 5], 16) + 1;
                                    if (Outbid > vdata.outleng) {
                                        Log.d("OUtid:", String.valueOf(Outbid) + "：" + String.valueOf(vdata.outleng) + String.valueOf(vdate.code[i + 5]));
                                        break;
                                    }
                                    Mybutton out;
                                    out = (Mybutton) findViewById(800 + Outbid);
                                    out.setFoucse(true);
                                    out.setOutOK(true);
                                    vdata.setOutchannge(out.getMyId());
                                    vdata.setWaiteIn(false);
                                    out.setText(InNum + "->" + vdata.getOutchannge());
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        out.setBackground(getResources().getDrawable(R.drawable.button_shap_on));
                                    }
                                    out.setTextColor(Color.parseColor("#22ee33"));

                                }
                            }

                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();

                    }
                }
            }

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        vdata.my_ethernet_address= "".toCharArray();
        vdata.LoginOk =false ;
        if (!EnTestView) {
            for (int i = 0; i < ViewNum; i++) {
                netSdk.CloseAlarmChan(i);
                netSdk.onDevLogout(i);
            }
            netSdk.Cleanup();
        }
        Outbid = 0 ;
        EnClose = true ;
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
                     //  Log.d("set收到数据", msg.obj.toString());
                     //   Toast.makeText(,msg.obj.toString(),Toast.LENGTH_LONG);
                        String data = msg.obj.toString();
                        vdata.LoginOk = true;
                        ParseCode.Parsecode(data);
                        vdata.ReCode = vdata.code[4];
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

    class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
               //         Log.i(getClass().getName(), "onSingleTapUp-----" + getActionName(e.getAction()));

                       return false;
                    }

                @Override
        public void onLongPress(MotionEvent e) {
              //           Log.i(getClass().getName(), "onLongPress-----" + getActionName(e.getAction()));

                    //TODO  输入通道预览

                    vibrator.vibrate(100);
                    bEnFull = false ;
                    final AlertDialog.Builder builder = new AlertDialog.Builder(SetingActivtiy.this);
               //     dlg.setTitle("预览：");
                    final View view = LayoutInflater.from(SetingActivtiy.this).inflate(R.layout.vwindow, null);
                    final LinearLayout l = (LinearLayout) view.findViewById(R.id.vsss);
                    SurfaceView s  =  findViewById(R.id.v1);
                    final GridLayout g = (GridLayout)findViewById(R.id.Tab3v);
                    g.removeView(s);
                    LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    s.setLayoutParams(lp);
                    l.addView(s);

                    builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SurfaceView v1 = view.findViewById(R.id.v1);
                            SurfaceView v2 = findViewById(R.id.v2);
                            SurfaceView v3 = findViewById(R.id.v3);
                            SurfaceView v4 = findViewById(R.id.v4);
                            l.removeAllViews();
                            g.removeView(v2);
                            g.removeView(v3);
                            g.removeView(v4);
                            g.removeAllViews();
                            for (int i = 0; i < 4; i++) {
                                GridLayout.Spec rowSpec = GridLayout.spec((int)i / 2, 1f);
                                GridLayout.Spec columnSpec = GridLayout.spec((int)i % 2, 1f);
                                GridLayout.LayoutParams lp = new GridLayout.LayoutParams(rowSpec, columnSpec);
                                lp.width = 0;
                                lp.height = 0;
                                switch (i){

                                    case 0:
                                        g.addView(v1, lp);
                                        continue;
                                    case  1:
                                        g.addView(v2,lp);
                                        continue;
                                    case 2:
                                        g.addView(v3,lp);
                                        continue;
                                    case 3:
                                        g.addView(v4,lp);
                                        continue;
                                }

                            }
                              isFull = false ;
                              bEnFull = true ;
                            dialog.dismiss();

                        }
                    });
                    final AlertDialog dlg = builder.create();
                    dlg.setCancelable(false);
                    dlg.setView(view,0,0,0,0);
                    dlg.show();
                    WindowManager.LayoutParams layoutParams = dlg.getWindow().getAttributes();
                    layoutParams.width = 576;
                    layoutParams.height = 480;
                    dlg.getWindow().setAttributes(layoutParams);

                    }

                 @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                  //   Log.i(getClass().getName(),
                  //                    "onScroll-----" + getActionName(e2.getAction()) + ",(" + e1.getX() + "," + e1.getY() + ") ,("
                  //                            + e2.getX() + "," + e2.getY() + ")");
                        return false;
                     }

                 @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                   //   Log.i(getClass().getName(),
                   //                     "onFling-----" + getActionName(e2.getAction()) + ",(" + e1.getX() + "," + e1.getY() + ") ,("
                    //                           + e2.getX() + "," + e2.getY() + ")");
                      return false;
                   }

               @Override
        public void onShowPress(MotionEvent e) {
                     //   Log.i(getClass().getName(), "onShowPress-----" + getActionName(e.getAction()));
                    }

               @Override
        public boolean onDown(MotionEvent e) {
                    // Log.i(getClass().getName(), "onDown-----" + getActionName(e.getAction()));
                        return false;
                   }

                 @Override
         public boolean onDoubleTap(MotionEvent e) {
                  //     Log.i(getClass().getName(), "onDoubleTap-----" + getActionName(e.getAction()));
                     vibrator.vibrate(100);
                     Mybutton b = findViewById(Inchangeid);
                     b.setOnLong(vdata);
                         return false;
                     }

                 @Override
                 public boolean onDoubleTapEvent(MotionEvent e) {
                   //     Log.i(getClass().getName(), "onDoubleTapEvent-----" + getActionName(e.getAction()));
                       return false;
                   }

                 @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
                    //    Log.i(getClass().getName(), "onSingleTapConfirmed-----" + getActionName(e.getAction()));
                        vibrator.vibrate(70);
                        Mybutton b = findViewById(Inchangeid);
                        b.Clickstart(vdata);
                        return false;
                    }
     }

    private String getActionName(int action) {
                 String name = "";
                 switch (action) {
                         case MotionEvent.ACTION_DOWN: {
                                 name = "ACTION_DOWN";
                                 break;
                             }
                         case MotionEvent.ACTION_MOVE: {
                                 name = "ACTION_MOVE";
                                 break;
                             }
                         case MotionEvent.ACTION_UP: {
                                 name = "ACTION_UP";
                                 break;
                             }
                         default:
                             break;
                     }
                 return name;
             }


    @SuppressLint("ClickableViewAccessibility")
    public void Createview(int insum, int outsum ){

        for (int i = 0; i < insum; i++) {
            final   Mybutton is = new Mybutton(getBaseContext());
            is.setText(i+1+"");
            @IdRes int ids =600+i ;
            is.setId(ids);
            ((Mybutton) is).SetButtonid(ids);
            ((Mybutton) is).setMyId(i+1);
            buttonlayot = new LinearLayout(getBaseContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            buttonlayot.setOrientation(LinearLayout.VERTICAL);
            layoutParams.setMargins(0, 0, 0, 0);

            GridLayout.LayoutParams param= null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                param = new GridLayout.LayoutParams(spec(
                        GridLayout.UNDEFINED,1,1f),
                        spec(GridLayout.UNDEFINED,1,1f));
            }
            param.setMargins(3,3,3,3);
            is.setLayoutParams(layoutParams);
            buttonlayot.setLayoutParams(param);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                buttonlayot.setBackground(getResources().getDrawable(R.drawable.button_shap2));
            }
            is.setHeight(100);
            is.setTextSize(20);
            /*
            is.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          vibrator.vibrate(70);
                                          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                              ((Mybutton) is).Clickstart(vdata);
                                          }
                                      }
                                  }
            );

            is.setOnLongClickListener(new View.OnLongClickListener() {
                                          @Override
                                          public boolean onLongClick(View v) {
                                              vibrator.vibrate(70);
                                              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                                  ((Mybutton) is).setOnLong(vdata );
                                              }
                                              return true;
                                          }
                                      }
            );

*/
            is.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Inchangeid =  v.getId();
                    gestureDetector.onTouchEvent(event);
                    return true ;
                }
            });


            buttonlayot.addView(is);
            final Remark_Edit ise = new Remark_Edit(getBaseContext());
            ise.Buttonid = ids ;
            ise.setHint("备注");
           // ise.setHeight(40);
            //ise.weight();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                ise.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
            ise.setTextColor(Color.parseColor("#33ee11"));
            buttonlayot.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {

                            Rect r = new Rect();
                            buttonlayot.getWindowVisibleDisplayFrame(r);
                            int screenHeight = buttonlayot.getRootView()
                                    .getHeight();
                            int heightDifference = screenHeight - (r.bottom);
                            if (heightDifference > 200) {
                                //软键盘显示
                                Keyboardon = true;
                                //    Log.d("Up","keys");
                            } else if (screenHeight > 20 && heightDifference < 200) {
                                //软键盘隐藏

                                Keyboardon = false;
                            }

                        }

                    });
            is.setTextColor(Color.parseColor("#ee2233"));
            ((Mybutton) is).setStatu(1);
            ise.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

// TODO Auto-generated method stub
                    if (hasFocus) {

                    }
                    if (!hasFocus) {
                        if (Keyboardon){
                            ise.ChanngeMark_in(600);
                        }else
                        if (!Keyboardon){
                            ise.ChanngeSaveMark_in(600);
                        }
                    }

                }

            });


            buttonlayot.addView(ise);
            id.addView(buttonlayot);

        }

        for (int i = 0; i < outsum; i++) {
            final Mybutton is1 = new Mybutton(getBaseContext());
            is1.setText(i+1+"");
            @IdRes int ids = 800+i;
            ((Mybutton) is1).SetButtonid(ids);
            is1.setId(ids);
            ((Mybutton) is1).setMyId(i+1);

            buttonlayot = new LinearLayout(getBaseContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            buttonlayot.setOrientation(LinearLayout.VERTICAL);
            layoutParams.setMargins(0, 0, 0, 0);

            GridLayout.LayoutParams param= null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                param = new GridLayout.LayoutParams(spec(
                        GridLayout.UNDEFINED,1,1f),
                        spec(GridLayout.UNDEFINED,1,1f));
            }
            param.setMargins(3,3,3,3);
            buttonlayot.setLayoutParams(param);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                buttonlayot.setBackground(getResources().getDrawable(R.drawable.button_shap2));
            }
            is1.setHeight(100);
            is1.setTextSize(20);
            is1.setLayoutParams(layoutParams);

            is1.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           vibrator.vibrate(70);
                                           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                               ((Mybutton) is1).Clickstart(vdata);
                                           }
                                           //SendCodes();
                                       }
                                   }
            );
            ((Mybutton) is1).setStatu(0);
            is1.setTextColor(Color.parseColor("#ee2233"));
            buttonlayot.addView(is1);
            final Remark_Edit ise = new Remark_Edit(getBaseContext());

            ise.Buttonid = ids ;
            ise.setHint("备注");
         //   ise.setHeight(40);
            //ise.weight();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                ise.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
            ise.setTextColor(Color.parseColor("#33ee11"));
            buttonlayot.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {

                            Rect r = new Rect();
                            buttonlayot.getWindowVisibleDisplayFrame(r);
                            int screenHeight = buttonlayot.getRootView()
                                    .getHeight();
                            int heightDifference = screenHeight - (r.bottom);
                            if (heightDifference > 200) {
                                //软键盘显示
                                Keyboardon = true;
                                //    Log.d("Up","keys");
                            } else if (screenHeight > 20 && heightDifference < 200) {
                                //软键盘隐藏

                                Keyboardon = false;
                            }

                        }

                    });
            ise.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

// TODO Auto-generated method stub
                    if (hasFocus) {

                    }
                    if (!hasFocus) {
                        if (Keyboardon){
                            ise.ChanngeMark_out(800);
                        }else
                        if (!Keyboardon){
                            ise.ChanngeSaveMark_out(800);
                        }


                    }

                }

            });


            buttonlayot.addView(ise);
            ido.addView(buttonlayot);
        }
    }
    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void CreateScene(int insum) {
        final EditText e = new EditText(getBaseContext());
        for (int i = 0; i < insum; i++) {
            final SceneButton is = new SceneButton(getBaseContext());
            is.setText(i + 1 + "");
            @android.support.annotation.IdRes int ids = 1000 + i;
            is.setId(ids);
            is.id = i+1;
            buttonlayot = new LinearLayout(getBaseContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            buttonlayot.setOrientation(LinearLayout.VERTICAL);
            layoutParams.setMargins(0, 0, 0, 0);

            GridLayout.LayoutParams param = new GridLayout.LayoutParams(spec(
                    GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                    spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f));
            param.setMargins(3, 3, 3, 3);
            is.setLayoutParams(layoutParams);
            buttonlayot.setLayoutParams(param);
            buttonlayot.setBackground(getResources().getDrawable(R.drawable.button_shap2));
            is.setHeight(120);
            is.setTextSize(30);

           // is.setBackgroundColor(Color.parseColor("#ff1111"));
        //    is.setBackground(getResources().getDrawable(R.drawable.button_shap2));
            is.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          vibrator.vibrate(70);
                                           is.Clickstart(vdata );
                                      }
                                  }
            );
            is.setTextColor(Color.parseColor("#ee2233"));
            buttonlayot.addView(is);
            final Remark_Edit ise = new Remark_Edit(getBaseContext());

            ise.Buttonid = ids ;
            ise.setHint("备注");
           // ise.setHeight(65);
            //ise.weight();
            ise.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            ise.setTextColor(Color.parseColor("#33ee11"));
            buttonlayot.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {

                            Rect r = new Rect();
                            buttonlayot.getWindowVisibleDisplayFrame(r);
                            int screenHeight = buttonlayot.getRootView()
                                    .getHeight();
                            int heightDifference = screenHeight - (r.bottom);
                            if (heightDifference > 200) {
                                //软键盘显示
                                Keyboardon = true;
                            //    Log.d("Up","keys");
// changeKeyboardHeight(heightDifference);
                            } else if (screenHeight > 20 && heightDifference < 200) {
                                //软键盘隐藏

                              //  if (!Keyboardon) {  Log.d("down","keys");

                              //  ise.setFocusableInTouchMode(true);
                              //      ise.setFocusable(true);
                                   // ise.clearFocus();

                                //}
                                Keyboardon = false;
                            }

                        }

                    });
            //监听软键盘是否显示或隐藏

            /*
            ise.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence text, int start, int before, int count) {
                    //text  输入框中改变后的字符串信息
                    //start 输入框中改变后的字符串的起始位置
                    //before 输入框中改变前的字符串的位置 默认为0
                    //count 输入框中改变后的一共输入字符串的数量


                }

                @Override
                public void beforeTextChanged(CharSequence text, int start, int count,int after) {
                    //text  输入框中改变前的字符串信息
                    //start 输入框中改变前的字符串的起始位置
                    //count 输入框中改变前后的字符串改变数量一般为0
                    //after 输入框中改变后的字符串与起始位置的偏移量

                }

                @Override
                public void afterTextChanged(Editable s) {
                   ise.SaveEidtMark();
                }


            });

*/
            ise.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

// TODO Auto-generated method stub
                    if (hasFocus) {

                    }
                    if (!hasFocus) {
                      if (Keyboardon){
                          ise.EidtMark();
                      }else
                        if (!Keyboardon){
                          ise.SaveEidtMark();
                      }


                    }

                }

            });


            buttonlayot.addView(ise);
            Scenel.addView(buttonlayot);

        }
    }

    public void SceneStart(View view) throws InterruptedException {
        vdata.SceneStart = true ;
        int Snum = vdata.Scenenum ;
        vibrator.vibrate(70);
        int d1=0 , d2=0;
        /*
         * V1.4 code*
         *

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
*/
        boolean IshaveScene = false ;

        for (int i =0;i<vdata.Scenenum;i++ ){
            IshaveScene= IshaveScene||checkStatu[i].ischeck();
            if (checkStatu[i].ischeck()){
                int scene = i+1;
                if (scene <9){
                    switch (scene){
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
                if (scene <17){
                    switch (scene){
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
        if (!IshaveScene){
            Toast.makeText(getBaseContext(),"请设置场景循环号后再启动循环",Toast.LENGTH_LONG).show();
            vdata.SceneStart =false ;
            return;
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
        Log.d("Scence:",codes.toString());
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
    public static void SendStatue(){
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
        SendStatue();
    }

    private void initView(final MySurfaceView v[] , final SurfaceHolder h[] , final int chn){

        v[0]=findViewById(R.id.v1);
        h[0]=v[0].getHolder();
        v[1]=findViewById(R.id.v2);
        h[1]=v[1].getHolder();
        v[2]=findViewById(R.id.v3);
        h[2]=v[2].getHolder();
        v[3]=findViewById(R.id.v4);
        h[3]=v[3].getHolder();
        h[0].setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        new Thread(){
            @Override
            public void run() {
                super.run();
                Startdecode s = new Startdecode(v,h,chn);
                s.Start_test();
            }
        }.start();

    }



    private void  sdkinitView(final int chn) throws InterruptedException {

      //  mWndsHolder.vv1.initAdd(new int[] { R.drawable.wnd_normal, R.drawable.wnd_selected });
      //  mWndsHolder.vv2.initAdd(new int[] { R.drawable.w, R.drawable.wnd_add_selected });
       // mWndsHolder.vv3.initAdd(new int[] { R.drawable.wnd_add_normal, R.drawable.wnd_add_selected });
      //  mWndsHolder.vv4.initAdd(new int[] { R.drawable.wnd_add_normal, R.drawable.wnd_add_selected });
        final Startdecode s = new Startdecode(chn);
        s.mWndsHolder = new Startdecode.WndsHolder();
        s.mWndsHolder.vv1 =findViewById(R.id.v1);
        s.mWndsHolder.vv2 =findViewById(R.id.v2);
        s.mWndsHolder.vv3 =findViewById(R.id.v3);
        s.mWndsHolder.vv4 =findViewById(R.id.v4);
    //    s.mWndsHolder.vv5 =findViewById(R.id.v4);
        s.mWndsHolder.vv1.init(this, 0);
        s.mWndsHolder.vv2.init(this, 1);
        s.mWndsHolder.vv3.init(this, 2);
        s.mWndsHolder.vv4.init(this, 3);
      //  s.mWndsHolder.vv5.init(this,3);
        /*
        final Startdecode s1 =new Startdecode(1);
        s1.mWndsHolder = new Startdecode.WndsHolder();
        s1.mWndsHolder.vv1 =findViewById(R.id.vsu);
        s1.mWndsHolder.vv1.init(this, 0);
     */
      threadhalder =  new Thread(){
           @Override
           public void run() {
               super.run();
               if (EnClose)
                   return;
               synchronized (this){
                   try {
                       wait(300);
                       s.Start_sdk();
                     //  wait(300);
                     //  s1.Start_sdk();
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }

               }

           }
       };
       threadhalder.start();

    }

    public void FullView(View view) {
        if (bEnFull) {
            SurfaceView v = (SurfaceView) view;
            GridLayout.LayoutParams l = (GridLayout.LayoutParams) v.getLayoutParams();
            if (!isFull) {
                l.height = GridLayout.LayoutParams.MATCH_PARENT;
                l.width = GridLayout.LayoutParams.MATCH_PARENT;
                v.setLayoutParams(l);
                isFull = true;
            } else {
                l.height = 0;
                l.width = 0;
                v.setLayoutParams(l);
                isFull = false;
            }

        }
    }
}
