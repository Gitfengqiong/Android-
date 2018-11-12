package com.example.administrator.muitleconter;

import android.content.Context;
import android.graphics.Color;
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



}
