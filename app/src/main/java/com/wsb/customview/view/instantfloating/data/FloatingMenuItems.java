package com.wsb.customview.view.instantfloating.data;

/**
 * 悬浮窗菜单实体
 *
 * @author wsb
 */
public class FloatingMenuItems {
    private String mTitleText;
    private int mTitleColor;
    private int mIcon;
    private int mBgColor;
    private String dotNum;

    public FloatingMenuItems(String mTitleText, int mIcon) {
        this.mTitleText = mTitleText;
        this.mIcon = mIcon;
    }

    public String getTitleText() {
        return mTitleText;
    }

    public void setTitleText(String mTitleText) {
        this.mTitleText = mTitleText;
    }

    public int getTitleColor() {
        return mTitleColor;
    }

    public void setTitleColor(int mTitleColor) {
        this.mTitleColor = mTitleColor;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int mIcon) {
        this.mIcon = mIcon;
    }

    public int getBgColor() {
        return mBgColor;
    }

    public void setBgColor(int mBgColor) {
        this.mBgColor = mBgColor;
    }

    public String getDotNum() {
        return dotNum;
    }

    public void setDotNum(String dotNum) {
        this.dotNum = dotNum;
    }
}
