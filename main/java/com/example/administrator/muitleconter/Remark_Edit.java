package com.example.administrator.muitleconter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.transition.Scene;
import android.util.Log;
import android.widget.EditText;

import static com.example.administrator.muitleconter.MainActivity.SceneRemark;
import static com.example.administrator.muitleconter.MainActivity.config;
import static com.example.administrator.muitleconter.MainActivity.config_in;
import static com.example.administrator.muitleconter.MainActivity.config_out;
import static com.example.administrator.muitleconter.MainActivity.vdate;
import static com.example.administrator.muitleconter.SetingActivtiy.ChanngRemark_in;
import static com.example.administrator.muitleconter.SetingActivtiy.ChanngeRemark_out;
import static com.example.administrator.muitleconter.SetingActivtiy.FileNames;
import static com.example.administrator.muitleconter.SetingActivtiy.InFileNames;
import static com.example.administrator.muitleconter.SetingActivtiy.OutFileNames;

@SuppressLint("AppCompatCustomView")
public class Remark_Edit extends EditText {
    public int Buttonid;
    public Remark_Edit(Context context) {
        super(context);
    }

    protected void SaveEidtMark(){
        SceneButton scene = (SceneButton) getRootView().findViewById(Buttonid);
        if (this.getText().length()>0 && !getText().equals("null")){

            scene.Remark = getText().toString();
            scene.performClick();
            scene.performClick();
            SceneRemark[Buttonid-1000]=this.getText().toString();
            ParseCode.SaveSceneRemark(FileNames);
        }

        if (getText().equals("null")){
            scene.Remark = "";
            scene.performClick();
            scene.performClick();
            SceneRemark[Buttonid-1000]="null";
            ParseCode.SaveSceneRemark(FileNames);
        }
        this.setText("");
    }
    protected void EidtMark(){
        SceneButton scene = (SceneButton) getRootView().findViewById(Buttonid);
        if (this.getText().length()>0 && !getText().equals("null")){

            scene.Remark = getText().toString();
            scene.performClick();
            scene.performClick();
            SceneRemark[Buttonid-1000]=this.getText().toString();
            ParseCode.SaveSceneRemark(FileNames);
        }

        if (getText().equals("null")){
            scene.Remark = "";
            scene.performClick();
            scene.performClick();
            SceneRemark[Buttonid-1000]="null";
            ParseCode.SaveSceneRemark(FileNames);
        }
        this.setText("");
    }

    protected  void ChanngeMark_in(int IdBase ){
        Mybutton scene = (Mybutton) getRootView().findViewById(Buttonid);
        if (this.getText().length()>0 && !getText().equals("null")){

            scene.Remark = getText().toString();
            scene.performClick();
            scene.performClick();
            ChanngRemark_in[Buttonid-IdBase]=this.getText().toString();
            ParseCode.SaveChanngeRemark(InFileNames,ChanngRemark_in,config_in,vdate.inleng);
        }

        if (getText().equals("null")){
            scene.Remark = "";
            scene.performClick();
            scene.performClick();
            ChanngRemark_in[Buttonid-IdBase]="null";
            ParseCode.SaveChanngeRemark(InFileNames,ChanngRemark_in,config_in,vdate.inleng);
        }
        this.setText("");
    }

    protected void ChanngeSaveMark_in(int IdBase){
        Mybutton scene = (Mybutton) getRootView().findViewById(Buttonid);
        if (this.getText().length()>0 && !getText().equals("null")){
            scene.Remark = getText().toString();
            scene.performClick();
            scene.performClick();
            ChanngRemark_in[Buttonid-IdBase]=this.getText().toString();
            ParseCode.SaveChanngeRemark(InFileNames,ChanngRemark_in,config_in,vdate.inleng);
        }

        if (getText().equals("null")){
            scene.Remark = "";
            scene.performClick();
            scene.performClick();
            ChanngRemark_in[Buttonid-IdBase]="null";
            ParseCode.SaveChanngeRemark(InFileNames,ChanngRemark_in,config_in,vdate.inleng);
        }
        this.setText("");
    }
    protected  void ChanngeMark_out(int IdBase ){
        Mybutton scene = (Mybutton) getRootView().findViewById(Buttonid);
        if (this.getText().length()>0 && !getText().equals("null")){

            scene.Remark = getText().toString();
            scene.performClick();
            scene.performClick();
            ChanngeRemark_out[Buttonid-IdBase]=this.getText().toString();
            ParseCode.SaveChanngeRemark(OutFileNames,ChanngeRemark_out,config_out,vdate.outleng);
        }

        if (getText().equals("null")){
            scene.Remark = "";
            scene.performClick();
            scene.performClick();
            ChanngeRemark_out[Buttonid-IdBase]="null";
            ParseCode.SaveChanngeRemark(OutFileNames,ChanngeRemark_out,config_out,vdate.outleng);
        }
        this.setText("");
    }

    protected void ChanngeSaveMark_out(int IdBase){
        Mybutton scene = (Mybutton) getRootView().findViewById(Buttonid);
        if (this.getText().length()>0 && !getText().equals("null")){

            scene.Remark = getText().toString();
            scene.performClick();
            scene.performClick();
            ChanngeRemark_out[Buttonid-IdBase]=this.getText().toString();
            ParseCode.SaveChanngeRemark(OutFileNames,ChanngeRemark_out,config_out,vdate.outleng);
        }

        if (getText().equals("null")){
            scene.Remark = "";
            scene.performClick();
            scene.performClick();
            ChanngeRemark_out[Buttonid-IdBase]="null";
            ParseCode.SaveChanngeRemark(OutFileNames,ChanngeRemark_out,config_out,vdate.outleng);
        }
        this.setText("");
    }


}
