package com.example.administrator.muitleconter;

import java.util.ArrayList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.xm.MyConfig.DEVICE_TYPE;
import com.xm.utils.OutputDebug;

public class RadarSearchLayout extends RelativeLayout {
	public static final String MYLOG = "RadarSearchLayout";
	private long TIME_DIFF = 1500;
	private boolean isSearching = false;
	private Bitmap monitor_bp;
	private Bitmap socket_bp;
	private int mCount;
	private OnDevClickListener mOnDevClickLs;
	private Context mContext;
	private RadarSearchView searchView;
	private float mDensity = 1;
	private ArrayList<ImageView> mImageViewList = new ArrayList<ImageView>();

	public boolean isSearching() {
		return isSearching;
	}

	public void setSearching(boolean isSearching) {
		if (searchView != null) {
			searchView.setSearching(isSearching);
			this.isSearching = isSearching;
		}
	}

	public RadarSearchLayout(Context context) {
		super(context);
		this.mContext = context;
		initBitmap();
	}

	public RadarSearchLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initBitmap();
	}

	@SuppressLint("NewApi")
	public RadarSearchLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		initBitmap();
	}

	public void initBitmap() {
		if (monitor_bp == null) {
			monitor_bp = Bitmap.createBitmap(BitmapFactory.decodeResource(
					mContext.getResources(), R.mipmap.chn_point));
		}
		if (socket_bp == null) {
			socket_bp = Bitmap.createBitmap(BitmapFactory.decodeResource(
					mContext.getResources(), R.mipmap.socket_point));
		}
		if (searchView == null) {
			searchView = new RadarSearchView(mContext);
			addView(searchView);
		}
	}

	public void setDensity(float density) {
		mDensity = density;
	}

	private OnClickListener onClickLs = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (mOnDevClickLs != null)
				mOnDevClickLs.onClick(arg0);
		}
	};

	public interface OnDevClickListener {
		void onClick(View arg0);
	}

	public void setOnDevClickListener(OnDevClickListener onDevClickLs) {
		mOnDevClickLs = onDevClickLs;
	}

	/*
	 * 显示设备图标
	 */
	public void addShowDev(String ssid, int deviceType) {
		ImageView iv = new ImageView(mContext);
		iv.setTag(ssid);
		switch (deviceType) {
		case DEVICE_TYPE.SOCKET:
			iv.setImageBitmap(socket_bp);
			break;
		default:
			iv.setImageBitmap(monitor_bp);
			break;
		}
		iv.setOnClickListener(onClickLs);
		addView(iv);
		mImageViewList.add(iv);
		mCount++;
		Random random = new Random();
		float x, y, x1, y1, x2, y2;
		int count = 0;
		x1 = getWidth() / 2;
		y1 = getHeight() / 2;
		x2 = searchView.getDefaultWidth() * 1 / 4;
		y2 = searchView.getDefaultHeight() * 1 / 4;
		OutputDebug.OutputDebugLogD(MYLOG,
				"width:" + searchView.getDefaultWidth() + " height:"
						+ searchView.getDefaultHeight());
		OutputDebug.OutputDebugLogD(MYLOG, "x1:" + x1 + " x2:" + x2 + " y1:"
				+ y1 + "y2:" + y2);
		if (x1 < 0 || x2 < 0 || y1 < 0 || y2 < 0)
			return;
		do {
			x = random.nextInt((int) (x1 + x2)) + (x1 - x2);
			y = random.nextInt((int) (y1 + y2)) + (y1 - y2);
			count++;
		} while ((x < (x1 - x2) || x > (x1 + x2) || y < (y1 - y2) || y > (y1 + y2))
				&& count < 20);
		if (count >= 20) {
			x = x1 + 5 * mCount;
			y = y1 + 5 * mCount;
			OutputDebug.OutputDebugLogD(MYLOG, "count:" + count);
		}
		OutputDebug.OutputDebugLogD(MYLOG, "x:" + x + "y:" + y);
		OutputDebug.OutputDebugLogD(MYLOG, "mDensity:" + mDensity);
		iv.layout((int) x, (int) y, (int) x + (int) (30 * mDensity), (int) y
				+ (int) (30 * mDensity));
	}

	public void removeShowDev() {
		mCount = 0;
		for (ImageView view : mImageViewList) {
			if (view != null)
				removeView(view);
		}
	}

	private void recycle() {
		if (null != monitor_bp) {
			monitor_bp.recycle();
			monitor_bp = null;
		}
		if (null != socket_bp) {
			socket_bp.recycle();
			socket_bp = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.ViewGroup#onLayout(b oolean, int, int, int, int)
	 */
	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		if (arg0 && searchView != null) {
			searchView.layout(getWidth() / 2 - getHeight() / 2, 0, getWidth()
					/ 2 + getHeight() / 2, getHeight());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.RelativeLayout#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.ViewGroup#onDetachedFromWindow()
	 */
	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		recycle();
		super.onDetachedFromWindow();
	}

}