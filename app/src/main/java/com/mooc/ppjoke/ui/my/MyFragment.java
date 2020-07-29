package com.mooc.ppjoke.ui.my;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.mooc.libcommon.utils.StatusBar;
import com.mooc.libnavannotation.FragmentDestination;
import com.mooc.ppjoke.R;
import com.mooc.ppjoke.databinding.FragmentMyBinding;
import com.mooc.ppjoke.model.User;
import com.mooc.ppjoke.ui.login.UserManager;

@FragmentDestination(pageUrl = "main/tabs/my", needLogin = true)
public class MyFragment extends Fragment {
    private FragmentMyBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMyBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        User user = UserManager.get().getUser();
        mBinding.setUser(user);

        UserManager.get().refresh().observe(this, newUser -> {
            if (newUser != null) {
                mBinding.setUser(newUser);
            }
        });

        mBinding.actionLogout.setOnClickListener(v -> new AlertDialog.Builder(getContext())
                .setMessage(getString(R.string.fragment_my_logout))
                .setPositiveButton(getString(R.string.fragment_my_logout_ok), (dialog, which) -> {
                    dialog.dismiss();
                    UserManager.get().logout();
                    getActivity().onBackPressed();
                }).setNegativeButton(getString(R.string.fragment_my_logout_cancel), null)
                .create().show());

        mBinding.goDetail.setOnClickListener(v -> ProfileActivity.startProfileActivity(getContext(), ProfileActivity.TAB_TYPE_ALL));
        mBinding.userFeed.setOnClickListener(v -> ProfileActivity.startProfileActivity(getContext(), ProfileActivity.TAB_TYPE_FEED));
        mBinding.userComment.setOnClickListener(v -> ProfileActivity.startProfileActivity(getContext(), ProfileActivity.TAB_TYPE_COMMENT));
        mBinding.userFavorite.setOnClickListener(v -> UserBehaviorListActivity.startBehaviorListActivity(getContext(), UserBehaviorListActivity.BEHAVIOR_FAVORITE));
        mBinding.userHistory.setOnClickListener(v -> UserBehaviorListActivity.startBehaviorListActivity(getContext(), UserBehaviorListActivity.BEHAVIOR_HISTORY));
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        StatusBar.lightStatusBar(getActivity(), false);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        StatusBar.lightStatusBar(getActivity(), hidden);
    }
}
