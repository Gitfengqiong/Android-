package com.example.administrator.muitleconter;
import android.app.Application;

public class VData extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
    private int Outchannge  = 0;
    private int Inchannge;
    private boolean waiteIn = false;
    private boolean SetInOk =false;
    private int InClikeNumber = 0 ;
    private boolean InClikeoff = false ;
    private int Owaiteid = 0;
    public boolean LoginOk =false;
    public String ReCode ="null" ;
    public String devaddrid ="01";
    public String vtype = "A0";
    public  String cleng;
    public int inleng,outleng;
    public String Thisip ;
    public boolean haveall =false ;
    public boolean haveallout =false;
    public int alloutchange ;
    public boolean SceneStart =false;
    public int Scenenum = 16 ;
    public int IsScene = 0 ;
    public  String code1,code2,code3,code4,code5,code6,code7,code8,code9,code10;
    public  void  setWaiteIn(boolean w){
        waiteIn = w ;
    }
    public boolean getWaiteIn(){
        return waiteIn ;
    }
    public void setInchannge(int c){
        Inchannge = c ;
    }
    public int getInchannge(){
        return Inchannge;
    }

    public void setOutchannge(int outchannge) {
        Outchannge = outchannge;
    }
    public int getOutchannge(){
        return Outchannge;
    }
    public  void setSetInOk(boolean inOk){
        SetInOk = inOk;
    }
    public  boolean getSetInOk(){
        return SetInOk;
    }
    public  void setInClikeNumber (int n){
        InClikeNumber = n;
    }
    public  int getInClikeNumber(){
        return InClikeNumber;
    }
    public  void setInClikeoff(boolean inClikeoff){
        InClikeoff =inClikeoff;
    }
    public  boolean getInClikeoff(){
        return InClikeoff;
    }
    public void setOwaiteid(int id){
        Owaiteid =id;
    }
    public int getOwaiteid(){
        return Owaiteid;
    }

            public final String  m_istart ="efef" ; //开始字头 0xef 0xfe
            public char m_cVersion; //版本号
            public char m_cType; //数据包类型 =0x01 查询 =0x02 设置 =0x10 应答
            public char[] my_ethernet_address; //MACunsigned char my_ip_address[4]; //IP
            public char[] multicast_ip_address=new char[4]; //MULTICAST
            public char[] host_ip_address=new char[4]; //HOSTIP
            public char[] gateway_ip_address=new char[4]; //GATEWAY
            public char[] mask_ip_address=new char[4]; //MASK
            public short m_LocalPort; //LOCAL PORT
            public short m_HostPort; //HOST PORT
            public char m_NetMode; //net working mode
            public char m_CommBaud; //buad
            public char m_ID; //ID
            public char[] DEVICE_name= new char[6]; //device name
            public char[] DECICE_model=new char[3]; //device model
            public char m_IPMode; //ip_mode
            public char[] password= new char[3]; //login password
            public char[] identify = new char[6]; //产品识别码
            public char[] dns_ip = new char[4]; //dns


}
