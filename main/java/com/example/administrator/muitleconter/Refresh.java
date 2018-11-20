package com.example.administrator.muitleconter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import static com.example.administrator.muitleconter.SetingActivtiy.Internets;
import static com.example.administrator.muitleconter.SetingActivtiy.Msid1;
import static com.example.administrator.muitleconter.SetingActivtiy.vdata;

public class Refresh {


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void Refresh_view(Activity base){
        int Num = Integer.parseInt(vdata.code[3],16)-2;
        for (int i=0 ; i<Num ; i++){
            int Outid =Integer.parseInt(vdata.code[i+5],16);
            i++;
            int InNum = Integer.parseInt(vdata.code[i+5],16)+1;
            Mybutton out = (Mybutton)base.findViewById(800+Outid);
            out.setFoucse(true);
            out.setOutOK(true);
            vdata.setOutchannge(out.getMyId());
            vdata.setWaiteIn(false);
            out.setText(InNum+"->"+vdata.getOutchannge());
            out.setBackground(base.getResources().getDrawable(R.drawable.button_shap_on));
        }

    }

    public static void GetChanngeStatue(int MaxNum ){
        StringBuffer codes = new StringBuffer();
        codes.delete(0,codes.length());
        codes.append("BA");
        codes.append(vdata.devaddrid);
        codes.append("02");
        codes.append(ParseCode.IntToNetByte(MaxNum+2));
        codes.append(vdata.vtype);
        for (int i = 0 ; i<MaxNum ; i++){
            codes.append(ParseCode.IntToNetByte(i));
        }
        codes.append("00");
        SendCodes(codes.toString());
    }

    public static void SendCodes(String codes){

        //   Log.d("getip::",Internets.getIp());
        try {
            Message msg = new Message();
            msg.what = Msid1;
            msg.obj = codes;
            Internets.rehandler.sendMessage(msg);
        }catch (Exception e){e.printStackTrace();}

    }

}
