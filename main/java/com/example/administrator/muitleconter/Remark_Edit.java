package com.example.administrator.muitleconter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.transition.Scene;
import android.util.Log;
import android.widget.EditText;

import static com.example.administrator.muitleconter.MainActivity.SceneRemark;
import static com.example.administrator.muitleconter.MainActivity.vdate;
import static com.example.administrator.muitleconter.SetingActivtiy.FileNames;

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

}
