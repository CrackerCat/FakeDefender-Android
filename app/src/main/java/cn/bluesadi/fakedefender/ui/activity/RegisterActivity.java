package cn.bluesadi.fakedefender.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import cn.bluesadi.fakedefender.R;
import cn.bluesadi.fakedefender.network.AuthLib;
import cn.bluesadi.fakedefender.ui.base.BaseActivity;

public class RegisterActivity extends BaseActivity {

    private Button btnRegister;
    private TextView txGoLogin;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etRepeatPassword;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        btnRegister = findViewById(R.id.btn_register);
        txGoLogin = findViewById(R.id.tx_go_login);
        etEmail = findViewById(R.id.et_register_email);
        etPassword = findViewById(R.id.et_register_password);
        etRepeatPassword = findViewById(R.id.et_register_repeat_password);
    }

    @Override
    protected void initListener() {
        btnRegister.setOnClickListener(this::onBtnRegisterClick);
        txGoLogin.setOnClickListener(this::onTxGoLoginClick);
    }

    public void onBtnRegisterClick(View view){
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String repeatPassword = etRepeatPassword.getText().toString();
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if(!matcher.matches()){
            toast(getString(R.string.tx_email_invaild));
            return;
        }
        if(!password.equals(repeatPassword)){
            toast(getString(R.string.tx_password_not_match));
            return;
        }
        if(password.length() < 8){
            toast(getString(R.string.tx_password_not_strong));
            return;
        }
        AuthLib.sendRegisterRequest(email, password, response -> {
            if(response.isSuccess()){
                toast(getString(R.string.tx_register_success));
                startActivity(LoginActivity.class);
            }else{
                toast(getString(R.string.tx_register_fail));
            }
        });
    }

    public void onTxGoLoginClick(View view){
        startActivity(LoginActivity.class);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_register;
    }
}