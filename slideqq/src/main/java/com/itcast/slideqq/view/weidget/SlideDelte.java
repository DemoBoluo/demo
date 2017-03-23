package com.itcast.slideqq.view.weidget;

import android.content.Context;
import android.database.CursorJoiner;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dell on 2017/3/22.
 */

public class SlideDelte extends ViewGroup {
    private View itemMain;
    private View itemRight;
    private int mItemMainMeasuredWidth;
    private int mItemMainMeasuredHeight;
    private int mItemRightMeasuredWidth;
    private int mItemRightMeasuredHeight;
    private ViewDragHelper mViewDragHelper;

    public SlideDelte(Context context) {
        this(context, null);
    }


    public SlideDelte(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    public SlideDelte(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    public SlideDelte(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mViewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {

            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return pointerId == 0;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                if (changedView == itemMain) {
                    int rightLeft = itemRight.getLeft() + dx;
                    itemRight.layout(rightLeft, 0, rightLeft+mItemRightMeasuredWidth, mItemMainMeasuredHeight);
                } else if (changedView == itemRight) {
                    int mainLeft = itemMain.getLeft() + dx;
                    itemMain.layout(mainLeft, 0, mItemMainMeasuredWidth + mainLeft, mItemMainMeasuredHeight);
                }

            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                if (itemMain.getLeft() < (-mItemRightMeasuredWidth / 2)) {
                    itemMainOpen();
                } else {
                    itemMainClose();
                }
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return mItemRightMeasuredWidth;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (itemMain == child) {
                    if (left < -mItemRightMeasuredWidth) {
                        left = -mItemRightMeasuredWidth;
                    } else if (left >0) {
                        left = 0;
                    }
                } else if (itemRight == child) {
                    if (left < (mItemMainMeasuredWidth - mItemRightMeasuredWidth)) {
                        left = mItemMainMeasuredWidth - mItemRightMeasuredWidth;
                    } else if (left >= mItemMainMeasuredWidth) {
                        left =  mItemMainMeasuredWidth;
                    }
                }
                return left;
            }
        });
    }

    private void itemMainClose() {
       mViewDragHelper.smoothSlideViewTo(itemMain,0,0);
        postInvalidateOnAnimation();
    }

    private void itemMainOpen() {
      mViewDragHelper.smoothSlideViewTo(itemMain,-mItemRightMeasuredWidth,0);
        postInvalidateOnAnimation();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mViewDragHelper.continueSettling(true)) {
            postInvalidateOnAnimation();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (itemMain.getLeft()<0){
            //MainView move to left,request deal touchevent
            requestDisallowInterceptTouchEvent(true);
        }
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        itemMain = getChildAt(0);
        itemRight = getChildAt(1);
        mItemMainMeasuredWidth = itemMain.getMeasuredWidth();
        mItemMainMeasuredHeight = itemMain.getMeasuredHeight();
        mItemRightMeasuredWidth = itemRight.getMeasuredWidth();
        mItemRightMeasuredHeight = itemRight.getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        itemMain.layout(0,0,mItemMainMeasuredWidth,mItemMainMeasuredHeight);
        itemRight.layout(mItemMainMeasuredWidth,0,mItemMainMeasuredWidth+mItemRightMeasuredWidth,mItemRightMeasuredHeight);
    }
}
