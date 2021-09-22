package cn.bluesadi.fakedefender.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import cn.bluesadi.fakedefender.R;
import cn.bluesadi.fakedefender.defender.FakeChecker;
import cn.bluesadi.fakedefender.ui.base.BaseActivity;

public class ResultActivity extends BaseActivity {

    private int index = 0;
    private ImageView imgPreview;
    private Button btnSaveResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_result;
    }

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        if(bundle.containsKey("index")){
            index = bundle.getInt("index");
        }
        imgPreview = findViewById(R.id.img_preview);
        btnSaveResult = findViewById(R.id.btn_save_result);
        imgPreview.setImageBitmap(FakeChecker.getInstance().getRecord(index).getResultImage());
    }

    @Override
    protected void initListener() {
        btnSaveResult.setOnClickListener(this::saveResultImage);
    }

    public void saveResultImage(View view){
        FakeChecker.getInstance().getRecord(index).saveImage();
        toast(getString(R.string.tx_saved_result));
    }
}