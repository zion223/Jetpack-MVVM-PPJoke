package com.mooc.ppjoke.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.mooc.libarchitecture.ui.page.BaseActivity;
import com.mooc.ppjoke.BR;
import com.mooc.ppjoke.R;
import com.mooc.ppjoke.ui.state.LoginViewModel;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;


public class LoginActivity extends BaseActivity {

    private LoginViewModel mLoginViewModel;

    @Override
    protected void initViewModel() {
        mLoginViewModel = getActivityViewModel(LoginViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.activity_layout_login, BR.vm, mLoginViewModel)
                .addBindingParam(BR.proxy, new ClickProxy());
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoginViewModel.loginRequest.getLoginStatus().observe(this, aBoolean -> {
            //登陆成功
            if (aBoolean) {
                this.finish();
            }
        });
        //登录时状态信息
        mLoginViewModel.loginRequest.getLoginMessage().observe(this, this::showShortToast);
    }

    public class ClickProxy {
        public void login() {
            mLoginViewModel.loginRequest.requestLogin(LoginActivity.this);
        }

        public void finishActivity() {
            UserManager.get().save(null);
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mLoginViewModel.loginRequest.listener);
        }
    }
}
