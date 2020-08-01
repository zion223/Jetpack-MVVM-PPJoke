package com.mooc.ppjoke.ui.login;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.mooc.libarchitecture.ui.page.BaseActivity;
import com.mooc.libnetwork.ApiResponse;
import com.mooc.libnetwork.ApiService;
import com.mooc.libnetwork.JsonCallback;
import com.mooc.ppjoke.BR;
import com.mooc.ppjoke.R;
import com.mooc.ppjoke.model.User;
import com.mooc.ppjoke.ui.state.LoginViewModel;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity {

    private Tencent tencent;

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

    public class ClickProxy {
        public void login() {
            if (tencent == null) {
                tencent = Tencent.createInstance("101794421", getApplicationContext());
            }
            tencent.login(LoginActivity.this, "all", loginListener);
        }

        public void finishActivity() {
            finish();
        }
    }


    IUiListener loginListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            JSONObject response = (JSONObject) o;
            try {
                String openid = response.getString("openid");
                String access_token = response.getString("access_token");
                String expires_in = response.getString("expires_in");
                long expires_time = response.getLong("expires_time");

                tencent.setOpenId(openid);
                tencent.setAccessToken(access_token, expires_in);
                QQToken qqToken = tencent.getQQToken();
                getUserInfo(qqToken, expires_time, openid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(getApplicationContext(), "登录失败:reason" + uiError.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(getApplicationContext(), "登录取消", Toast.LENGTH_SHORT).show();
        }
    };

    private void getUserInfo(QQToken qqToken, long expires_time, String openid) {
        UserInfo userInfo = new UserInfo(getApplicationContext(), qqToken);
        userInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object o) {
                JSONObject response = (JSONObject) o;

                try {
                    String nickname = response.getString("nickname");
                    String figureurl_2 = response.getString("figureurl_2");

                    save(nickname, figureurl_2, openid, expires_time);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {
                Toast.makeText(getApplicationContext(), "登录失败:reason" + uiError.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "登录取消", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void save(String nickname, String avatar, String openid, long expires_time) {
        ApiService.get("/user/insert")
                .addParam("name", nickname)
                .addParam("avatar", avatar)
                .addParam("qqOpenId", openid)
                .addParam("expires_time", expires_time)
                .execute(new JsonCallback<User>() {
                    @Override
                    public void onSuccess(ApiResponse<User> response) {
                        if (response.body != null) {
                            UserManager.get().save(response.body);
                            finish();
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "登陆失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(ApiResponse<User> response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "登陆失败,msg:" + response.message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        }
    }
}
