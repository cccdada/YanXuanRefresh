package com.sheng.yanxuanrefresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.sheng.yanxuanrefresh.refresh.ViewStatus;
import com.sheng.yanxuanrefresh.refresh.YanXuanRefreshView;

/**
 * Created by wangsheng on 17/8/17.
 */

public class YXRefreshHeader extends LinearLayout implements RefreshHeader{

    private YanXuanRefreshView yanXuanRefreshView;

    public YXRefreshHeader(Context context) {
        super(context);
    }

    public YXRefreshHeader(Context context, @Nullable AttributeSet attrs) {
        super(context,attrs);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.yx_refresh_header,this);
        yanXuanRefreshView = (YanXuanRefreshView) view.findViewById(R.id.refreshView);
    }

    public YXRefreshHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onPullingDown(float percent, int offset, int headerHeight, int extendHeight) {
        yanXuanRefreshView.setDistance(offset);
        Log.d("下拉距离",offset+"");
    }

    @Override
    public void onReleasing(float percent, int offset, int headerHeight, int extendHeight) {

    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public void onStartAnimator(RefreshLayout layout, int height, int extendHeight) {

    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        return 0;
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState){
            case Refreshing:
                yanXuanRefreshView.setViewStatus(ViewStatus.REFRESHING);
                break;
            case None:
                if (oldState== RefreshState.RefreshFinish) {
                    yanXuanRefreshView.restoreView();
                }
                break;
        }
    }
}
