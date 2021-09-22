package cn.bluesadi.fakedefender.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.bluesadi.fakedefender.MainActivity;
import cn.bluesadi.fakedefender.R;
import cn.bluesadi.fakedefender.network.AuthLib;
import cn.bluesadi.fakedefender.network.NetworkUtils;
import cn.bluesadi.fakedefender.ui.base.BaseActivity;

public class LoginActivity extends BaseActivity {

    private Button btnLogin;
    private EditText etEmail;
    private EditText etPassword;
    private TextView txGoRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetworkUtils.init(this);
    }

    @Override
    protected void initView() {
        btnLogin = findViewById(R.id.btn_login);
        txGoRegister = findViewById(R.id.tx_go_register);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
    }

    @Override
    protected void initListener() {
        btnLogin.setOnClickListener(this::onBtnLoginClick);
        txGoRegister.setOnClickListener(this::onTxGoRegisterClick);
    }

    public void onBtnLoginClick(View view){
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        AuthLib.sendLoginRequest(email, password, response -> {
            if(response.isSuccess()){
                toast(getString(R.string.login_success));
                startActivity(MainActivity.class);
            }else{
                toast(getString(R.string.login_fail));
            }
        });
    }

    public void onTxGoRegisterClick(View view){
        startActivity(RegisterActivity.class);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_login;
    }

}