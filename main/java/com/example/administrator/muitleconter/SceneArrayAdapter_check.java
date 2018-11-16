package com.example.administrator.muitleconter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 给下拉框的适配器
 */

public class SceneArrayAdapter_check<T> extends ArrayAdapter {
    private Context mContext;
    private List<String> mStringArray;
    private Model data[];
    private Map<Integer,Boolean> map=new HashMap<>();
    //构造方法
    public SceneArrayAdapter_check(Context context, int spinner_view, int resource, List<String> objects ,Model[] models) {
        super(context,spinner_view,resource,objects);
        mContext =context;
        mStringArray =objects;
        data = models ;
    }
    //复写这个方法，使返回的数据没有最后一项
    @Override
    public int getCount() {
        // don't display last item. It is used as hint.
        int count = super.getCount();
        return count > 0 ? count - 1 : count;
    }


    @Override
    public View  getDropDownView(final int position, View convertView, ViewGroup parent) {
        //修改Spinner展开后的字体颜色
        final MyViewHolder holder ;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.spinner_drop_check, parent,false);
            holder =new MyViewHolder();
            holder.mTextView= (TextView) convertView.findViewById(R.id.text1);
            holder.mCheckBox= (CheckBox) convertView.findViewById(R.id.ch);
            convertView.setTag(holder);
        }else {
            holder = (MyViewHolder) convertView.getTag();
        }


        //此处text1是Spinner默认的用来显示文字的TextView

        holder.mTextView.setText(mStringArray.get(position));
        //tv.setTextSize(22f);
        //  tv.setTextColor(Color.RED);
        // tv.setGravity(Gravity.CENTER_VERTICAL);

       // final CheckBox cb = (CheckBox) convertView.findViewById(R.id.ch);
       // cb.setChecked(data[position].ischeck());
      //  Log.d("poist",String.valueOf(position));
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

                                          @Override
                                          public void onCheckedChanged(CompoundButton buttonView,
                                                                       boolean isChecked) {
                                              // TODO Auto-generated method stub
                                              if(isChecked){
                                                  map.put(position,true);
                                               //   Log.d("选中",String.valueOf(position));
                                                  data[position].setIscheck(true);
                                              }else
                                              {
                                                  // btn.setEnabled(false);
                                                  // tv.setText("请勾选我");
                                                  map.remove(position);
                                                 // Log.d("No","Check"+String.valueOf(position));
                                                  data[position].setIscheck(false);
                                              }
                                          }
                                      }

        );

        if(map!=null&&map.containsKey(position)){
            holder.mCheckBox.setChecked(true);
        }else {
            holder.mCheckBox.setChecked(false);
        }


        return convertView;

    }

    public static class  MyViewHolder {
        TextView mTextView;
        CheckBox mCheckBox;
    }
}
