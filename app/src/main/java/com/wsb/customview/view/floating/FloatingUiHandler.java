package com.wsb.customview.view.floating;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * 悬浮窗ui更新处理对象
 * */
class FloatingUiHandler extends Handler {
    /**
     * 悬浮按钮半隐藏
     */
    public static final int HANDLER_TYPE_HALF_HIDE = 2;

    FloatingUiCallback mFloatingUiCallback;

    FloatingUiHandler(FloatingUiCallback floatingUiCallback) {
        super(Looper.getMainLooper());
        mFloatingUiCallback = floatingUiCallback;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case HANDLER_TYPE_HALF_HIDE:
                mFloatingUiCallback.shrinkMenu();
                mFloatingUiCallback.hideHalfSize();
                break;
            default:
                break;
        }
    }
}