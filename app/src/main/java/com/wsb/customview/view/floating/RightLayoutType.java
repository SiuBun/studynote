package com.wsb.customview.view.floating;

import android.content.Context;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;
import java.util.Map;

/**
 * 右侧布局类型
 *
 * @author wsb
 * */
public class RightLayoutType extends DefaultLayoutType {
    RightLayoutType(LayoutTypeCallback typeCallback) {
        super(typeCallback);
    }

    @Override
    public boolean getTypeBoolean() {
        return true;
    }

    @Override
    public void addMenuItem(ViewGroup group, List<String> list, Map<String, ViewGroup> map) {
        for (int index = list.size() - 1; index >= 0; index--) {
            addViewByName(group, map.get(list.get(index)));
        }
    }

    @Override
    public void stuffWindowContent(ViewGroup group, ViewGroup menuContainer, ImageView logo) {
        group.addView(menuContainer);
        group.addView(logo);
    }

    @Override
    public void onConfigurationChanged() {
        getTypeCallback().layoutTypeOnConfigChanged();
    }

    @Override
    public WindowManager.LayoutParams editWindowsLayoutParams(WindowManager.LayoutParams windowLayoutParams) {
        return FloatingSupport.wrapperWindowLayoutParams(getTypeBoolean(),windowLayoutParams);
    }

    @Override
    public LinearLayout buildMenuItemContainer(Context context, int margin) {
        return FloatingSupport.buildMenuItemContainer(context,margin,getTypeBoolean());
    }
}
