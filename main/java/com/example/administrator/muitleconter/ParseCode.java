package com.example.administrator.muitleconter;

import android.util.Log;

//import static com.example.administrator.muitleconter.MainActivity.vdate;
import static com.example.administrator.muitleconter.MainActivity.iPlist;
import static com.example.administrator.muitleconter.MainActivity.ipIndex;
import static com.example.administrator.muitleconter.MainActivity.vdate;
import static java.lang.String.*;


public class ParseCode {

     public static void Parsecode(String data ) {
         /* VData v =vdata; */
         for (int i = 0; i < data.length() - 1; i++) {
             Log.d("i:", valueOf(i));
             if (i >= data.length() - 1) {
                 break;
             }
             String substring;
             substring = data.substring(i, i + 2).toUpperCase();
             Log.d("Subs1:", substring);
             //一条指令
             if (substring.equals("BA") && (data.length() - i) > 2) {
                 Log.d("find code", valueOf(i));
                 for (int j = i + 2, mode = 1; 1 < data.length() - j; j++) {
                     substring = data.substring(j, j + 2).toUpperCase();
                     Log.d("Subs2:", substring);
                     if (data.length() - j < 2 || substring.length() < 2 || substring.equals("BA")) {
                         i = j - 2;
                         Log.d("break code j:", valueOf(j));
                         break;
                     }
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

                     Log.d("next code mode", valueOf(j));
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
                    Log.d("Subs1:", substring);
                    substring = data.substring(i,i+=2);
                    if  (substring.equals("01") && (data.length() - i) > 2){
                     //   Log.d("Subs1:", substring);
                        substring = data.substring(i,i+=2);
                        if (substring.equals("10") && (data.length() - i) > 2){
                            //数据区
                            //Mac Address
                           // ipIndex = 0 ;
                             Log.d("Subs1:", substring);
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


 }

