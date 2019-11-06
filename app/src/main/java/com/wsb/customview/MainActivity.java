package com.wsb.customview;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.wsb.customview.fragment.CameraFragment;
import com.wsb.customview.fragment.CanvasFragment;
import com.wsb.customview.fragment.HomeWorkFragment;
import com.wsb.customview.fragment.MatrixFragment;
import com.wsb.customview.fragment.paint.PaintFragment;
import com.wsb.customview.fragment.practice.PracticeFragment;
import com.wsb.customview.fragment.practice.ScalaImageFragment;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "custom";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void paintPage(View view) {
        Log.d(TAG,"paintPage");
        getCommit(PaintFragment.newInstance());
    }

    private void getCommit(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.llt_container, fragment).addToBackStack(null).commit();
    }

    public void canvasPage(View view) {
        Log.d("custom","canvasPage");
        getCommit(CanvasFragment.newInstance());
    }

    public void cameraPage(View view) {
        Log.d("custom","cameraPage");
        getCommit(CameraFragment.newInstance());
    }

    public void matrixPage(View view) {
        Log.d("custom","matrixPage");
        getCommit(MatrixFragment.newInstance());
    }

    public void practicePage(View view) {
        Log.d("custom","matrixPage");
        getCommit(PracticeFragment.newInstance());
    }

    public void homeworkPage(View view) {
        Log.d("custom","homeworkPage");
        getCommit(HomeWorkFragment.newInstance());
    }

    public void scrollPage(View view) {
        Log.d("custom","scrollPage");
        getCommit(ScalaImageFragment.newInstance());
    }
}
