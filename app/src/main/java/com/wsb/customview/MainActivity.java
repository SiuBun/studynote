package com.wsb.customview;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.wsb.customview.fragment.CameraFragment;
import com.wsb.customview.fragment.CanvasFragment;
import com.wsb.customview.fragment.MatrixFragment;
import com.wsb.customview.fragment.MultiTouchFragment;
import com.wsb.customview.fragment.paint.PaintFragment;
import com.wsb.customview.fragment.practice.PracticeFragment;
import com.wsb.customview.fragment.practice.ScalaImageFragment;
import com.wsb.customview.shareelement.ShareElementActivity;
import com.wsb.customview.utils.LogUtils;
import com.wsb.customview.view.instantfloating.data.FloatingMenuItems;
import com.wsb.customview.view.instantfloating.strategy.LayoutType;
import com.wsb.customview.view.instantfloating.widget.InstantFloatingWindow;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "custom";
    private InstantFloatingWindow mWindow;
    SparseArray<FloatingMenuItems> sparseArray = new SparseArray<>();
    private boolean showing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> menuNameList = new ArrayList<>();
        int[] icons = new int[]{R.drawable.floating_account, R.drawable.floating_msg, R.drawable.floating_hide};
        menuNameList.add("account");
        menuNameList.add("msg");
        menuNameList.add("hide");
        for (int i = 0; i < menuNameList.size(); i++) {
            sparseArray.append(i, new FloatingMenuItems(menuNameList.get(i), icons[i]));
        }


        /*TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(
                        () -> FloatButton
                                .attach(MainActivity.this, code -> {
                                })
                                .setMsgVisible(true)
                                .setCommunityVisible(true)
                                .setCustomerVisible(false)
                                .setAnnouncementVisible(false)
                                .show()
                );
            }
        };
        new Timer().schedule(timerTask, 1000);*/
    }

    public void paintPage(View view) {
        Log.d(TAG, "paintPage");
        getCommit(PaintFragment.newInstance());
    }

    private void getCommit(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.llt_container, fragment).addToBackStack(null).commit();
    }

    public void canvasPage(View view) {
        Log.d("custom", "canvasPage");
        getCommit(CanvasFragment.newInstance());
    }

    public void cameraPage(View view) {
        Log.d("custom", "cameraPage");
        getCommit(CameraFragment.newInstance());
    }

    public void matrixPage(View view) {
        Log.d("custom", "matrixPage");
        getCommit(MatrixFragment.newInstance());
    }

    public void practicePage(View view) {
        Log.d("custom", "matrixPage");
        getCommit(PracticeFragment.newInstance());
    }

    public void multitouch(View view) {
        Log.d("custom", "multitouch");
        getCommit(MultiTouchFragment.newInstance());
    }

    public void scrollPage(View view) {
        Log.d("custom", "scrollPage");
        getCommit(ScalaImageFragment.newInstance());
    }

    public void floatingShow(View view) {
        Log.d("custom", "floatingShow:" + showing);


        if (mWindow == null) {
            mWindow = InstantFloatingWindow
                    .with(MainActivity.this)
                    .setLogo(R.drawable.floating_logo)
                    .setLayoutType(LayoutType.LEFT)
                    .setMenuItems(sparseArray)
                    .setMenuItemsClickListener((position, title) -> LogUtils.d("点击了菜单列表中第" + position + "个菜单项,标题为" + title))
                    .build();
        }

        if (showing) {
            mWindow.hide();
        } else {
            mWindow.show();
        }
        showing = !showing;
    }

    @Override
    protected void onDestroy() {
        mWindow.onDestroy();
        super.onDestroy();
    }

    public void navigate(View view) {
        startActivity(new Intent(this, ShareElementActivity.class));
    }

    public void motion(View view) {
        startActivity(new Intent(this, ShareElementActivity.class));
    }
}
