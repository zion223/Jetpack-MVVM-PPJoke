package com.mooc.ppjoke.ui.detail;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.Nullable;

import com.mooc.libcommon.utils.PixUtils;
import com.mooc.ppjoke.R;


public class RecordView extends View {
    // 录制时的环形进度条
    private Paint mRecordPaint;
    // 录制时点击的圆形按钮
    private Paint mBgPaint;
    // 画笔宽度
    private int mStrokeWidth;
    // 圆形按钮半径
    private int mRadius;
    //控件宽度
    private int mWidth;
    //控件高度
    private int mHeight;
    // 圆的外接圆
    private RectF mRectF;
    //progress max value
    private int mMaxValue = 100;
    //per progress value
    private int mProgressValue;
    //是否开始record
    private boolean mIsStartRecord = false;
    //Arc left、top value
    private int mArcValue;
    //录制 time
    private long mRecordTime;

    private static final int MSG_START_LONG_RECORD = 1;
    private static final int MSG_START = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_START) {
                ++mProgressValue;
                postInvalidate();
                //当没有达到最大值时一直绘制
                if (mProgressValue <= mMaxValue) {
                    mHandler.sendEmptyMessageDelayed(MSG_START, 100);
                }
            } else if (msg.what == MSG_START_LONG_RECORD) {
                //callback.onStartRecord
                Log.e("record-view", "onTouchEvent: record-start");
            }
        }
    };

    public RecordView(Context context) {
        this(context, null);
    }

    public RecordView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams(context);
    }

    //初始化画笔操作
    private void initParams(Context context) {
        mArcValue = mStrokeWidth = PixUtils.dp2px(3);

        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(context.getResources().getColor(R.color.color_white));
        mBgPaint.setStrokeWidth(mStrokeWidth);
        mBgPaint.setStyle(Paint.Style.FILL);

        mRecordPaint = new Paint();
        mRecordPaint.setAntiAlias(true);
        mRecordPaint.setColor(context.getResources().getColor(R.color.colorAccent));
        mRecordPaint.setStrokeWidth(mStrokeWidth);
        mRecordPaint.setStyle(Paint.Style.STROKE);

        mRadius = PixUtils.dp2px(30);
        mRectF = new RectF();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mWidth = getWidth();
        mHeight = getHeight();
        if (mWidth != mHeight) {
            int min = Math.min(mWidth, mHeight);
            mWidth = min;
            mHeight = min;
        }

        if (mIsStartRecord) {

            canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2, mBgPaint);

            if (mProgressValue <= mMaxValue) {
                //left--->距Y轴的距离
                //top--->距X轴的距离
                //right--->距Y轴的距离
                //bottom--->距X轴的距离
                mRectF.left = mArcValue;
                mRectF.top = mArcValue;
                mRectF.right = mWidth - mArcValue;
                mRectF.bottom = mHeight - mArcValue;
                canvas.drawArc(mRectF, -90, ((float) mProgressValue / mMaxValue) * 360, false, mRecordPaint);

                if (mProgressValue == mMaxValue) {
                    mProgressValue = 0;
                    mHandler.removeMessages(0);
                    mIsStartRecord = false;
                    //这里可以回调出去表示已到录制时间最大值
                    //code.....
                }
            }
        } else {
            canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mBgPaint);
        }
    }

    //重新该方法来完成触摸时，圆变大的效果
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsStartRecord = true;
                mRecordTime = System.currentTimeMillis();
                mHandler.sendEmptyMessage(0);
                mHandler.sendEmptyMessageDelayed(MSG_START_LONG_RECORD, ViewConfiguration.getLongPressTimeout());
                //这里可以回调出去表示已经开始录制了
                //code.....
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //这里可以回调出去表示已经取消录制了
                long endTime = System.currentTimeMillis();
                if (endTime - mRecordTime < ViewConfiguration.getLongPressTimeout()) {
                    if (event.getAction() != MotionEvent.ACTION_CANCEL) {
                        // callback.onShortRecord();
                        Log.e("record-view", "onTouchEvent: shortClick");
                    }
                } else {
                    Log.e("record-view", "onTouchEvent: record-finish");
                    //callback.onRecordFinish();
                }
                mHandler.removeMessages(MSG_START);
                mHandler.removeMessages(MSG_START_LONG_RECORD);
                mIsStartRecord = false;
                mRecordTime = 0;
                mProgressValue = 0;
                postInvalidate();
                break;
        }
        return true;
    }
}

