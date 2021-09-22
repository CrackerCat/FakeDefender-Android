package cn.bluesadi.fakedefender;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.FileNotFoundException;
import java.io.InputStream;

import cn.bluesadi.fakedefender.defender.FakeMonitor;
import cn.bluesadi.fakedefender.defender.FakeChecker;
import cn.bluesadi.fakedefender.ui.activity.ResultActivity;
import cn.bluesadi.fakedefender.ui.activity.SettingsActivity;
import cn.bluesadi.fakedefender.ui.adapter.RecordListAdapter;
import cn.bluesadi.fakedefender.ui.base.BaseActivity;
import cn.bluesadi.fakedefender.util.ScreenShot;
import cn.bluesadi.fakedefender.util.Settings;

public class MainActivity extends BaseActivity {

    private Button btnRun;
    private Button btnTerminate;
    private Button btnUpload;
    private Button btnSettings;
    private RecyclerView rvRecordList;
    private static final int SELECT_PHOTO_CODE = 0;
    private static final int REQUEST_MEDIA_PROJECTION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        btnRun = findViewById(R.id.btn_run);
        btnTerminate = findViewById(R.id.btn_terminate);
        btnUpload = findViewById(R.id.btn_upload);
        btnSettings = findViewById(R.id.btn_settings);
        rvRecordList = findViewById(R.id.rv_record_list);
        rvRecordList.setLayoutManager(new LinearLayoutManager(this));
        rvRecordList.setAdapter(RecordListAdapter.getInstance());
    }

    @Override
    protected void initListener() {
        btnRun.setOnClickListener(this::onBtnRunClick);
        btnTerminate.setOnClickListener(this::onBtnTerminateClick);
        btnUpload.setOnClickListener(this::onBtnUploadClick);
        btnSettings.setOnClickListener(this::onBtnSettingsClick);
    }

    @Override
    protected void initData() {
        Settings.init(this);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    private void onBtnRunClick(View view){
        btnRun.setVisibility(View.GONE);
        btnTerminate.setVisibility(View.VISIBLE);
        requestCapturePermission();
    }

    private void onBtnTerminateClick(View view){
        btnRun.setVisibility(View.VISIBLE);
        btnTerminate.setVisibility(View.GONE);
        FakeMonitor.getInstance().stop();
    }

    private void onBtnUploadClick(View view){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO_CODE);
    }

    private void onBtnSettingsClick(View view){
        startActivity(SettingsActivity.class);
    }

    /**
     * 获取屏幕截图的权限
     * */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void requestCapturePermission() {
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager)
                getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        startActivityForResult(
                mediaProjectionManager.createScreenCaptureIntent(),
                REQUEST_MEDIA_PROJECTION
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_PHOTO_CODE){
            if (resultCode == RESULT_OK && data != null) {
                try {
                    Uri imageUri = data.getData();
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                    ProgressDialog dialog = ProgressDialog.show(this, "", getString(R.string.checking));
                    FakeChecker.getInstance().check(bitmap, response -> {
                        dialog.dismiss();
                        Bundle bundle = new Bundle();
                        bundle.putInt("index", 0);
                        startActivity(ResultActivity.class, bundle);
                    }, true);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }else if(requestCode == REQUEST_MEDIA_PROJECTION){
            if(resultCode == RESULT_OK && data != null){
                Intent service = new Intent(this, FakeMonitor.class);
                service.putExtra("result_code", resultCode);
                service.putExtra("result_data", data);
                startForegroundService(service);
            }
        }
    }

}