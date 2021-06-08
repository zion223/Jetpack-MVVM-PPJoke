package com.mooc.ppjoke.domain.request;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mooc.libarchitecture.domain.request.BaseRequest;
import com.mooc.libarchitecture.utils.Utils;
import com.mooc.libnetwork.ApiResponse;
import com.mooc.libnetwork.ApiService;
import com.mooc.libnetwork.JsonCallback;
import com.mooc.ppjoke.data.bean.User;
import com.mooc.ppjoke.ui.login.UserManager;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginRequest extends BaseRequest {


	public MutableLiveData<Boolean> loginStatus;
	public MutableLiveData<String> loginMessage;

	public final Tencent tencent = Tencent.createInstance("101794421", Utils.getApp());


	public LiveData<Boolean> getLoginStatus() {
		if (loginStatus == null) {
			loginStatus = new MutableLiveData<>();
		}
		return loginStatus;
	}

	public LiveData<String> getLoginMessage() {
		if (loginMessage == null) {
			loginMessage = new MutableLiveData<>();
		}
		return loginMessage;
	}

	public void requestLogin(Activity activity) {

		tencent.login(activity, "all", listener);
	}


	public IUiListener listener = new IUiListener() {
		@Override
		public void onComplete(Object o) {
			JSONObject response = (JSONObject) o;
			try {
                String openid = response.getString("openid");
                String accessToken = response.getString("access_token");
                String expiresIn = response.getString("expires_in");
                long expiresTime = response.getLong("expires_time");

                tencent.setOpenId(openid);
                tencent.setAccessToken(accessToken, expiresIn);
                QQToken qqToken = tencent.getQQToken();
                getUserInfo(qqToken, expiresTime, openid);
            } catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onError(UiError uiError) {
			UserManager.get().save(null);
			loginMessage.postValue("登录失败:reason" + uiError.toString());
        }

        @Override
        public void onCancel() {
            UserManager.get().save(null);
            loginMessage.postValue("登录取消");
        }
    };


    private void getUserInfo(QQToken qqToken, long expiresTime, String openid) {
        UserInfo userInfo = new UserInfo(Utils.getApp(), qqToken);
        userInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object o) {
                JSONObject response = (JSONObject) o;

                try {
                    String nickname = response.getString("nickname");
                    String figureurl2 = response.getString("figureurl_2");

                    save(nickname, figureurl2, openid, expiresTime);
                } catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(UiError uiError) {
				UserManager.get().save(null);
				loginMessage.postValue("登录失败:reason" + uiError.toString());
            }

            @Override
            public void onCancel() {
                UserManager.get().save(null);
                loginMessage.postValue("登录取消");
            }
        });
    }

    private void save(String nickname, String avatar, String openid, long expiresTime) {
        ApiService.get("/user/insert")
                .addParam("name", nickname)
                .addParam("avatar", avatar)
                .addParam("qqOpenId", openid)
                .addParam("expires_time", expiresTime)
                .execute(new JsonCallback<User>() {
                    @Override
                    public void onSuccess(ApiResponse<User> response) {
                        if (response.body != null) {
                            UserManager.get().save(response.body);
							loginStatus.postValue(true);
						} else {
							loginMessage.postValue("登录失败");
							UserManager.get().save(null);
						}
					}

					@Override
					public void onError(ApiResponse<User> response) {
						loginMessage.postValue("登录失败");
						UserManager.get().save(null);
					}
				});
	}


}
