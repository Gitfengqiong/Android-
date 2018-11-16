package com.example.administrator.muitleconter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * 给下拉框的适配器
 */

public class SceneArrayAdapter<T> extends ArrayAdapter {
    private Context mContext;
    private List<String> mStringArray;
    //构造方法
    public SceneArrayAdapter(Context context, int spinner_view, int resource, List<String> objects) {
        super(context,spinner_view,resource,objects);
        mContext =context;
        mStringArray =objects;
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
    public View getDropDownView(final int position, View convertView, ViewGroup parent) {
        //修改Spinner展开后的字体颜色
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.spinner_drop_check, parent,false);
        }

        //此处text1是Spinner默认的用来显示文字的TextView
        TextView tv = (TextView) convertView.findViewById(R.id.text1);
        tv.setText(mStringArray.get(position));
        //tv.setTextSize(22f);
      //  tv.setTextColor(Color.RED);
       // tv.setGravity(Gravity.CENTER_VERTICAL);

        CheckBox cb = (CheckBox) convertView.findViewById(R.id.ch);

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

                                          @Override
                                          public void onCheckedChanged(CompoundButton buttonView,
                                                                       boolean isChecked) {
                                              // TODO Auto-generated method stub
                                              if(isChecked){

                                                  Log.d("选中",String.valueOf(position));
                                              }else
                                              {
                                                  // btn.setEnabled(false);
                                                  // tv.setText("请勾选我");
                                                  Log.d("No","Check");
                                              }
                                          }
                                      }
        );
        return convertView;

    }

*/
}
