package com.example.administrator.muitleconter;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

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
    private SurfaceView View[];
    private SurfaceHolder Vholder[];
    private int Chnum ;
    private  MediaPlayer mediaPlayer[] ;
    Startdecode(SurfaceView[] v ,SurfaceHolder[] h , int chn){
        View = v ;
        Vholder = h ;
        Chnum = chn ;
    }

    public boolean Start() {
        //Startsdk();
        for (int i = 0; i < 4; i++)
            try {
                synchronized (this) {
                    wait(100);

                    final int finalI = i;
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();

                            Log.d("Load video", "vvvvvv");
                            final MediaPlayer mediaPlayer = new MediaPlayer();
                            try {
                                mediaPlayer.setDataSource("/storage/emulated/0/v"+String.valueOf(finalI+1)+".mp4");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Vholder[finalI].addCallback(new SurfaceHolder.Callback() {
                                @Override
                                public void surfaceCreated(SurfaceHolder holder) {
                                    Log.d("Create", "holder");

                                    mediaPlayer.setDisplay(Vholder[finalI]);
                                    mediaPlayer.start();
                                    Vholder[finalI].setKeepScreenOn(true);

                                }

                                @Override
                                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                                    //  mediaPlayer.start();
                                    Log.d("Changed", "holder");
                                }

                                @Override
                                public void surfaceDestroyed(SurfaceHolder holder) {
                                    Log.d("Destroyed", "holder");
                                    if (mediaPlayer != null) {
                                        mediaPlayer.stop();
                                        //释放资源
                                        mediaPlayer.release();
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
        return true;
    }
}
