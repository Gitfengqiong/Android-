package com.example.administrator.muitleconter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.widget.Toast;

import static com.example.administrator.muitleconter.SetingActivtiy.Internets;
import static com.example.administrator.muitleconter.SetingActivtiy.MReview;
import static com.example.administrator.muitleconter.SetingActivtiy.Msid1;
import static com.example.administrator.muitleconter.SetingActivtiy.Mstatue;
import static com.example.administrator.muitleconter.SetingActivtiy.Statue;

public class SceneButton extends AppCompatButton {

    public SceneButton(Context context) {
        super(context);
    }
    public boolean Onstatu =false ;
    public int id ;
    private boolean Foucse =false;
    public  String Remark = "";
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public int Clickstart(VData vdata ){
        //Log.d("this",this.statu+"Number"+this.getText().toString());

        if (vdata.SceneStart){
            Toast.makeText(getContext(),"请关闭场景循环...",Toast.LENGTH_LONG).show();
        }
        if (!Foucse&&!vdata.SceneStart){
            //选中当前场景
            Foucse = true ;
            Onstatu =true;
            if (vdata.IsScene >0 ){
                SceneButton Scen = (SceneButton) getRootView().findViewById(vdata.IsScene+999);
                Scen.Foucse = false ;
                Scen.setBackground(getResources().getDrawable(R.drawable.button_shap2));
                Scen.setTextColor(Color.parseColor("#ee2233"));
            }
            vdata.IsScene = id ;
            this.setBackground(getResources().getDrawable(R.drawable.button_shap_on));
            this.setTextColor(Color.parseColor("#22ee33"));
            StringBuffer codes ;
            String leng = "03";
            codes=new StringBuffer();
            codes.append(getResources().getString(R.string.Ncode_Begin));
            codes.append(vdata.devaddrid);
            codes.append(getResources().getString(R.string.Ncode_Order_SceneGet));
            codes.append(leng);
            codes.append("00");
            if (id <16){
                String hexnum = Integer.toString(id,16);
                codes.append("0");
                codes.append(hexnum);
            }else if ( id >15){
                String hexnum = Integer.toString(id,16);
                codes.append(hexnum);
            }
            codes.append("00");
           // Log.d("Call Scence:",codes.toString());
            SendCodes(codes.toString());//Call Scene
            synchronized (this){
                try {
                    wait(200);
                    SendRefresh();
                    wait(500);
                    SendStatue();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return  0 ;

        }
        else if (Foucse&&!vdata.SceneStart){
            Foucse =false;
            Onstatu = false;
            vdata.IsScene = 0;
            this.setBackground(getResources().getDrawable(R.drawable.button_shap2));
            this.setTextColor(Color.parseColor("#ee2233"));
            return  0 ;
        }
        return 1;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制第一行文字
        Paint paint = new Paint();
        paint.setTextSize(32);
        paint.setColor(Color.rgb(0x33,0xff,0x22));
        float tagWidth = paint.measureText(String.valueOf(Remark));
        int x = (int) (this.getWidth() - tagWidth)/2;
        int y = this.getHeight()/2;
        canvas.drawText(String.valueOf(Remark), x, y, paint);
        //绘制第二行文字
       // Paint paint1 = new Paint();
       // paint1.setTextSize(28);
       // paint1.setColor(Color.rgb(0x00,0xff,0x00));
       // float numWidth = paint.measureText(Remark + "");
       // int x1 = (int) (this.getWidth() - numWidth)/2;
       // int y1 = this.getHeight()/2 + 35;
       // canvas.drawText(Remark+"", x1, y1, paint1);
//        canvas.translate(0,(this.getMeasuredHeight()/2) - (int) this.getTextSize());
    }


    private  void SendStatue(){
        Message msg = new Message();
        msg.what = Mstatue;
        Statue.sendMessage(msg);
    }

    private void SendRefresh(){
        Message msg = new Message();
        msg.what = MReview;
        Statue.sendMessage(msg);
    }

    public  void SendCodes(String codes){

        try {
            Message msg = new Message();
            msg.what = Msid1;
            msg.obj = codes;
            assert (Internets.rehandler != null);

            Internets.rehandler.sendMessage(msg);
        }catch (Exception e){e.printStackTrace();}

    }


}
