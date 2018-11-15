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
import static com.example.administrator.muitleconter.SetingActivtiy.Msid1;

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
            //    Log.d("3:","选中输出，等待输入");
          //  this.setBackgroundColor(Color.parseColor("#11ff11"));
            this.setBackground(getResources().getDrawable(R.drawable.button_shap_on));
            this.setTextColor(Color.parseColor("#22ee33"));
            return  0 ;

        }
        else if (Foucse&&!vdata.SceneStart){
            Foucse =false;
            Onstatu = false;
           // this.setBackgroundColor(Color.parseColor("#ff1111"));
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



}
