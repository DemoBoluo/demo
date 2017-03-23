package com.itcast.slideqq.view.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.itcast.slideqq.R;
import com.itcast.slideqq.adapter.MainAdaper;
import com.itcast.slideqq.mode.Cheeses;
import com.itcast.slideqq.present.ipresenter.MainActivityPresent;
import com.itcast.slideqq.present.presentimp.MainActivityPresentImp;
import com.itcast.slideqq.view.iview.MainActivityView;
import com.itcast.slideqq.view.weidget.SlideMenu;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainActivityView {


    @BindView(R.id.menu_iv)
    ImageView mMenuIv;
    @BindView(R.id.menu_lv)
    ListView mMenuLv;
    @BindView(R.id.main_iv)
    ImageView mMainIv;
    @BindView(R.id.main_lv)
    ListView mMainLv;
    @BindView(R.id.silde_main)
    SlideMenu mSildeMain;
    private MainActivityPresent mMainActivityPresentImp;
    private MainAdaper mMainAdaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainActivityPresentImp = new MainActivityPresentImp(this);
        initView();

    }

    private void initView() {
        mMainIv = (ImageView) findViewById(R.id.main_iv);
        mMenuIv = (ImageView) findViewById(R.id.menu_iv);
        mMainLv = (ListView) findViewById(R.id.main_lv);
        mMenuLv = (ListView) findViewById(R.id.menu_lv);
        mSildeMain = (SlideMenu)findViewById(R.id.silde_main);
        mMainActivityPresentImp.initData();
        mSildeMain.setOnChangingListener(new SlideMenu.OnChangingListener() {
            @Override
            public void getPrecent(float percent) {
                if (percent == 0) {
                    ObjectAnimator.ofFloat(mMainIv, "alpha", 1).setDuration(20).start();
                    ObjectAnimator.ofFloat(mMenuIv, "alpha", 1).setDuration(20).start();
                    ObjectAnimator animator = ObjectAnimator.ofFloat(mMainIv, "translationX",0,20);
                    animator.setInterpolator(new CycleInterpolator(3));
                    animator.setDuration(500);
                    animator.start();
                } else if (percent <= 1) {
                    //透明度
                    float alpha = 1 - percent;
                    ObjectAnimator.ofFloat(mMainIv, "alpha", alpha).setDuration(200).start();
                    ObjectAnimator.ofFloat(mMenuIv, "alpha", percent).setDuration(200).start();
                }
            }
        });
        mMainIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSildeMain.openLeftMenu();
            }
        });
    }

    @Override
    public void onInitData(List<String> data) {
        if (mMainAdaper == null) {
            mMainAdaper = new MainAdaper(data);
            mMainLv.setAdapter(mMainAdaper);
        }
        mMenuLv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Cheeses.sCheeseStrings) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (view instanceof TextView) {
                    TextView tv = (TextView) view;
                    tv.setTextColor(Color.WHITE);
                }
                return view;
            }
        });
    }

}
