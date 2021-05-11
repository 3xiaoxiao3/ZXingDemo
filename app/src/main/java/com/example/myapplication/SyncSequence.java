package com.example.myapplication;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.king.zxing.CaptureActivity;
import com.king.zxing.camera.CameraConfigurationUtils;

public class SyncSequence extends CaptureActivity {
    private ImageView icImg;
    private boolean isContinuousScan;
    @Override
    public int getLayoutId() {
        return R.layout.activity_snyc_sequence;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        icImg = findViewById(R.id.ivTorch);
        isContinuousScan = getIntent().getBooleanExtra("key_continuous_scan",false);

        if (!hasTorch()){
            icImg.setVisibility(View.GONE);
        }

        getCaptureHelper().playBeep(false)//播放音效
                .vibrate(true)//震动
                .supportVerticalCode(true)//是否支持扫垂直条码
//                .decodeFormats(DecodeFormatManager.QR_CODE_FORMATS)//设置支持的一/二维码格式
//                .framingRectRatio(0.9f)//设置识别区域比例，范围建议在0.625 ~ 1.0之间。非全屏识别时才有效
//                .framingRectVerticalOffset(0)//设置识别区域垂直方向偏移量，非全屏识别时才有效
//                .framingRectHorizontalOffset(0)//设置识别区域水平方向偏移量，非全屏识别时才有效
                .continuousScan(isContinuousScan);//是否连扫

        icImg.setOnClickListener(v -> {
            clickFlash(v);
        });
    }
    //查询手机是否支持闪光灯
    public boolean hasTorch(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }
    //设置手电筒开关
    public void setTorch(boolean on){
        Camera camera = getCameraManager().getOpenCamera().getCamera();
        Camera.Parameters parameters = camera.getParameters();
        CameraConfigurationUtils.setTorch(parameters,on);
        camera.setParameters(parameters);
    }

    private void clickFlash(View v){
        boolean isSelected = v.isSelected();
        setTorch(!isSelected);
        v.setSelected(!isSelected);
    }
    //直接返回结果
    @Override
    public boolean onResultCallback(String result) {
        if(isContinuousScan){
            Toast.makeText(this,result, Toast.LENGTH_SHORT).show();
        }

        return super.onResultCallback(result);
    }
}