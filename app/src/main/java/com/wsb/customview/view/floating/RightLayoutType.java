package com.wsb.customview.view.floating;

import android.content.Context;
import android.content.res.Resources;
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
 */
public class RightLayoutType extends LeftLayoutType {
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
    public WindowManager.LayoutParams editWindowsLayoutParams(WindowManager.LayoutParams windowLayoutParams) {
        return FloatingSupport.wrapperWindowLayoutParams(getTypeBoolean(), windowLayoutParams);
    }

    @Override
    public LinearLayout buildMenuItemContainer(Context context, int margin) {
        return FloatingSupport.buildMenuItemContainer(context, margin, getTypeBoolean());
    }

    @Override
    public void hideHalfSize(WindowManager.LayoutParams layoutParams, ImageView logo) {
        LinearLayout.LayoutParams logoLayoutParams = (LinearLayout.LayoutParams) logo.getLayoutParams();

        float m = FloatingSupport.LOGO_SIZE / 3 * 2;


            // 右侧
        layoutParams.x = (int) (Resources.getSystem().getDisplayMetrics().widthPixels - FloatingSupport.LOGO_SIZE + m);
            logoLayoutParams.setMargins(0, 0, (int) -m, 0);
        logo.setLayoutParams(logoLayoutParams);


        layoutParams.alpha = 0.7f;
        getTypeCallback().layoutTypeOnParamsChanged(layoutParams);



    }
}
