package com.example.administrator.muitleconter;

import android.annotation.SuppressLint;
import android.os.Looper;
import android.os.Message;
import android.os.Handler;
import java.net.Socket;
import java.io.*;
import java.net.SocketTimeoutException;

public class MyTcpClient implements Runnable {
    private Handler handler;
    private Handler rehandler;
    private Socket s;
    BufferedReader br = null;
    OutputStream ow = null;
    private String Ip;
    private int port;

    public MyTcpClient(Handler handler){
        this.handler =handler;
    }
    public MyTcpClient(Handler handler , String ip , int port){
        this.handler=handler;
        this.Ip = ip ;
        this.port =port ;
    }
    @SuppressLint("HandlerLeak")
    @Override
    public void run() {

        try {

            s=new Socket(Ip,port);
            s.setSoTimeout(3000);
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            ow = s.getOutputStream();
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    String Connet =null;//服务器端数据
                    try {
                        while ((Connet=br.readLine())!=null)
                        {
                            //收到数据后返回数据到主线
                            Message msg =new Message();
                            msg.what = 0x110;
                            msg.obj = Connet;
                            handler.sendMessage(msg);
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }.start();//启动接收线程
            Looper.prepare();//当前线程初始化Looper
            rehandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 0x120){
                        try {
                            ow.write((msg.obj.toString()).getBytes("utf-8"));
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            };
            Looper.loop();
        }
        catch (SocketTimeoutException e){
            //网络超时错误
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }
}