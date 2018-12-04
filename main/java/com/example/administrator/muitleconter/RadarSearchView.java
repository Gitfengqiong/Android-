package com.example.administrator.muitleconter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;



public class RadarSearchView extends View {

	public static final String MYLOG = "SearchDevicesView";
	@SuppressWarnings("unused")
	private long TIME_DIFF = 1500;
	int[] lineColor = new int[] { 0x7B, 0x7B, 0x7B };
	int[] innerCircle0 = new int[] { 0xb9, 0xff, 0xFF };
	int[] innerCircle1 = new int[] { 0xdf, 0xff, 0xFF };
	int[] innerCircle2 = new int[] { 0xec, 0xff, 0xFF };
	int[] argColor = new int[] { 0xF3, 0xf3, 0xfa };
	private float offsetArgs = 0;
	private boolean isSearching = false;
	private Bitmap bitmap;
	private Bitmap bitmap1;
	private Bitmap bitmap2;
	private Context mContext;
	// 控件默认长�?�宽
	private int defaultWidth = 0;
	private int defaultHeight = 0;

	public boolean isSearching() {
		return isSearching;
	}

	public void setSearching(boolean isSearching) {
		this.isSearching = isSearching;
		offsetArgs = 0;
		invalidate();
	}

	public RadarSearchView(Context context) {
		super(context);
		this.mContext = context;
		invalidate();
		initBitmap();
	}

	public RadarSearchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initBitmap();
	}

	@SuppressLint("NewApi")
	public RadarSearchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		initBitmap();
	}

	private void initBitmap() {
		if (bitmap == null) {
			bitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(
					mContext.getResources(), R.mipmap.gplus_search_bg));
		}
		if (bitmap1 == null) {
			bitmap1 = Bitmap.createBitmap(BitmapFactory.decodeResource(
					mContext.getResources(), R.mipmap.connect_bg));
		}
		if (bitmap2 == null) {
			bitmap2 = Bitmap.createBitmap(BitmapFactory.decodeResource(
					mContext.getResources(), R.mipmap.gplus_search_args));
		}
		defaultWidth = bitmap.getWidth();
		defaultHeight = bitmap.getHeight();
	}

	public int getDefaultWidth() {
		return defaultWidth;
	}

	public int getDefaultHeight() {
		return defaultHeight;
	}

	private void recycle() {
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
		if (bitmap1 != null) {
			bitmap1.recycle();
			bitmap1 = null;
		}
		if (bitmap2 != null) {
			bitmap2.recycle();
			bitmap2 = null;
		}
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint p = new Paint();
		p.setColor(Color.RED);// 设置红色
		p.setStyle(Paint.Style.FILL);
		p.setAntiAlias(true);// 设置画笔的锯齿效果�?? true是去除，大家�?看效果就明白�?
		canvas.drawBitmap(bitmap, getWidth() / 2 - bitmap.getWidth() / 2,
				getHeight() / 2 - bitmap.getHeight() / 2, null);
		if (isSearching) {
			Rect rMoon = new Rect(getWidth() / 2, getHeight() / 2,
					getWidth() / 2, getHeight() / 2);
			canvas.rotate(offsetArgs, getWidth() / 2, getHeight() / 2);
			canvas.drawBitmap(bitmap2, null, rMoon, null);
			offsetArgs = offsetArgs + 3;
		}
		canvas.drawBitmap(bitmap2, getWidth() / 2 - bitmap2.getWidth() / 2,
				getHeight() / 2 - bitmap2.getHeight() / 2, null);
		// drawCircleBorder(canvas,defaultWidth / 2,Color.RED);
		if (isSearching)
			invalidate();
	}

	/**
	 * 边缘画圆
	 */
	private void drawCircleBorder(Canvas canvas, int radius, int color) {
		Paint paint = new Paint();
		/* 去锯�? */
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		paint.setColor(color);
		/* 设置paint的�??style�?为STROKE：空�? */
		paint.setStyle(Paint.Style.STROKE);
		/* 设置paint的外框宽�? */
		paint.setStrokeWidth(8);
		canvas.drawCircle(defaultWidth / 2, defaultHeight / 2, radius, paint);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onDetachedFromWindow()
	 */
	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		recycle();
		super.onDetachedFromWindow();
	}

}