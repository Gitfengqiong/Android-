package com.example.administrator.muitleconter;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class MyUdpIo implements Runnable {

        private Handler handler;
        public Handler rehandler;
        private DatagramSocket s , s1;
        DatagramPacket br = null;
        DatagramPacket ow = null;
        private String Ip;
        private  int Localport = 4069;
        private  int diverport = 4068;
        private  int Mreid;
        private  int Mseid;
        byte Data[]=new byte[255];
        private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        public MyUdpIo(Handler handler){
            this.handler =handler;
        }
        public MyUdpIo(Handler handler , String ip ){
            this.handler=handler;
            this.Ip = ip ;
        }
        public MyUdpIo(Handler handler , String ip ,int Mrid ,int Msid){
            this.handler= handler;
            this.Ip = ip;
            this.Mreid = Mrid;
            this.Mseid = Msid;
        }
    public static String bytesToHexS(byte[] bytes) {
        // 一个byte为8位，可用两个十六进制位标识
        char[] buf = new char[bytes.length * 2];
        int a = 0;
        int index = 0;
        for(byte b : bytes) { // 使用除与取余进行转换
            if(b < 0) {
                a = 256 + b;
            } else {
                a = b;
            }

            buf[index++] = HEX_CHAR[a / 16];
            buf[index++] = HEX_CHAR[a % 16];
        }

        return new String(buf);
    }

    public static char[] bytesToHexC(byte[] bytes) {
        char[] buf = new char[bytes.length * 2];
        int index = 0;
        for(byte b : bytes) { // 利用位运算进行转换，可以看作方法一的变种
            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
            buf[index++] = HEX_CHAR[b & 0xf];
        }

        return buf;
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
        @SuppressLint("HandlerLeak")
        @Override
        public void   run() {


            try {


                new Thread(){
                    @Override
                    public void run() {
                        super.run();

                        String Connet =null;//服务器端数据
                       while (true) {
                           try {
                               if(s==null){
                                   s = new DatagramSocket(null);
                                   s.setReuseAddress(true);
                                   s.bind(new InetSocketAddress(Localport));
                               }
                               // s=new DatagramSocket(Localport);
                               br = new DatagramPacket(Data,Data.length) ;
                               s.receive(br);
                               if (br.getLength() > 0) {
                                   /* 收到数据后返回数据到主线 */
                                   Message msg = new Message();
                                   msg.what = Mreid;
                                   msg.obj = bytesToHexString(br.getData());
                                   handler.sendMessage(msg);
                                   br.setLength(0);
                               }
                               s.close();
                               s=null;
                               br.setLength(0);


                           } catch (IOException e) {
                               e.printStackTrace();
                           }
                       }
                    }
                }.start();//启动接收线程
                Looper.prepare();//当前线程初始化Looper
                 //接收主线程信息，发送到网络上

                rehandler = new Handler(){

                    @Override
                    public void  handleMessage(Message msg) {

                        if (msg.what == Mseid){
                            try {
                                InetAddress serverAddress = InetAddress.getByName(Ip);
                                String tmp = msg.obj.toString();
                                byte[] DataH = StringtoBytes(tmp);
                                ow = new DatagramPacket(DataH,DataH.length,serverAddress,diverport) ;
                                s.send(ow);
                                DataH = null;
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                };
                Looper.loop();
            }

            catch (Exception e){
                e.printStackTrace();
            }

        }

    public void setIp(String ip) {
        Ip = ip;
    }
    public  String getIp(){return Ip;}
    public  void setMreid(int mreid){Mreid = mreid;}
    public  void setMseid(int  mseid){Mseid =mseid;}
}

