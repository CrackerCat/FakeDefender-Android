package cn.bluesadi.fakedefender.ui.base;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import java.lang.ref.WeakReference;

import cn.bluesadi.fakedefender.R;

abstract public class BaseActivity extends AppCompatActivity {

    private static WeakReference<BaseActivity> topActivity = null;
    private FrameLayout frameLayout;
    private Toast currentToast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBaseView();
        initView();
        initListener();
        initData();
        topActivity = new WeakReference<>(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        topActivity = new WeakReference<>(this);
    }

    private void initBaseView(){
        setContentView(R.layout.activity_base);
        frameLayout = findViewById(R.id.layout_base);
        View baseView = LayoutInflater.from(this).inflate(initLayout(), frameLayout, false);
        frameLayout.addView(baseView);
        frameLayout.setBackgroundResource(R.drawable.background);
        frameLayout.getBackground().setAlpha(100);
    }

    protected void initView(){

    }

    protected void initListener(){

    }

    protected void initData(){

    }

    abstract protected int initLayout();

    public void startActivity(Class<?> clz) {
        startActivity(clz, null);
    }

    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void toast(String text){
        toast(text, Toast.LENGTH_SHORT);
    }

    public void toast(String text, int duration){
        currentToast = Toast.makeText(this, text, duration);
        currentToast.show();
    }

    public void cancelToast(){
        if(currentToast != null){
            currentToast.cancel();
        }
    }

    public static BaseActivity getTopActivity(){
        return topActivity.get();
    }


}