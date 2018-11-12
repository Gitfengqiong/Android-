package com.example.administrator.muitleconter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * 给下拉框的适配器
 */

public class SceneArrayAdapter<T> extends ArrayAdapter {
    private Context mContext;
    private String [] mStringArray;
    //构造方法
    public SceneArrayAdapter(Context context, int spinner_view, int resource, List<T> objects) {
        super(context,spinner_view,resource,objects);
        mContext =context;
    }
    //复写这个方法，使返回的数据没有最后一项
    @Override
    public int getCount() {
        // don't display last item. It is used as hint.
        int count = super.getCount();
        return count > 0 ? count - 1 : count;
    }
    /*
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        //修改Spinner展开后的字体颜色
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent,false);
        }

        //此处text1是Spinner默认的用来显示文字的TextView
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(mStringArray[position]);
        tv.setTextSize(22f);
        tv.setTextColor(Color.RED);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        return convertView;

    }

*/
}
