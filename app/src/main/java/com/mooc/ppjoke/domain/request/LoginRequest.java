package com.mooc.ppjoke.domain.request;

import android.app.Activity;
import android.widget.Toast;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mooc.libarchitecture.data.repository.DataResult;
import com.mooc.libarchitecture.domain.request.BaseRequest;
import com.mooc.libarchitecture.utils.Utils;
import com.mooc.libnetwork.ApiResponse;
import com.mooc.libnetwork.ApiService;
import com.mooc.libnetwork.JsonCallback;
import com.mooc.ppjoke.model.User;
import com.mooc.ppjoke.ui.login.LoginActivity;
import com.mooc.ppjoke.ui.login.UserManager;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginRequest extends BaseRequest {

	public final Tencent tencent = Tencent.createInstance("101794421", Utils.getApp());

	public MutableLiveData<Boolean> loginStatus;
	public MutableLiveData<String> loginMessage;

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

		tencent.login(activity, "all", createListener(tencent));
	}


	public IUiListener createListener(Tencent tencent) {
		return new IUiListener() {
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
				loginMessage.postValue("登录失败:reason" + uiError.toString());
			}

			@Override
			public void onCancel() {
				loginMessage.postValue("登录取消");
			}
		};
	}

	private void getUserInfo(QQToken qqToken, long expires_time, String openid) {
		UserInfo userInfo = new UserInfo(Utils.getApp(), qqToken);
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
				loginMessage.postValue("登录失败:reason" + uiError.toString());
			}

			@Override
			public void onCancel() {
				loginMessage.postValue("登录取消");
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
							//finish();
							loginStatus.postValue(true);
						} else {
							loginMessage.postValue("登录失败");

//							runOnUiThread(new Runnable() {
//								@Override
//								public void run() {
//									Toast.makeText(getApplicationContext(), "登陆失败", Toast.LENGTH_SHORT).show();
//								}
//							});
						}
					}

					@Override
					public void onError(ApiResponse<User> response) {
						loginMessage.postValue("登录失败");

//						runOnUiThread(new Runnable() {
//							@Override
//							public void run() {
//								Toast.makeText(getApplicationContext(), "登陆失败,msg:" + response.message, Toast.LENGTH_SHORT).show();
//							}
//						});
					}
				});
	}


}
