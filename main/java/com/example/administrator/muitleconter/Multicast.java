package com.example.administrator.muitleconter;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import static java.util.logging.Logger.*;

public class Multicast  implements Runnable {
    public String mip;
    public  int mport;
    private Handler handler;
    public Handler rehandler;
    private  int Mreid;
    private  int Mseid;
    public String codes;
   private MulticastSocket ms = null;
    private MulticastSocket s = null;
   private DatagramPacket dp = null;
    private DatagramPacket dr = null;
    int length = 120;
    byte[] buf = new byte[length];
    private InetAddress groupr = null;
    public Multicast(String mIP , int port ,Handler handler1 ,int Mrid ,int Msid , String code) {
        mip = mIP;
        mport = port;
        handler = handler1;
        Mreid =  Mrid;
        Mseid = Msid;
        codes = code;
    }

    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] StringtoBytes(String str) {
        if(str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void run() {
        String host = mip;//多播地址


        if(ms==null) {
            try {
                ms=new MulticastSocket(mport);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    synchronized (this) {
                        while (true) {
                            ms = new MulticastSocket(mport);


                            //加入多播地址
                            InetAddress group = InetAddress.getByName(mip);
                            ms.joinGroup(group);
                            dp = new DatagramPacket(buf, length, group, mport);
                            // System.out.println("监听多播端口打开：");
                            ms.receive(dp);
                            while (dp.getLength() > 0) {
                                Message msg = new Message();
                                msg.what = Mreid;
                                msg.obj = bytesToHexString(buf);
                                handler.sendMessage(msg);
                                Log.d("信息", bytesToHexString(buf));
                                ms.leaveGroup(group);
                                ms.close();
                                dp.setLength(0);
                            }
                            dp = null;

                        }
                    }
                    } catch(IOException e){
                        e.printStackTrace();
                    }

                }

        }.start();
        Looper.prepare();
        rehandler = new Handler(){

            @Override
            public void handleMessage(Message msg) {

                super.handleMessage(msg);
                if(msg.what ==Mseid){
                    try {
                        if (s == null){

                            try {
                                groupr = InetAddress.getByName(mip);
                            } catch (UnknownHostException e) {
                                e.printStackTrace();
                            }
                            try {
                                s = new MulticastSocket(mport);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //加入多播组
                            try {
                                s.joinGroup(groupr);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                       byte btes[] = StringtoBytes(msg.obj.toString());
                        dr  = new DatagramPacket(btes,btes.length,groupr,mport);
                        s.send(dr);
                        s.leaveGroup(groupr);
                        s.close();
                        s=null;
                        dr=null;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };

        Looper.loop();



    }

    public  void Client(String codes) {
        String host = mip;//多播地址
        int port = mport;
        String message = codes;
        try {
            InetAddress group = InetAddress.getByName(host);
            MulticastSocket s = new MulticastSocket();
            //加入多播组
            s.joinGroup(group);
            DatagramPacket dp = new DatagramPacket(message.getBytes(),message.length(),group,port);
            s.send(dp);
            s.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public   void   Severs(String mip ,int mport ){
        //接受组播和发送组播的数据报服务都要把组播地址添加进来
        String host = mip;//多播地址
        int port = mport;
        int length = 120;
        byte[] buf = new byte[length];
        MulticastSocket ms = null;
        DatagramPacket dp = null;
        StringBuffer sbuf = new StringBuffer();
        try {
            ms = new MulticastSocket(port);
            dp = new DatagramPacket(buf, length);

            //加入多播地址
            InetAddress group = InetAddress.getByName(host);
            ms.joinGroup(group);

            System.out.println("监听多播端口打开：");
            ms.receive(dp);
            ms.close();
            int i;
            for(i=0;i<1024;i++){
                if(buf[i] == 0){
                    break;
                }
                sbuf.append((char) buf[i]);
            }
            System.out.println("收到多播消息：" + sbuf.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
