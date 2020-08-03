package com.mooc.ppjoke.ui.my;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.mooc.libarchitecture.ui.page.BaseFragment;
import com.mooc.libcommon.utils.StatusBar;
import com.mooc.libnavannotation.FragmentDestination;
import com.mooc.ppjoke.BR;
import com.mooc.ppjoke.R;
import com.mooc.ppjoke.model.User;
import com.mooc.ppjoke.ui.login.UserManager;
import com.mooc.ppjoke.ui.state.MyViewModel;

@FragmentDestination(pageUrl = "main/tabs/my", needLogin = true)
public class MyFragment extends BaseFragment {

	private MyViewModel myViewModel;


	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		User user = UserManager.get().getUser();
		myViewModel.user.setValue(user);

		UserManager.get().refresh().observe(getViewLifecycleOwner(), newUser -> {
			if (newUser != null) {
				myViewModel.user.setValue(newUser);
			}
		});
	}


	@Override
	protected void initViewModel() {
		myViewModel = getFragmentViewModel(MyViewModel.class);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		StatusBar.lightStatusBar(getActivity(), false);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected DataBindingConfig getDataBindingConfig() {
		return new DataBindingConfig(R.layout.fragment_my, BR.vm, myViewModel)
				.addBindingParam(BR.proxy, new ClickProxy());
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		StatusBar.lightStatusBar(getActivity(), hidden);
	}

	public class ClickProxy {
		public void actionLogout() {
			new AlertDialog.Builder(getContext())
					.setMessage(getString(R.string.fragment_my_logout))
					.setPositiveButton(getString(R.string.fragment_my_logout_ok), (dialog, which) -> {
						dialog.dismiss();
						UserManager.get().logout();
						getActivity().onBackPressed();
					}).setNegativeButton(getString(R.string.fragment_my_logout_cancel), null)
					.create().show();
		}

		public void goDetail() {
            ProfileActivity.startProfileActivity(getContext(), ProfileActivity.TAB_TYPE_ALL);
		}

		public void userFeed() {
            ProfileActivity.startProfileActivity(getContext(), ProfileActivity.TAB_TYPE_FEED);
		}

		public void userComment() {
            ProfileActivity.startProfileActivity(getContext(), ProfileActivity.TAB_TYPE_COMMENT);
		}

		public void userFavorite() {
            UserBehaviorListActivity.startBehaviorListActivity(getContext(), UserBehaviorListActivity.BEHAVIOR_FAVORITE);
		}

		public void userHistory() {
            UserBehaviorListActivity.startBehaviorListActivity(getContext(), UserBehaviorListActivity.BEHAVIOR_HISTORY);
		}
	}
}
