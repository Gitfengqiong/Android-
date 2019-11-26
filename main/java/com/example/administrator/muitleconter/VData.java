package com.example.administrator.muitleconter;
import android.app.Application;

public class VData extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    private int Outchannge = 0;
    private int Inchannge;
    private boolean waiteIn = false;
    private boolean SetInOk = false;
    private int InClikeNumber = 0;
    private boolean InClikeoff = false;
    private int Owaiteid = 0;
    public boolean LoginOk = false;
    public String ReCode = "null";
    public String devaddrid = "01";
    public String vtype = "AF";
    public String cleng;
    public int inleng, outleng;
    public String Thisip;
    public boolean haveall = false;
    public boolean haveallout = false;
    public int alloutchange;
    public boolean SceneStart = false;
    public int Scenenum = 16;
    public int IsScene = 0;
    public String code[] = new String[256];

    public void setWaiteIn(boolean w) {
        waiteIn = w;
    }

    public boolean getWaiteIn() {
        return waiteIn;
    }

    public void setInchannge(int c) {
        Inchannge = c;
    }

    public int getInchannge() {
        return Inchannge;
    }

    public void setOutchannge(int outchannge) {
        Outchannge = outchannge;
    }

    public int getOutchannge() {
        return Outchannge;
    }

    public void setSetInOk(boolean inOk) {
        SetInOk = inOk;
    }

    public boolean getSetInOk() {
        return SetInOk;
    }

    public void setInClikeNumber(int n) {
        InClikeNumber = n;
    }

    public int getInClikeNumber() {
        return InClikeNumber;
    }

    public void setInClikeoff(boolean inClikeoff) {
        InClikeoff = inClikeoff;
    }

    public boolean getInClikeoff() {
        return InClikeoff;
    }

    public void setOwaiteid(int id) {
        Owaiteid = id;
    }

    public int getOwaiteid() {
        return Owaiteid;
    }

    public char[] my_ethernet_address; //Remark Id ;
}