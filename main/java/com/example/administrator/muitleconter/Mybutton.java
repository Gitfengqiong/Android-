package com.example.administrator.muitleconter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.Toast;

import static com.example.administrator.muitleconter.SetingActivtiy.Internets;
import static com.example.administrator.muitleconter.SetingActivtiy.Mrid1;
import static com.example.administrator.muitleconter.SetingActivtiy.Msid1;




public class Mybutton extends AppCompatButton {
    private int haveSetchannge = 0 ;
    private  boolean Foucse =false;
    private  boolean OutOK =false ;
    private int MyId;
    private int Thisid;
    private boolean thinkon =false;
    public Mybutton(Context context) {
        super(context);

    }
    private int statu ;//状态1 in 0 out



    public int  getStatu(){
        return statu;
    }
    public void setHaveSetchannge(int channge){
        haveSetchannge = channge;
    }
    public void setFoucse (boolean f){
        Foucse =f ;
    }
    public  void  setOutOK(boolean o){
        OutOK = o;
    }
    public void setStatu(int i){
        statu =i;
    }
    public void setMyId(int i ){
        MyId=i;
    }
    public int getMyId(){
        return MyId;
    }
    public void SetButtonid(int id){Thisid = id ;}
    public int GetButtonid(){return Thisid;}
    @SuppressLint("SetTextI18n")
    public int Clickstart(VData vdata ){
        //Log.d("this",this.statu+"Number"+this.getText().toString());


        if (statu == 0 && !Foucse && vdata.getSetInOk() &&!OutOK){
            //输出设置完毕状态
            Foucse = true ;
            OutOK = true ;
            vdata.setOutchannge(this.getMyId());
            vdata.setWaiteIn(false);
            this.setText(vdata.getInchannge()+"->"+vdata.getOutchannge());
            this.setBackgroundColor(Color.parseColor("#11ff11"));
            StringBuffer codes ;
            String leng = "04";
            codes=new StringBuffer();
            codes.append(this.getResources().getString(R.string.Ncode_Begin));
            codes.append(vdata.devaddrid);
            codes.append(this.getResources().getString(R.string.Ncode_Order_Cut));
            codes.append(leng);
            codes.append(vdata.vtype);
            if ((vdata.getOutchannge()-1) <16){
                String hexnum = Integer.toString((vdata.getOutchannge()-1),16);
                codes.append("0");
                codes.append(hexnum);
            }else if ((vdata.getOutchannge()-1) >15){
            String hexnum = Integer.toString((vdata.getOutchannge()-1),16);
            codes.append(hexnum);
            }
            if ((vdata.getInchannge()-1) <16){
                String hexnum1 = Integer.toString((vdata.getInchannge()-1),16);
                codes.append("0");
                codes.append(hexnum1);
            }else if ((vdata.getInchannge()-1) >15) {
                String hexnum1 = Integer.toString((vdata.getInchannge() - 1), 16);
                codes.append(hexnum1);
            }
            codes.append("00");
            SendCodes(codes.toString());
           // Log.d("1:",codes.toString());
            return  1 ;

        } else if(statu == 0 && Foucse ){
            //取消输出状态
            Foucse = false;
            OutOK =false;
            vdata.setWaiteIn(false);
            this.setText(this.getMyId()+"");
            this.setBackgroundColor(Color.parseColor("#ff1111"));
            Log.d("2:","cancel");
            return 0 ;
        }else if (statu == 0 && !Foucse && !vdata.getSetInOk()){

             //问题代码，暂不开放
            //选中输出但没选输入时
            Foucse = true ;
            OutOK = false ;
            vdata.setWaiteIn(true);
            vdata.setOutchannge(Integer.parseInt(this.getText().toString()));
            vdata.setOwaiteid(Thisid);
            this.setBackgroundColor(Color.parseColor("#11ff11"));
            Log.d("3:","选中输出，等待输入");
            return  0 ;

        }
        else if (statu == 1 && vdata.getWaiteIn() &&!Foucse ){
            //输出选中等待输入
            //问题代码，暂不开放
             Log.d("4:","输出选中,输入");
            vdata.setInchannge(Integer.parseInt(this.getText().toString()));
            Foucse= false;
            Mybutton out = (Mybutton) getRootView().findViewById(vdata.getOwaiteid());
            out.setText(""+vdata.getInchannge()+"->"+vdata.getOutchannge());
            out.setOutOK(true);
            vdata.setSetInOk(false);
            vdata.setWaiteIn(false);

            StringBuffer codes ;
            String leng = "04";
            codes=new StringBuffer();
            codes.append(this.getResources().getString(R.string.Ncode_Begin));
            codes.append(vdata.devaddrid);
            codes.append(this.getResources().getString(R.string.Ncode_Order_Cut));
            codes.append(leng);
            codes.append(vdata.vtype);
            if ((vdata.getOutchannge()-1) <16){
                String hexnum = Integer.toString((vdata.getOutchannge()-1),16);
                codes.append("0");
                codes.append(hexnum);
            }else if ((vdata.getOutchannge()-1) >15){
                String hexnum = Integer.toString((vdata.getOutchannge()-1),16);
                codes.append(hexnum);
            }
            if ((vdata.getInchannge()-1) <16){
                String hexnum1 = Integer.toString((vdata.getInchannge()-1),16);
                codes.append("0");
                codes.append(hexnum1);
            }else if ((vdata.getInchannge()-1) >15) {
                String hexnum1 = Integer.toString((vdata.getInchannge() - 1), 16);
                codes.append(hexnum1);
            }
            codes.append("00");
            SendCodes(codes.toString());

             return  0 ;
        }else if(statu ==1 && !Foucse  &&!thinkon){
            //输入选中
            Foucse =true ;
            if (vdata.getSetInOk()&& vdata.getInClikeoff()){
                @SuppressLint("ResourceType") Mybutton In = (Mybutton) getRootView().findViewById(599+vdata.getInchannge());
                In.setFoucse(false);
                In.setBackgroundColor(Color.parseColor("#ff1111"));
            }
            vdata.setInchannge(Integer.parseInt(this.getText().toString()));
            vdata.setSetInOk(true);
            vdata.setInClikeoff(true);
            this.setBackgroundColor(Color.parseColor("#11ff11"));
            Log.d("5:","输入选中");
            return 1;
        }else if(statu ==1  && Foucse && vdata.getInClikeoff()&&!thinkon){
            //输入放开
            Foucse = false ;
            vdata.setInchannge(0);
            vdata.setSetInOk(false);
            vdata.setInClikeoff(false);
            this.setBackgroundColor(Color.parseColor("#ff1111"));
            Log.d("6:","输入取消");
            return 0;
        }
        return 1;
    }
    public static   void SendCodes(String codes){

        Log.d("getip::",Internets.getIp());
        try {
            Message msg = new Message();
            msg.what = Msid1;
            msg.obj = codes;
            assert (Internets.rehandler != null);

            Internets.rehandler.sendMessage(msg);
        }catch (Exception e){e.printStackTrace();}

    }


    public boolean setOnLong(VData vdata) {
        if (statu ==1 && !Foucse && !thinkon ){
            this.Foucse =true;
         if (vdata.haveall){
             Mybutton IN = (Mybutton) getRootView().findViewById(599+vdata.alloutchange);
             IN.setFoucse(false);
             IN.thinkon =false;
             IN.setText(""+vdata.alloutchange);
             IN.setBackgroundColor(Color.parseColor("#ff1111"));
         }
            vdata.alloutchange = this.MyId;
            vdata.haveall =true;
            thinkon =true;
            this.setBackgroundColor(Color.parseColor("#88ff88"));
            this.setText(""+this.getMyId()+"->ALL");
            StringBuffer codes = new StringBuffer();
            codes.append("BA");
            codes.append(vdata.devaddrid);
            codes.append("31");
            codes.append("05");
            codes.append(vdata.vtype);
            String hexchange = "";
            if (MyId-1 < 16) {
                hexchange = "0" + Integer.toHexString(MyId);
            }else if (MyId-1 >15) {
                hexchange = Integer.toHexString(MyId);
            }
            codes.append(hexchange);
            codes.append("000000");
            SendCodes(codes.toString());
            for (int i=0 ; i <vdata.outleng ;i++)
            {
                Mybutton out = (Mybutton) getRootView().findViewById(800+i);
                out.setText(this.getMyId()+"->"+String.valueOf(i+1));
                vdata.setOutchannge(i+1);
                vdata.setWaiteIn(false);
                out.setFoucse(true);
                out.setBackgroundColor(Color.parseColor("#11ff11"));
                out.setOutOK(true);
            }
           return true;
        }

        if (statu ==1 && Foucse  &&thinkon){
            Foucse = false ;
            thinkon = false;
           // vdata.setInchannge(0);
           // vdata.setSetInOk(false);
            //vdata.setInClikeoff(false);
            vdata.haveall =false ;
            this.setText(this.getMyId()+"");
            this.setBackgroundColor(Color.parseColor("#ff1111"));
            for (int i=0 ; i <vdata.outleng ;i++)
            {
                Mybutton out = (Mybutton) getRootView().findViewById(800+i);
                out.setText(String.valueOf(i+1));
                vdata.setOutchannge(i+1);
                vdata.setWaiteIn(false);
                out.setFoucse(false);
                out.setBackgroundColor(Color.parseColor("#ff1111"));
                out.setOutOK(false);
            }
            return false;
        }


        return false;
    }
}
