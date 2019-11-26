package com.example.administrator.muitleconter;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import static com.example.administrator.muitleconter.MainActivity.SceneRemark;
import static com.example.administrator.muitleconter.MainActivity.config;
import static com.example.administrator.muitleconter.MainActivity.iPlist;
import static com.example.administrator.muitleconter.MainActivity.ipIndex;
import static com.example.administrator.muitleconter.MainActivity.vdate;
import static java.lang.String.valueOf;

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

    public static class ParseCode {

         public static void Parsecode(String data ) {
             /* VData v =vdata; */
             for (int i = 0; i < data.length() - 1; i++) {
                 Log.d("i:", valueOf(i));
                 if (i >= data.length() - 1) {
                     break;
                 }
                 String substring;
                 substring = data.substring(i, i + 2).toUpperCase();
               //  Log.d("Subs1:", substring);
                 //一条指令
                 if (substring.equals("BA") && (data.length() - i) > 2) {
                  //   Log.d("find code", valueOf(i));
                     for (int j = i + 2, mode = 1; 1 < data.length() - j; j++) {
                         substring = data.substring(j, j + 2).toUpperCase();
                     //    Log.d("Subs2:", substring);
                         if (data.length() - j < 2 || substring.length() < 2 || substring.equals("BA")) {
                             i = j - 2;
                       //      Log.d("break code j:", valueOf(j));
                             break;
                         }
                         /*
                         switch (mode) {
                             case 1: {
                                 vdate.code1 = valueOf(substring);
                                 mode++;
                                 if (Debug.Endebug) {
                                     Log.d("c1:", vdate.code1);
                                 }
                                 break;
                             }
                             case 2: {
                                 vdate.code2 = String.valueOf(substring);
                                 mode++;
                                 if (Debug.Endebug) {
                                     Log.d("c2:", vdate.code2);
                                 }
                                 break;
                             }
                             case 3: {
                                 vdate.code3 = valueOf(substring);
                                 mode++;
                                 if (Debug.Endebug) {
                                     Log.d("c3:", vdate.code3);
                                 }
                                 break;
                             }
                             case 4: {
                                 vdate.code4 = valueOf(substring);
                                 mode++;
                                 if (Debug.Endebug) {
                                     Log.d("c4:", vdate.code4);
                                 }
                                 break;
                             }
                             case 5: {
                                 vdate.code5 = valueOf(substring);
                                 mode++;
                                 if (Debug.Endebug) {
                                     Log.d("c5:", vdate.code5);
                                 }
                                 break;
                             }
                             case 6: {
                                 vdate.code6 = valueOf(substring);
                                 mode++;
                                 if (Debug.Endebug) {
                                     Log.d("c6:", vdate.code6);
                                 }
                                 break;
                             }
                             case 7: {
                                 vdate.code7 = valueOf(substring);
                                 mode++;
                                 if (Debug.Endebug) {
                                     Log.d("c7:", vdate.code7);
                                 }
                                 break;
                             }
                             case 8: {
                                 vdate.code8 = valueOf(substring);
                                 mode++;
                                 if (Debug.Endebug) {
                                     Log.d("c8:", vdate.code8);
                                 }
                                 break;
                             }
                             case 9: {
                                 vdate.code9 = valueOf(substring);
                                 mode++;
                                 if (Debug.Endebug) {
                                     Log.d("c9:", vdate.code9);
                                 }
                                 break;
                             }
                             case 10: {
                                 vdate.code10 = valueOf(substring);
                                 mode++;
                                 if (Debug.Endebug) {
                                     Log.d("c10:", vdate.code10);
                                 }
                                 break;
                             }


                             default:
                                 continue;
                         }

    */

                         vdate.code[mode++] =valueOf(substring);
                         j++;
                         i = j + 3;
                     }
                 }
                 i++;
             }
         }

         public  static int ParseMultiCodes(String data){
             int offset = 2;
             String substring;
             data = data.toUpperCase();
            for (int i =0; i<data.length()-1 ;){
                if (i >= data.length() - 2) {
                    return 0;
                }
                substring = data.substring(i, i += 2);
                //一条返回
                if (substring.equals("EF") && (data.length() - i) > 2) {
                   // Log.d("Subs1:", substring);
                    substring = data.substring(i,i+=2);
                   // Log.d("sub2",substring);
                    if (substring.equals("FE") && (data.length() - i) > 2){
                        substring = data.substring(i,i+=2);
                        if  (substring.equals("01") && (data.length() - i) > 2){
                         //   Log.d("Subs1:", substring);
                            substring = data.substring(i,i+=2);
                            if (substring.equals("10") && (data.length() - i) > 2){
                                //数据区
                                //Mac Address
                               // ipIndex = 0 ;
                          //       Log.d("Subs1:", substring);
                               String Mac = data.substring(i,i+=6*offset);
                               if (Debug.Endebug){
                                   Log.d("Codes:",Mac);
                               }
                               iPlist[ipIndex].my_ethernet_address=Mac;
                                //IP
                                String IP = data.substring(i,i+=4*offset);
                                if (Debug.Endebug){
                                    Log.d("Codes:",IP);
                                }
                                iPlist[ipIndex].my_ip_address=IP;
                                //MULTICAST
                                String multcast = data.substring(i,i+=4*offset);
                                if (Debug.Endebug){
                                    Log.d("Codes:",multcast);
                                }
                                iPlist[ipIndex].multicast_ip_address = multcast;
                                //HOSTIP
                                String hostip = data.substring(i,i+=4*offset);
                                if (Debug.Endebug){
                                    Log.d("Codes:",hostip);
                                }
                                iPlist[ipIndex].host_ip_address = hostip;
                                //GATEWAY
                                String  gateway = data.substring(i,i+=4*offset);
                                if (Debug.Endebug){
                                    Log.d("Codes:",gateway);
                                }
                                iPlist[ipIndex].gateway_ip_address = gateway;
                                //MASK
                                String mask = data.substring(i,i+=4*offset);
                                if (Debug.Endebug){
                                    Log.d("Codes:",mask);
                                }
                                iPlist[ipIndex].mask_ip_address = mask;
                                //LOCAL PORT
                                String localport = data.substring(i,i+=2*offset);
                                if (Debug.Endebug){
                                    Log.d("Codes:",localport);
                                }
                                iPlist[ipIndex].m_LocalPort = ByteSwap(localport);
                                //HOST PORT
                                String hostport = data.substring(i,i+=2*offset);
                                if (Debug.Endebug){
                                    Log.d("Codes:",hostport);
                                }
                                iPlist[ipIndex].m_HostPort = ByteSwap(hostport);
                                //net working mode
                                String nwm = data.substring(i,i+=1*offset);
                                if (Debug.Endebug){
                                    Log.d("Codes:",nwm);
                                }
                                iPlist[ipIndex].m_NetMode = nwm;
                                //buad
                                String buad = data.substring(i,i+=1*offset);
                                if (Debug.Endebug){
                                    Log.d("Codes:",buad);
                                }
                                iPlist[ipIndex].m_CommBaud = buad;
                                //ID
                                String Id = data.substring(i,i+=1*offset);
                                if (Debug.Endebug){
                                    Log.d("Codes:",Id);
                                }
                                iPlist[ipIndex].m_ID = Id;
                                //device name
                                String name = data.substring(i,i+=6*offset);
                                if (Debug.Endebug){
                                    Log.d("Codes:",name);
                                }
                                iPlist[ipIndex].DEVICE_name = name;
                                //device model
                                String model =data.substring(i,i+=3*offset);
                                if (Debug.Endebug){
                                    Log.d("Codes:",model);
                                }
                                iPlist[ipIndex].DECICE_model = model;
                                //ip_mode
                                String ipmode =data.substring(i,i+=1*offset);
                                if (Debug.Endebug){
                                    Log.d("Codes:",ipmode);
                                }
                                iPlist[ipIndex].m_IPMode = ipmode;
                                //login password
                                String passwd=data.substring(i,i+=3*offset);
                                if (Debug.Endebug){
                                    Log.d("Codes:",passwd);
                                }
                                iPlist[ipIndex].password = passwd;
                                //产品识别码
                                String devid = data.substring(i,i+=6*offset);
                                if (Debug.Endebug){
                                    Log.d("Codes:",devid);
                                }
                                iPlist[ipIndex].identify = devid;
                                //dns
                                String dns = data.substring(i,i+=4*offset);
                                if (Debug.Endebug){
                                    Log.d("Codes:",dns);
                                }
                                iPlist[ipIndex].dns_ip = dns;
                                ipIndex++;
                            }else {
                                if (i < data.length()-60)
                                                   {
                                                       return 1;
                                                   }
                                continue;
                            }

                        }else {

                            //i+=2;
                            continue;
                        }

                    }else {
                       // i+=2;
                        continue;
                    }

                }else {
                    //i+=2;
                    continue;
                }

            }
            return 1;
         }

         public static String parseIpSlice(String ip_n){
             if (ip_n.length()<1){
                 return  "erro";
             }
             if (Integer.parseInt(ip_n) >255){
                 return  "erro";
             }
             if (Integer.parseInt(ip_n) <16)
             {
                 return "0"+Integer.toHexString(Integer.parseInt(ip_n));
             }
             return Integer.toHexString(Integer.parseInt(ip_n));

         }
        public  static String ByteSwap(String data ){
             int dlengs= data.length();

                 for (int i = 0 ;i<4-dlengs;i++)
                 {
                     data="0"+data;
                 }
             String d1 , d2 ;
             d1 = data.substring(0,2);
             d2 = data.substring(2,4);
             return d2+d1;
        }

        public static void parseSceneRemark(String FlieName) {
            try {
                /*
                 * 注意：获取流的方式通过openFileInput函数，指定文件名以及后缀
                 * 参数1.文件名和后缀        2.文件模式
                 * 保存在手机data/data/包名/files
                 * */
                FileInputStream fis = new FileInputStream(FlieName);
                InputStreamReader is = new InputStreamReader(fis, "UTF-8");
                //fis.available()文件可用长度
                char input[] = new char[fis.available()];
                is.read(input);

                if (fis.available() == 0) {
                    String buff =String.valueOf(input);
                    is.close();
                    fis.close();
                    int leng = buff.length() ;
                    String substring ;
                    StringBuffer inde = new StringBuffer(3);
                    for (int i = 0; i < leng; ) {
                        substring = buff.substring(i, ++i);
                       // Log.d("this",substring);
                        if (substring.equals("$") && (leng > i)) {
                            inde.delete(0,inde.length());
                            substring = buff.substring(i, ++i);
                            while (!substring.equals(":")){
                                inde.append(substring);
                                substring = buff.substring(i,++i);
                            }
                            int index = Integer.parseInt(inde.toString(),10);
                            if (substring.equals(":") && (leng > i)) {
                                StringBuffer b = new StringBuffer();
                                substring = buff.substring(i,++i);
                                while (!substring.equals("$") && (leng >= i)) {

                                    b.append(substring);
                                    if (i == leng){
                                        substring = buff.substring(i,i++);
                                    }else {
                                        substring = buff.substring(i, ++i);
                                    }
                                }
                                SceneRemark[index - 1] = b.toString();

                                i--;
                            }
                        }

                    }
                }

            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }

        }

        public static void SaveSceneRemark(String FileName ) {
            try {
                //将文件数据写到应用的内部存储
                /*
                 * 注意：获取流的方式通过openFileInput函数，指定文件名以及后缀
                 * 参数1.文件名和后缀        2.文件模式
                 * 保存在手机data/data/包名/files
                 * */
                config.delete();
                config = new File(FileName);
                config.createNewFile();
                FileOutputStream fos= new FileOutputStream(FileName);
                OutputStreamWriter osw=new OutputStreamWriter(fos,"UTF-8");
                StringBuffer Scene = new StringBuffer();
                for (int i = 0 ; i <vdate.Scenenum ; i++){
                    Scene.append("$").append(i+1).append(":").append(SceneRemark[i]);
                }
                osw.write(Scene.toString());
                //保证输出缓冲区中的所有内容
                osw.flush();
                fos.flush();
                //后打开的先关闭，逐层向内关闭
                fos.close();
                osw.close();
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        public static void parseChanngeRemark(String FlieName , String [] data ) {
            try {
                /*
                 * 注意：获取流的方式通过openFileInput函数，指定文件名以及后缀
                 * 参数1.文件名和后缀        2.文件模式
                 * 保存在手机data/data/包名/files
                 * */
                FileInputStream fis = new FileInputStream(FlieName);
                InputStreamReader is = new InputStreamReader(fis, "UTF-8");
                //fis.available()文件可用长度
                char input[] = new char[fis.available()];
                is.read(input);

                if (fis.available() == 0) {
                    String buff =String.valueOf(input);
                    is.close();
                    fis.close();
                    int leng = buff.length() ;
                    String substring ;
                    StringBuffer inde = new StringBuffer(3);
                    for (int i = 0; i < leng; ) {
                        substring = buff.substring(i, ++i);
                        // Log.d("this",substring);
                        if (substring.equals("$") && (leng > i)) {
                            inde.delete(0,inde.length());
                            substring = buff.substring(i, ++i);
                            while (!substring.equals(":")){
                                inde.append(substring);
                                substring = buff.substring(i,++i);
                            }
                            int index = Integer.parseInt(inde.toString(),10);
                            if (substring.equals(":") && (leng > i)) {
                                StringBuffer b = new StringBuffer();
                                substring = buff.substring(i,++i);
                                while (!substring.equals("$") && (leng >= i)) {

                                    b.append(substring);
                                    if (i == leng){
                                        substring = buff.substring(i,i++);
                                    }else {
                                        substring = buff.substring(i, ++i);
                                    }
                                }
                                data[index - 1] = b.toString();

                                i--;
                            }
                        }

                    }
                }

            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }

        }


        public static void SaveChanngeRemark(String FileName ,String[] data , File flies , int dataNum) {
            try {
                //将文件数据写到应用的内部存储
                /*
                 * 注意：获取流的方式通过openFileInput函数，指定文件名以及后缀
                 * 参数1.文件名和后缀        2.文件模式
                 * 保存在手机data/data/包名/files
                 * */
                flies.delete();
                flies = new File(FileName);
                flies.createNewFile();
                FileOutputStream fos= new FileOutputStream(FileName);
                OutputStreamWriter osw=new OutputStreamWriter(fos,"UTF-8");
                StringBuffer Scene = new StringBuffer();
                for (int i = 0 ; i <dataNum ; i++){
                    Scene.append("$").append(i+1).append(":").append(data[i]);
                }
                osw.write(Scene.toString());
                //保证输出缓冲区中的所有内容
                osw.flush();
                fos.flush();
                //后打开的先关闭，逐层向内关闭
                fos.close();
                osw.close();
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }


        public static String IntToNetByte(int num){
           StringBuffer netbyte =new StringBuffer() ;
           if (num<16){
               netbyte.append("0");
               netbyte.append(Integer.toString(num,16));
           }else {
               netbyte.append(Integer.toString(num,16));
           }
           return netbyte.toString();
        }

        public static  String[] IpStringToIpArray(String IP){
             String IPSpilt[] = new String[4];
             StringBuffer s= new StringBuffer();
             String sub ;
            for (int i=0 ,j =0 ; i<IP.length() ;){

              sub = IP.substring(i,++i);
              if (!sub.equals(".")&&!sub.equals("null")){
                  s.append(sub);
                  if (i >= IP.length())
                  {
                       IPSpilt[j++] = s.toString();
                      s.delete(0,s.length());
                  }


              }else {

                  IPSpilt[j++] = s.toString();
                  s.delete(0,s.length());
              }
             }
             return IPSpilt ;
        }

     }
}

