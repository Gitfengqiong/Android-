package com.example.administrator.muitleconter;

        import android.app.Activity;
        import android.app.ProgressDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.Handler;
        import android.os.Looper;
        import android.os.Message;
        import android.app.AlertDialog;
        import android.os.Bundle;
        import android.text.method.DigitsKeyListener;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.SimpleAdapter;
        import android.widget.Spinner;
        import android.widget.Toast;

        import java.io.File;
        import java.io.IOException;
        import java.lang.ref.WeakReference;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;


public class MainActivity extends Activity {
    private EditText ipin ;
    protected static MyUdpIo Internet;
    protected  static VData vdate;
    private ProgressDialog podia ,podia1;
    private static Handler runhandler;
    protected static final int Msid = 0x110;
    protected static final int Mrid = 0x120;
    protected static final int MMsid = 0x150;
    protected static final int MMrid=  0x160;
    protected static   IPlist iPlist[] =new IPlist[30] ;
    protected static  int ipIndex = 0;
    private static SimpleAdapter simp;
    private  Multicast multicast;
    private static ListView listView;
    private static List<Map<String,Object>> listitems;
    private static String IP;
    protected static File config ;
    protected static String SceneRemark[] ;
    private static boolean  NewD = false;
    //private static String debugs;
   // private static EditText e;
  //  protected static MyUdpIo  Internets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.longin);
        //Cheek InterNet
       if(!CheekInternet.isNetworkAvalible(this))
           CheekInternet.checkNetwork(this);
        ipin = findViewById(R.id.inip);
        listView = findViewById(R.id.list_view);
        ipin.setKeyListener(DigitsKeyListener.getInstance("1234567890."));
        listitems = new ArrayList<Map<String, Object>>();
        simp = new SimpleAdapter(this,listitems,R.layout.listitem,new String[]{"itemmage","itemip"},new int[]{R.id.itemmage,R.id.itemip});
        MyHandler handler = new MyHandler(this);
        Internet = new MyUdpIo(handler,"0.0.0.1",Mrid,Msid);
        runhandler = new Handler();
        final VData vdata = (VData) getApplication();
        vdate = vdata;
        SceneRemark = new String[vdate.Scenenum];
         //e = findViewById(R.id.beugs);
       multicast = new Multicast(this.getResources().getString(R.string.Multi_ip),Integer.parseInt((getResources().getString(R.string.Multi_port))
       ),handler,MMrid,MMsid,this.getResources().getString(R.string.Ncode_FindDev));
       for (int i=0 ;i<30 ;i++) {
           iPlist[i] = new IPlist();
           //创建30个IP参数单
       }

        //ipIndex = 0 ;
        //启动网络线程
        new Thread(Internet).start();
        new Thread(multicast).start();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
                                    long arg3) {
               // vdata.LoginOk =false ;
                //获得选中项的HashMap对象
                HashMap<String,String> map=(HashMap<String,String>)listView.getItemAtPosition(arg2);
                IP=map.get("itemip");
               // String content=map.get("itemContent");
                Toast.makeText(getApplicationContext(),
                        "链接："+IP+"中...",Toast.LENGTH_SHORT).show();
            //    podia = ProgressDialog.show(getBaseContext(), "连接设备", "正在连接中...请稍等", false, true);
                Internet.setIp(IP);
                vdata.LoginOk =false ;
                Message callMac  = new Message() ;
                callMac.what = Msid ;
                callMac.obj = "BA019604AA000000";
                Internet.rehandler.sendMessage(callMac);
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        synchronized (this){
                            try {
                                wait(300);
                                Looper.prepare();
                                if(!vdate.LoginOk){
                                    Toast.makeText(getBaseContext(),"旧版本设备",Toast.LENGTH_LONG).show();
                                    vdata.my_ethernet_address = iPlist[arg2].my_ethernet_address.toCharArray();
                                }else {
                                    Toast.makeText(getBaseContext(),"新版本设备",Toast.LENGTH_LONG).show();
                                    vdate.my_ethernet_address = (vdate.code5+vdate.code6+vdate.code7+vdate.code8+vdate.code9+vdate.code10).toCharArray();
                                }
                                Looper.loop();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();

              //  podia1 = ProgressDialog.show(getBaseContext(), "连接设备", "正在连接中...请稍等", false, true);
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        synchronized (this){
                            try {
                                wait(700);
                                vdata.LoginOk=false;
                                Internet.setMreid(Mrid);
                                Internet.setMseid(Msid);
                                Message msg = new Message();
                                msg.what = Msid;
                                String m = "BA01140200D1";//发送一条指令等待应答
                                //  msg.obj =String.valueOf(m);
                                msg.obj = m;
                                Internet.rehandler.sendMessage(msg);
                                wait(200);
                                runhandler.post(beug);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();


            }

        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int arg2, long arg3) {
                // TODO Auto-generated method stub
                HashMap<String, String> map = (HashMap<String, String>) listView.getItemAtPosition(arg2);
                IP = map.get("itemip");
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(R.mipmap.set);
                builder.setTitle("通信设置");
                //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.setippram, null);
                //    设置我们自己定义的布局文件作为弹出框的Content
                builder.setView(view);
               // builder.show();

                final EditText IP1 = (EditText) view.findViewById(R.id.ip_1);
                final EditText IP2 = (EditText) view.findViewById(R.id.ip_2);
                final EditText IP3 = (EditText) view.findViewById(R.id.ip_3);
                final EditText IP4 = (EditText) view.findViewById(R.id.ip_4);
                final EditText Port = (EditText) view.findViewById(R.id.portset);
                final EditText Gw1 = (EditText) view.findViewById(R.id.gw_1);
                final EditText Gw2 = (EditText) view.findViewById(R.id.gw_2);
                final EditText Gw3 = (EditText) view.findViewById(R.id.gw_3);
                final EditText Gw4 = (EditText) view.findViewById(R.id.gw_4);
                final EditText mac = (EditText) view.findViewById(R.id.macset);
                final EditText mask = (EditText) view.findViewById(R.id.mask_1);
                final EditText mask2 = (EditText) view.findViewById(R.id.mask_2);
                final EditText mask3 = (EditText) view.findViewById(R.id.mask_3);
                final EditText mask4 = (EditText) view.findViewById(R.id.mask_4);
                final Spinner bauds  = (Spinner) view.findViewById(R.id.baud);
                final EditText Id = (EditText) view.findViewById(R.id.ids);

                for (int i =0 ; i<8 ;){
                    String ips = iPlist[arg2].my_ip_address.substring(i,i+=2);
                    if (i == 2) {
                        IP1.setText(String.valueOf(Integer.parseInt(ips, 16)));

                    }
                    if (i == 4) {
                        IP2.setText(String.valueOf(Integer.parseInt(ips, 16)));

                    }
                    if (i == 6) {
                        IP3.setText(String.valueOf(Integer.parseInt(ips, 16)));

                    }
                    if (i == 8) {
                        IP4.setText(String.valueOf(Integer.parseInt(ips, 16)));

                    }
                }

                for (int i =0 ; i<8 ;){
                    String gws = iPlist[arg2].gateway_ip_address.substring(i,i+=2);
                    if (i == 2)
                        Gw1.setText(String.valueOf(Integer.parseInt(gws,16)));
                    if (i == 4)
                        Gw2.setText(String.valueOf(Integer.parseInt(gws,16)));
                    if (i == 6)
                        Gw3.setText(String.valueOf(Integer.parseInt(gws,16)));
                    if (i == 8)
                        Gw4.setText(String.valueOf(Integer.parseInt(gws,16)));

                }
                for (int i =0 ; i<8 ;){
                    String ms = iPlist[arg2].mask_ip_address.substring(i,i+=2);
                    if (i == 2)
                        mask.setText(String.valueOf(Integer.parseInt(ms,16)));
                    if (i == 4)
                        mask2.setText(String.valueOf(Integer.parseInt(ms,16)));
                    if (i == 6)
                        mask3.setText(String.valueOf(Integer.parseInt(ms,16)));
                    if (i == 8)
                        mask4.setText(String.valueOf(Integer.parseInt(ms,16)));

                }
                Id.setText(String.valueOf(Integer.parseInt(iPlist[arg2].m_ID,16)));
                Port.setText(String.valueOf(Integer.parseInt(iPlist[arg2].m_LocalPort,16)));
                mac.setText(iPlist[arg2].my_ethernet_address);
                bauds.setSelection(Integer.parseInt(iPlist[arg2].m_CommBaud,16),true);

                final String[] bs = {iPlist[arg2].m_CommBaud};
                bauds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {

                        bs[0] = "0"+ Integer.toHexString(position);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub

                    }
                });


                builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String ip1 = ParseCode.parseIpSlice(IP1.getText().toString());
                        String ip2 = ParseCode.parseIpSlice(IP2.getText().toString());
                        String ip3 = ParseCode.parseIpSlice(IP3.getText().toString());
                        String ip4 = ParseCode.parseIpSlice(IP4.getText().toString());
                        String gw1 = ParseCode.parseIpSlice(Gw1.getText().toString());
                        String gw2 = ParseCode.parseIpSlice(Gw2.getText().toString());
                        String gw3 = ParseCode.parseIpSlice(Gw3.getText().toString());
                        String gw4 = ParseCode.parseIpSlice(Gw4.getText().toString());
                        String mk1 = ParseCode.parseIpSlice(mask.getText().toString());
                        String mk2 = ParseCode.parseIpSlice(mask2.getText().toString());
                        String mk3 = ParseCode.parseIpSlice(mask3.getText().toString());
                        String mk4 = ParseCode.parseIpSlice(mask4.getText().toString());
                        String id = ParseCode.parseIpSlice(Id.getText().toString());
                        if (ip1.equals("erro")||ip2.equals("erro")||ip3.equals("erro")||ip4.equals("erro"))
                        {
                            Toast.makeText(MainActivity.this, "IP地址错误", Toast.LENGTH_SHORT).show();

                        }else if(gw1.equals("erro")||gw2.equals("erro")||gw3.equals("erro")||gw4.equals("erro")){
                            Toast.makeText(MainActivity.this, "网关地址错误", Toast.LENGTH_SHORT).show();
                        }else if (mk1.equals("erro")||mk2.equals("erro")||mk3.equals("erro")||mk4.equals("erro")){
                            Toast.makeText(MainActivity.this, "掩码地址错误", Toast.LENGTH_SHORT).show();
                        }
                        else if(id.equals("erro")){
                            Toast.makeText(MainActivity.this, "ID错误", Toast.LENGTH_SHORT).show();
                        }else {
                           // Toast.makeText(MainActivity.this, "IP: " + a + ", Port: " + b, Toast.LENGTH_SHORT).show();
                            Message msg =new  Message();
                            msg.what = MMsid;
                            msg.obj = "EFFE0102"+mac.getText().toString()+ip1+ip2+ip3+ip4+iPlist[arg2].multicast_ip_address+
                                    iPlist[arg2].host_ip_address +gw1+gw2+gw3+gw4+mk1+mk2+mk3+mk4+ParseCode.ByteSwap(Integer.toString(Integer.parseInt(Port.getText().toString()),16)) +
                                    ParseCode.ByteSwap(iPlist[arg2].m_HostPort) +iPlist[arg2].m_NetMode+ bs[0]+id+iPlist[arg2].DEVICE_name+
                                    iPlist[arg2].DECICE_model+ iPlist[arg2].m_IPMode+ iPlist[arg2].password+ iPlist[arg2].identify
                                    + iPlist[arg2].dns_ip;

                            ipIndex = 0 ;
                            listitems.clear();
                            listitems.add(null);
                            listView.setAdapter(simp);
                            multicast.rehandler.sendMessage(msg);
                            Toast.makeText(MainActivity.this, "请等待3秒重新搜索设备", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
                builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
                builder.show();
                return true;
            }

        });

    }
    Runnable runui =new Runnable() {
            @Override
            public void run() {
                ipin.setText("");
                vdate.Thisip =Internet.getIp();
                vdate.devaddrid = vdate.code1;
                vdate.vtype = vdate.code5;
                vdate.inleng = Integer.parseInt(vdate.code7,16);
                vdate.outleng =  Integer.parseInt(vdate.code8,16);
                Intent intent = new Intent(MainActivity.this, SetingActivtiy.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
//                podia.dismiss();

            }
    };
   Runnable beug =new Runnable() {
        @Override
        public void run() {

            if (vdate.LoginOk) {
                vdate.Thisip =Internet.getIp();
                vdate.devaddrid = vdate.code1;
                vdate.vtype = vdate.code5;
                vdate.inleng = Integer.parseInt(vdate.code7,16);
                vdate.outleng =  Integer.parseInt(vdate.code8,16);
                Intent intent = new Intent(MainActivity.this, SetingActivtiy.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);


            } else if (!vdate.LoginOk) {
             //   podia1.dismiss();
                Toast.makeText(getBaseContext(),"连接超时",Toast.LENGTH_SHORT).show();
            }

        }
    };
    public void Links(View view) throws InterruptedException {
        vdate.LoginOk =false;
        vdate.my_ethernet_address ="".toCharArray();

        if(ipin.getText().toString().isEmpty()){
            Toast toast = Toast.makeText(this,"请输入地址",Toast.LENGTH_SHORT);
            toast.show();
            /***********
             * Debug to SetingActivtiy Codes*
             */
        //    ipIndex = 0 ;
         //   String data ="effe01100014970f1ee0c0a801c8e0000029c0a8011ac0a80101ffffff00e40fe50f02080150472d4345004f53440031323300570000202000000000" +
          //          "effe01100014970f1ef0c0b801c8e0000029c0a8011ac0a80101ffffff00e40fe50f02080150472d4345004f53440031323300570000202000000000";
         //   int num =ParseCode.ParseMultiCodes(data);
          //  Log.d("find ip",iPlist[ipIndex-1].m_HostPort);
          //  CreateListView();
            String s= "BA0103A001130F0F7D";
            ParseCode.Parsecode(s);
            vdate.LoginOk =true;
           runhandler.post(runui);

        }else if(ipin.getText().toString().length() <7){
            Toast toast = Toast.makeText(this,"地址错误",Toast.LENGTH_SHORT);
            toast.show();
        }else {
                try {
                        Internet.setIp(ipin.getText().toString());
                        Internet.setMreid(Mrid);
                        Internet.setMseid(Msid);
                        Message callMac  = new Message() ;
                        callMac.what = Msid ;
                        callMac.obj = "BA019604AA000000";
                        Internet.rehandler.sendMessage(callMac);
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                synchronized (this){
                                    try {
                                        wait(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    Looper.prepare();
                                    if(!vdate.LoginOk){
                                        Toast.makeText(getBaseContext(),"旧设备，请在搜索中进入",Toast.LENGTH_LONG).show();
                                        NewD = false ;

                                    }else {
                                       // Toast.makeText(getBaseContext(),"",Toast.LENGTH_LONG).show();
                                        vdate.my_ethernet_address = (vdate.code5+vdate.code6+vdate.code7+vdate.code8+vdate.code9+vdate.code10).toCharArray();
                                        NewD = true ;
                                        Log.d("ID",vdate.my_ethernet_address.toString());
                                    }
                                    Looper.loop();
                                }
                            }
                        }.start();


                    podia = ProgressDialog.show(this, "连接设备", "正在连接中...请稍等", false, true);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                synchronized (this){
                                    wait(500);
                                    Message msg = new Message();
                                    msg.what = Msid;
                                    String m = getResources().getString(R.string.Ncode_Order_GetSYS);//发送一条指令等待应答
                                    //  msg.obj =String.valueOf(m);
                                    msg.obj = m;
                                    Internet.rehandler.sendMessage(msg);
                                    vdate.LoginOk =false ;
                                }

                                synchronized (this){
                                    wait(1500);
                                    if (vdate.LoginOk&&NewD) {
                                      //  vdate.my_ethernet_address = (vdate.code5+vdate.code6+vdate.code7+vdate.code8+vdate.code9+vdate.code10).toCharArray();
                                        runhandler.post(runui);
                                        podia.dismiss();
                                    } else if (!vdate.LoginOk) {
                                        vdate.my_ethernet_address = (vdate.code5+vdate.code6+vdate.code7+vdate.code8+vdate.code9+vdate.code10).toCharArray();
                                        /*
                                        String my = "ff";
                                        int num = Integer.parseInt(my,16);
                                        String nums = Integer.toString(num,16);
                                        Log.d("10radix:",String.valueOf(num));
                                        Log.d("16radix:",String.valueOf(nums));
                                        */
                                        podia.dismiss();//runhandler.post(runui);
                                        Looper.prepare();
                                        Toast.makeText(getBaseContext(),"链接超时",Toast.LENGTH_LONG).show();
                                        Looper.loop();
                                    }
                                }

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
    }

    public void search(View view){
        ipIndex= 0 ;
        vdate.LoginOk =false ;
        listitems.clear();
        listitems.add(null);
        listView.setAdapter(simp);
        Message msg = new Message();
        msg.what = MMsid;
        String m = this.getString(R.string.Ncode_FindDev);//发送一条指令等待应答
        //  msg.obj =String.valueOf(m);
        msg.obj = m;
        multicast.rehandler.sendMessage(msg);

    }

    public static void CreateListView(){

        listitems.clear();
        String ips[] = new String[ipIndex];
        for (int i = 0 ;i<ipIndex ; i++){
            ips[i]="";
            for (int is =0 ;is<8 ;) {
                String sub = String.valueOf(Integer.parseInt(iPlist[i].my_ip_address.substring(is, is += 2), 16));
               if (is<=6)
                sub += ".";
               ips[i]+=sub;
            }
            //Log.d("ipS:",ips[i]);
        }
        for ( int i = 0 ; i <ipIndex ; i++){
            Map<String,Object> listitem = new HashMap<String, Object>();
            listitem.put("itemmage",R.mipmap.ipdev);
            listitem.put("itemip",ips[i]);
            listitems.add(listitem);
        }


        if (ipIndex >0 )
        listView.setAdapter(simp);


    }

    public static class MyHandler extends Handler {
        private WeakReference<Activity> reference;
        public MyHandler(MainActivity activity) {
            reference = new WeakReference<Activity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            String Vdebugs  = "";

            if (reference.get() != null) {
                switch (msg.what) {
                    case Mrid:
                        // do something...
                    {
                        Log.d("Main收到数据", msg.obj.toString());
                        String data = msg.obj.toString();

                       ParseCode.Parsecode(data);

                       vdate.LoginOk = true;
                        break;
                    }

                    case MMrid:
                    {
                       // Log.d("收到多播返回","");
                       String  debugs = msg.obj.toString();
                       ParseCode.ParseMultiCodes(debugs);
                        CreateListView();
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


}

