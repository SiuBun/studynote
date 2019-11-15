package com.wsb.customview.view.floating;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;
import java.util.Map;

/**
 * 悬浮窗默认布局类型,左
 *
 * @author wsb
 * */
public class DefaultLayoutType implements LayoutType{
    private LayoutTypeCallback mTypeCallback;

    DefaultLayoutType(LayoutTypeCallback typeCallback) {
        mTypeCallback = typeCallback;
    }

    @Override
    public boolean getTypeBoolean() {
        return false;
    }

    @Override
    public void addMenuItem(ViewGroup group, List<String> list, Map<String, ViewGroup> map) {
        for (int index = 0; index < list.size(); index++) {
            addViewByName(group, map.get(list.get(index)));
        }
    }

    @Override
    public void stuffWindowContent(ViewGroup group, ViewGroup menuContainer, ImageView logo) {
        group.addView(logo);
        group.addView(menuContainer);
    }

    @Override
    public void onConfigurationChanged() {
    }

    @Override
    public WindowManager.LayoutParams editWindowsLayoutParams(WindowManager.LayoutParams windowLayoutParams) {
        return FloatingSupport.wrapperWindowLayoutParams(getTypeBoolean(),windowLayoutParams);
    }

    @Override
    public LinearLayout buildMenuItemContainer(Context context, int margin) {
        return FloatingSupport.buildMenuItemContainer(context,margin,getTypeBoolean());
    }

    void addViewByName(ViewGroup group, View itemView) {
        if (null != itemView) {
            group.addView(itemView);
        }
    }

    LayoutTypeCallback getTypeCallback() {
        return mTypeCallback;
    }
}
