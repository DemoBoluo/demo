package com.itcast.slideqq.present.presentimp;

import com.itcast.slideqq.mode.Cheeses;
import com.itcast.slideqq.present.ipresenter.MainActivityPresent;
import com.itcast.slideqq.view.activity.MainActivity;
import com.itcast.slideqq.view.iview.MainActivityView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/3/22.
 */

public class MainActivityPresentImp implements MainActivityPresent {
    private MainActivityView mMainActivityView;
    private List<String> mData = new ArrayList<>();
    public MainActivityPresentImp(MainActivityView mainActivityView) {
        this.mMainActivityView = mainActivityView;
    }

    @Override
    public void initData() {
        mData.clear();
        if (Cheeses.NAMES.length>0) {
            for (int i = 0; i < Cheeses.NAMES.length; i++) {
                mData.add(Cheeses.NAMES[i]);
            }
            mMainActivityView.onInitData(mData);
        }
    }
}
