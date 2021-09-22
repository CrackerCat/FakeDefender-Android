package cn.bluesadi.fakedefender.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import java.util.Set;

import cn.bluesadi.fakedefender.MainActivity;
import cn.bluesadi.fakedefender.R;
import cn.bluesadi.fakedefender.ui.base.BaseActivity;
import cn.bluesadi.fakedefender.util.Settings;

public class SettingsActivity extends BaseActivity {

    private Button btnSaveSettings;
    private RadioGroup rgPeriod;
    private RadioGroup rgThreshold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_settings;
    }

    @Override
    protected void initView() {
        btnSaveSettings = findViewById(R.id.btn_save_settings);
        rgPeriod = findViewById(R.id.rg_period);
        if(Settings.period == Settings.LONG_PERIOD){
            rgPeriod.check(R.id.rb_long_period);
        }else if(Settings.period == Settings.SHORT_PERIOD){
            rgPeriod.check(R.id.rb_short_period);
        }else{
            rgPeriod.check(R.id.rb_middle_period);
        }
        rgThreshold = findViewById(R.id.rg_threshold);
        if(Settings.threshold == Settings.HIGH_THRESHOLD){
            rgThreshold.check(R.id.rb_high_threshold);
        }else if(Settings.threshold == Settings.LOW_THRESHOLD){
            rgThreshold.check(R.id.rb_low_threshold);
        }else{
            rgThreshold.check(R.id.rb_middle_threshold);
        }
    }

    @Override
    protected void initListener() {
        btnSaveSettings.setOnClickListener(this::saveSettings);
    }

    public void saveSettings(View view){
        int checkedPeriodId = rgPeriod.getCheckedRadioButtonId();
        if(checkedPeriodId == R.id.rb_long_period){
            Settings.period = Settings.LONG_PERIOD;
        }else if(checkedPeriodId == R.id.rb_short_period){
            Settings.period = Settings.SHORT_PERIOD;
        }else{
            Settings.period = Settings.MIDDLE_PERIOD;
        }
        int checkedThresholdId = rgThreshold.getCheckedRadioButtonId();
        if(checkedThresholdId == R.id.rb_high_threshold){
            Settings.threshold = Settings.HIGH_THRESHOLD;
        }else if(checkedThresholdId == R.id.rb_low_threshold){
            Settings.threshold = Settings.LOW_THRESHOLD;
        }else{
            Settings.threshold = Settings.MIDDLE_THRESHOLD;
        }
        Settings.save();

        startActivity(MainActivity.class);
        toast(getString(R.string.tx_settings_saved));
    }
}