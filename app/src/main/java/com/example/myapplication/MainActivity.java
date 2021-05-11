package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.king.zxing.CaptureActivity;
import com.king.zxing.Intents;
import com.king.zxing.util.CodeUtils;


public class MainActivity extends AppCompatActivity {
    Button sync_btn,sync_sequence_btn,frag_sync_btn,pic_sync_btn;
    public static final int REQUEST_CODE_SCAN = 0X01;
    public static final int REQUEST_CODE_PHOTO = 0X02;
    public static final String KEY_IS_CONTINUOUS = "key_continuous_scan";
    private boolean isContinuousScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sync_btn = findViewById(R.id.QuickSync);
        sync_sequence_btn = findViewById(R.id.SyncSquence);
        frag_sync_btn = findViewById(R.id.FragmentSync);
        pic_sync_btn = findViewById(R.id.syncpic);
        initView();
    }

    private void initView() {
        isContinuousScan = false;
        sync_btn.setOnClickListener(v -> startSync(CaptureActivity.class));
        sync_sequence_btn.setOnClickListener(v -> {
            isContinuousScan = true;
            startSync(SyncSequence.class);
        });
        frag_sync_btn.setOnClickListener(v -> startSync(FragmentActivity.class));
        pic_sync_btn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, REQUEST_CODE_PHOTO);
        });
    }
    private void startSync(Class cls){
        Intent intent = new Intent(MainActivity.this,cls);
        intent.putExtra(KEY_IS_CONTINUOUS,isContinuousScan);
        startActivityForResult(intent,REQUEST_CODE_SCAN);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SCAN:
                    String result = data.getStringExtra(Intents.Scan.RESULT);
                    Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
                    break;
                case REQUEST_CODE_PHOTO:
                    parsePhoto(data);
                    break;
                default:
                    Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parsePhoto(Intent data){
        final String path = UriUtils.INSTANCE.getImagePath(this,data);
        if(TextUtils.isEmpty(path)){
            return;
        }
        final String result = CodeUtils.parseCode(path);
        Toast.makeText(MainActivity.this,result,Toast.LENGTH_SHORT).show();
    }

}
