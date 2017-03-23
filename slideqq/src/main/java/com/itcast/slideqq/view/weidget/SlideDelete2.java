package com.itcast.slideqq.view.weidget;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by taojin on 2017/03/22.
 */

public class SlideDelete2 extends ViewGroup {

    private View mRightView;
    private View mLeftView;
    private int mLeftWidth;
    private int mLeftHeight;
    private int mRightHeight;
    private int mRightWidth;
    private ViewDragHelper mViewDragHelper;

    public SlideDelete2(Context context) {
        this(context, null);
    }

    public SlideDelete2(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    public SlideDelete2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    public SlideDelete2(Context context, AttributeSet attrs) {
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
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (child == mLeftView) {
                    if (left > 0) {
                        left = 0;
                    } else if (left < -mRightWidth) {
                        left = -mRightWidth;
                    }
                } else if (child == mRightView) {
                    if (left < mLeftWidth - mRightWidth) {
                        left = mLeftWidth - mRightWidth;
                    } else if (left > mLeftWidth) {
                        left = mLeftWidth;
                    }
                }
                return left;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                if (changedView == mLeftView) {
                    //让rightView跟着移动
                    int newLeft = mRightView.getLeft() + dx;
                    mRightView.layout(newLeft, 0, newLeft + mRightWidth, mRightHeight);
                } else if (changedView == mRightView) {
                    //让leftView跟着移动
                    int newLeft = mLeftView.getLeft() + dx;
                    mLeftView.layout(newLeft, 0, newLeft + mLeftWidth, mLeftHeight);
                }
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                if (mLeftView.getLeft() < -mRightWidth / 2) {
                    open();
                } else {
                    close();
                }
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return mRightWidth;
            }
        });
    }

    private void close() {
        mViewDragHelper.smoothSlideViewTo(mLeftView, 0, 0);
        postInvalidateOnAnimation();
    }

    private void open() {

        /**
         * 当前控件打开了，则需要添加到SlideDeleteManager中
         */
        //SlideDeleteManager.getInstance().setSlideDelete(this);

        mViewDragHelper.smoothSlideViewTo(mLeftView, -mRightWidth, 0);
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
        /**
         * 当触摸的时候，如果有已经打开的并且不是当前触摸的对象，则关闭
         */
      /*  SlideDelete slideDelete = SlideDeleteManager.getInstance().getSlideDelete();
            if (slideDelete!=null&&slideDelete != this) {
                //关闭slideDelete对象
                slideDelete.close();
                SlideDeleteManager.getInstance().setSlideDelete(null);
            }*/

        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        if (childCount != 2) {
            throw new RuntimeException("必须有两个子控件");
        }
        mLeftView = getChildAt(0);
        mRightView = getChildAt(1);
        //测量自己的所有的子控件
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        mLeftWidth = mLeftView.getMeasuredWidth();
        mLeftHeight = mLeftView.getMeasuredHeight();

        mRightHeight = mRightView.getMeasuredHeight();
        mRightWidth = mRightView.getMeasuredWidth();

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mLeftView.layout(0, 0, mLeftWidth, mLeftHeight);
        mRightView.layout(mLeftWidth, 0, mLeftWidth + mRightWidth, mRightHeight);

    }
/*
    static class SlideDeleteManager {
        private SlideDelete mSlideDelete;

        private SlideDeleteManager() {
        }

        private static SlideDeleteManager sSlideDeleteManager;

        public synchronized static SlideDeleteManager getInstance() {
            if (sSlideDeleteManager == null) {
                synchronized (SlideDeleteManager.class) {
                    sSlideDeleteManager = new SlideDeleteManager();
                }
            }
            return sSlideDeleteManager;
        }

        public SlideDelete getSlideDelete() {
            return mSlideDelete;
        }

        public void setSlideDelete(SlideDelete slideDelete) {
            mSlideDelete = slideDelete;
        }

    }*/

}
