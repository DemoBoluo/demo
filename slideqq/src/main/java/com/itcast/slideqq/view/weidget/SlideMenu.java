package com.itcast.slideqq.view.weidget;

import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.widget.ViewDragHelper;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

/**
 * Created by dell on 2017/3/22.
 */

public class SlideMenu extends FrameLayout {

    private View mLeftMenu;
    private View mMainView;
    private int mLeftMenuMeasuredHeight;
    private int mMLeftMenuMeasuredWidth;
    private int mMMainViewMeasuredHeight;
    private int mMMainViewMeasuredWidth;
    private FloatEvaluator mFloatEvaluator;
    private ArgbEvaluator mArgbEvaluator;
    private ViewDragHelper mViewDragHelper;
    private float mMaxRange;
    private int mStartX;
    private int mStartY;

    public SlideMenu(@NonNull Context context) {
        this(context, null);
    }

    public SlideMenu(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        this(context, attrs);
    }

    public SlideMenu(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    public SlideMenu(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mFloatEvaluator = new FloatEvaluator();
        mArgbEvaluator = new ArgbEvaluator();
        mViewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            /**
             * 用于判断当前选中的childVie是否能够被拖拽
             *
             * @param child
             * @param pointerId
             * @return
             */
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return pointerId == 0;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (child == mMainView) {
                    if (left < 0) {
                        left = 0;
                    } else if (left >= mMaxRange) {
                        left = (int) mMaxRange;
                    }
                }
                return left;
            }

            /**
             * @param changedView
             * @param left
             * @param top
             * @param dx
             * @param dy
             */
            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                if (changedView == mMainView) {
                    int newLeft = mMainView.getLeft() + dx;
                    if (newLeft >= mMaxRange) {
                        newLeft = (int) mMaxRange;
                    } else if (newLeft <= 0) {
                        newLeft = 0;
                    }
                    mMainView.layout(newLeft, 0, newLeft + mMMainViewMeasuredWidth, mMMainViewMeasuredHeight);
                }
                mLeftMenu.layout(0, 0, mMLeftMenuMeasuredWidth, mLeftMenuMeasuredHeight);
                //添加动画
                //计算移动的比率
                float percent = (mMainView.getLeft() + 0.0f) / mMaxRange;
                showAnimation(percent);
                // TODO: 2017/3/22
                //接口回调
                if (mOnChangingListener != null) {
                    mOnChangingListener.getPrecent(percent);
                }

            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                if (releasedChild == mMainView) {
                    if (mMainView.getLeft() >= mMaxRange / 2) {
                        openLeftMenu();
                    } else if (mMainView.getLeft() < mMaxRange / 2) {
                        closeLeftMenu();
                    } else if (xvel > 1000) {
                        openLeftMenu();
                    } else if (xvel <= 1000) {
                        closeLeftMenu();
                    }
                }
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return (int) mMaxRange;
            }
        });


    }

    public void closeLeftMenu() {
        mViewDragHelper.smoothSlideViewTo(mMainView, 0, 0);
        //动画
        postInvalidateOnAnimation();
    }

    public void openLeftMenu() {
        mViewDragHelper.smoothSlideViewTo(mMainView, (int) mMaxRange, 0);
        //动画
        postInvalidateOnAnimation();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mViewDragHelper.continueSettling(true)) {
            postInvalidateOnAnimation();
        }
    }

    private void showAnimation(float percent) {
        //1、mMainView:缩放[1.0,0.75],（利用接口）头像透明度,抖动
        //2、mLeftMenuView：移动[-mLeftMenuView/2,0]
        //3、透明度[0,1]
        //4、背景由黑变透明了（利用接口）
        //5、缩放[0.75,1.0]
        Float evaluateMain = mFloatEvaluator.evaluate(percent, 1.0, 0.75);
        mMainView.setScaleX(evaluateMain);
        mMainView.setScaleY(evaluateMain);
        Float evaluateLeftMenu = mFloatEvaluator.evaluate(percent, 0.75, 1.0);
        Float evaluateTranslationX = mFloatEvaluator.evaluate(percent, -mMLeftMenuMeasuredWidth / 2, 0);
        mLeftMenu.setScaleY(evaluateLeftMenu);
        mLeftMenu.setScaleX(evaluateLeftMenu);
        mLeftMenu.setTranslationX(evaluateTranslationX);
        mLeftMenu.setAlpha(percent);
        int evaluateColor = (int) mArgbEvaluator.evaluate(percent, Color.BLACK, Color.TRANSPARENT);
        getBackground().setColorFilter(evaluateColor, PorterDuff.Mode.SRC_OVER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() != 2) {
            throw new RuntimeException("必须只能有两个儿子");
        }
        mLeftMenu = getChildAt(0);
        mMainView = getChildAt(1);
        mLeftMenuMeasuredHeight = mLeftMenu.getMeasuredHeight();
        mMLeftMenuMeasuredWidth = mLeftMenu.getMeasuredWidth();
        mMMainViewMeasuredHeight = mMainView.getMeasuredHeight();
        mMMainViewMeasuredWidth = mMainView.getMeasuredWidth();
        mMaxRange = mMMainViewMeasuredWidth * 0.65f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //完全打开MainView不让子条目移动
        if (mMainView.getLeft() == mMaxRange) {
            float x = ev.getX();
            if (x >= mMaxRange) {
                return true;
            }
        } else if (mMainView.getLeft() == 0) {
            //左移要求父控件不拦截事件
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mStartX = (int) ev.getX();
                    mStartY = (int) ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int moveX = (int) ev.getX();
                    int moveY = (int) ev.getY();
                    if (moveX < mStartX) {
                            return false;
                    }
            }
        }
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    public interface OnChangingListener {
        void getPrecent(float percent);
    }

    private OnChangingListener mOnChangingListener;

    public void setOnChangingListener(OnChangingListener onChangingListener) {
        mOnChangingListener = onChangingListener;
    }
}
