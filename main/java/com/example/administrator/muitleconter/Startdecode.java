package com.example.administrator.muitleconter;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.xm.ChnInfo;
import com.xm.DevInfo;
import com.xm.GlobalData;
import com.xm.MyConfig;
import com.xm.NetSdk;
import com.xm.dialog.RadarSearchDevicesDlg;
import com.xm.utils.OutputDebug;
import com.xm.video.MySurfaceView;
import com.xm.view.MyDirection;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Startdecode {
   /*
    static {
        System.loadLibrary("xmnetsdk");
        System.loadLibrary("spotify");
        System.loadLibrary("spotifywrapper");
    }
    public static native int   H264_DVR_Init();
    public static native int  Startsdk();
    */
    private MySurfaceView View[];
    private SurfaceHolder Vholder[];
    private int Chnum ;
    private NetSdk mNetSdk = null;
    private RelativeLayout talkBackLayout;
    private TranslateAnimation mShowAnimationDownToUp;
    private TranslateAnimation mHideAnimationUpToDown;
    private MyDirection mDirection; // 云台控件
    private boolean mbPTZ = false;
    private long[] mplayhandles = new long[4];
    private long mlVoiceHandle = 0; // 对讲句柄
    private int mbConnectMode = MyConfig.ConnectMode.monitorconn; // 默认是监控连接
    private AlertDialog mAudioRecordDlg = null; // 语音对讲提示框
    private int mWndSelected = 0; // 选中的窗口
    private TranslateAnimation mShowAnimation;
    private TranslateAnimation mHideAnimation;
    protected String mIpTV ;// ip
    protected String mPortTV ; // 端口
    protected String mUserNameTV ; // 用户名
    protected String mPassWordTV ; // 密码
    /*
    private Button mCancel = null; // 取消
    private TextView sum, num[]; // 总通道数，窗口显示通道号
    private TextView mSnTV = null; // 序列号

    private EditText mSnET = null; // 序列号
    private EditText mIpET = null; // ip
    private EditText mPortET = null; // 端口
    private EditText mUserNameET = null; // 用户名
    private EditText mPassWordET = null; // 密码
    private Spinner mLoginTypeSP = null; // 登陆类型:普通登陆、云方式登陆
    */
    public  WndsHolder mWndsHolder;
    private int mSocketStyle = MyConfig.SocketStyle.TCPSOCKET;
    private Bitmap mCatchPic; // 抓图图片
    private int miStreamType = MyConfig.StreamType.Extra; // 当前码流类型，默认是副码流
    private DevInfo mDevInfo = new DevInfo();
 //   private FileOutputStream fos;
    private int ReviewState[]; // 预览状态“播放1停止0”
    private int Channel[]; // 窗口0 1 2 3预览通道号
  //  private MyWifiManager mWifiManager;
    private RadarSearchDevicesDlg dlg;
    private boolean isLogin = false;
    protected  static NetSdk netSdk ;
    Startdecode(MySurfaceView[] v ,SurfaceHolder[] h , int chn){
        View = v ;
        Vholder = h ;
        Chnum = chn ;
    }

    Startdecode(int chn){
        Chnum =chn;
    }


    public  boolean Start_sdk(String StartIP){
        ReviewState = new int[] { 0, 0, 0, 0 };
        Channel = new int[] { -1, -1, -1, -1 };
        netSdk = new NetSdk();
        netSdk.DevInit();
        mNetSdk = netSdk.getInstance();
        mNetSdk.onStopAlarmMsg(false);
        mNetSdk.setOnAlarmListener(new NetSdk.OnAlarmListener() {

            @Override
            public void onAlarm(long loginid, int ichannel, int iEvent, int iStatus, int[] time) {

                Log.d( "iEvent:", String.valueOf(iEvent));
            }
        });


        for (int i = 0 ; i<Chnum ; i++){
            final int finalI = i;

                            mWndSelected = finalI;
                            onLogin(StartIP, "34567", "admin", "" ,mWndSelected);
                            onPlay(mWndSelected);


        }

      return  true ;
    }

    private void onLogin(String ip, String port, String name, String pwd ,int chn) {
     //   SPUtil.getInstance(this).setSettingParam("Ip", ip);
     //   SPUtil.getInstance(this).setSettingParam("Port", port);
     //   SPUtil.getInstance(this).setSettingParam("Name", name);
     //   SPUtil.getInstance(this).setSettingParam("Pwd", pwd);
        mDevInfo.Ip = ip;// 这里可以填写ip或者序列号 mDevInfo.TCPPort =
        // 如果devInfo.Ip填写了ip则需要在这个地方填写端口
        mDevInfo.TCPPort = !port.equals("") ? Integer.parseInt(port) : 34567;
        mDevInfo.UserName = name.getBytes();
        mDevInfo.PassWord = pwd;
        if (isSn(mDevInfo.Ip))
            mDevInfo.Socketstyle = MyConfig.SocketStyle.NATSOCKET;// 如果devInfo.Ip为ip地址则这边需要改成SocketStyle.TCPSOCKET,如果是序列号则为SOCKETNR
        else
            mDevInfo.Socketstyle = MyConfig.SocketStyle.TCPSOCKET;
        mSocketStyle = mDevInfo.Socketstyle;// //如果devInfo.Ip为ip地址则这边需要改成SocketStyle.TCPSOCKET,如果是序列号则为SOCKETNR
        // mWndSelected为选中的窗口id
        int[] mLoginError = new int[1]; // 登陆错误值
        GlobalData.mLoginId = mNetSdk.onLoginDevNat(chn, mDevInfo, mLoginError, mSocketStyle);
        mNetSdk.SetupAlarmChan(GlobalData.mLoginId);
        // 设置报警回调函数
        mNetSdk.SetAlarmMessageCallBack();
       // OutputDebug.OutputDebugLogE("LoginError", "chnNum:" + mDevInfo.DigChannel);
    }

    public static boolean isSn(String sn) {
        return isPatternMatches(sn, "^[A-Za-z0-9]{16}$");
    }

    public static boolean isPatternMatches(String val, String match) {
        Pattern p = Pattern.compile(match);
        Matcher m = p.matcher(val);
        if (m.matches())
            return true;
        else
            return false;
    }


    public void onPlay(int chn) {

                    // 下面就是打开通道需要做的准备工作
                    ChnInfo chnInfo = new ChnInfo();
                    // chnInfo.ChannelNo = 0;//通道ID
                    chnInfo.nStream = miStreamType;// 0表示主码流，1表示副码流
                    if (chn >= mDevInfo.getAllChnNum())
                        chnInfo.ChannelNo = 1;
                    else
                        chnInfo.ChannelNo = chn;
                    int position = 0;
                    for (int i : Channel) {
                        if (i == chnInfo.ChannelNo) {
                            onStopChn(position);

                        }
                        position++;
                    }
                    onReview(chnInfo,chn);

                    getMySurface(chn).setAudioCtrl(MyConfig.AudioState.CLOSED);
                    getMySurface(chn).onPlay();
                    if (chn == 0 ) {
                   //     getMySurface(4).setAudioCtrl(MyConfig.AudioState.CLOSED);
                     //   getMySurface(4).onPlay();
                    }
                    mNetSdk.setReceiveCompleteVData(0, true);


    }


    private void onReview(ChnInfo chnInfo , int chn) {
        int position = 0;
        for (int i : Channel) {
            if (i == chnInfo.ChannelNo) {
                Channel[position] = -1;
                ReviewState[position] = 0;
                onStopChn(position);
            }
            position++;
        }
        // 打开实时预览
        mplayhandles[chn] = mNetSdk.onRealPlay(chn, GlobalData.mLoginId, chnInfo);
        // 以下就是视频数据接收的缓冲区大小，默认是10个
        mNetSdk.SetVBufferCount(30);
        // 设置数据回调
        mNetSdk.setDataCallback(mplayhandles[mWndSelected]);
        switch (chn) {
            case 0:
                mWndsHolder.vv1.initData();
               // mWndsHolder.vv5.initData();
                break;
            case 1:
                mWndsHolder.vv2.initData();
                break;
            case 2:
                mWndsHolder.vv3.initData();
                break;
            case 3:
                mWndsHolder.vv4.initData();
                break;
        }
        Channel[chn] = chnInfo.ChannelNo;
        ReviewState[chn] = 1;
    }

    private MySurfaceView getMySurface(int position) {
        switch (position) {
            case 0:
                return mWndsHolder.vv1;

            case 1:
                return mWndsHolder.vv2;
            case 2:
                return mWndsHolder.vv3;
            case 3:
                return mWndsHolder.vv4;
            case 4:
            //    return mWndsHolder.vv5;
            default:
                return null;
        }
    }


    private void onStopChn(int pos) {
        switch (pos) {
            case 0:
                mWndsHolder.vv1.onStop();
           //     mWndsHolder.vv5.onStop();
                break;
            case 1:
                mWndsHolder.vv2.onStop();
                break;
            case 2:
                mWndsHolder.vv3.onStop();
                break;
            case 3:
                mWndsHolder.vv4.onStop();
                break;
            case 4:
                mWndsHolder.vv1.onStop();
                mWndsHolder.vv2.onStop();
                mWndsHolder.vv3.onStop();
                mWndsHolder.vv4.onStop();
              //  mWndsHolder.vv5.onStop();
                break;
            default:
                pos = 0;
                break;
        }
        if (pos < 4) {
            ReviewState[pos] = 0;
            Channel[pos] = -1;

            if (mplayhandles[pos] > 0) {
                mNetSdk.onStopRealPlay(mplayhandles[pos]);
                mplayhandles[pos] = 0;
            }
        } else {
            for (int i = 0; i <= 3; i++) {
                if (ReviewState[i] == 1) {
                    ReviewState[i] = 0;
                    Channel[i] = -1;

                }
                if (mplayhandles[i] > 0) {
                    mNetSdk.onStopRealPlay(mplayhandles[i]);
                    mplayhandles[i] = 0;
                }
            }

        }

    }

    public boolean Start_test() {

        for (int i = 0; i < Chnum; i++) {
            try {
                synchronized (this) {
                    wait(100);

                    final int finalI = i;
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();

                            // Log.d("Load video", "vvvvvv");
                            final MediaPlayer mediaPlayer = new MediaPlayer();
                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);//静音
                            mediaPlayer.setVolume(0f, 0f);
                            try {
                                mediaPlayer.setDataSource("/storage/emulated/0/v" + String.valueOf(finalI + 1) + ".mp4");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Vholder[finalI].addCallback(new SurfaceHolder.Callback() {
                                @Override
                                public void surfaceCreated(SurfaceHolder holder) {
                                //    Log.d("Create", "holder");

                                    mediaPlayer.setDisplay(Vholder[finalI]);
                                    mediaPlayer.start();
                                    Vholder[finalI].setKeepScreenOn(true);

                                }

                                @Override
                                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                                    //  mediaPlayer.start();
                                  //  Log.d("Changed", "holder");
                                }

                                @Override
                                public void surfaceDestroyed(SurfaceHolder holder) {
                                 //   Log.d("Destroyed", "holder");
                                    if (mediaPlayer != null) {
                                        mediaPlayer.pause();
                                        //释放资源
                                    }

                                }
                            });
                            try {
                                mediaPlayer.prepare();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    //准备完成后播放
                                    //                mp.setDisplay(Vholder[finalI]);
                                    //                mp.start();
                                    mp.setLooping(true);

                                }
                            });


                        }
                    }.start();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
       static class  WndsHolder {
        MySurfaceView vv1;
        MySurfaceView vv2;
        MySurfaceView vv3;
        MySurfaceView vv4;
      //  MySurfaceView vv5;
    }

}
