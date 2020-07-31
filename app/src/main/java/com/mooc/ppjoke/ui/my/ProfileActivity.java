package com.mooc.ppjoke.ui.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.material.appbar.AppBarLayout;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.mooc.libarchitecture.ui.page.BaseActivity;
import com.mooc.libcommon.utils.StatusBar;
import com.mooc.ppjoke.BR;
import com.mooc.ppjoke.R;
import com.mooc.ppjoke.model.User;
import com.mooc.ppjoke.ui.login.UserManager;
import com.mooc.ppjoke.ui.state.ProfileActivityViewModel;

//个人详情页
public class ProfileActivity extends BaseActivity {

    private ProfileActivityViewModel mProfileViewModel;

    public static final String TAB_TYPE_ALL = "tab_all";
    public static final String TAB_TYPE_FEED = "tab_feed";
    public static final String TAB_TYPE_COMMENT = "tab_comment";

    public static final String KEY_TAB_TYPE = "key_tab_type";

    public static void startProfileActivity(Context context, String tabType) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(KEY_TAB_TYPE, tabType);
        context.startActivity(intent);
    }

    @Override
    protected void initViewModel() {
        mProfileViewModel = getActivityViewModel(ProfileActivityViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.activity_layout_profile, BR.vm, mProfileViewModel)
                .addBindingParam(BR.proxy, new ClickProxy());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        StatusBar.fitSystemBar(this);
        super.onCreate(savedInstanceState);
        String initTab = getIntent().getStringExtra(KEY_TAB_TYPE);
        User user = UserManager.get().getUser();
        mProfileViewModel.user.set(user);
        mProfileViewModel.activity.set(this);
        mProfileViewModel.initTab.set(initTab);
    }


    public class ClickProxy{
        public void finshActivity(){
            finish();
        }

        //TODO 不生效
        public AppBarLayout.OnOffsetChangedListener offsetChangeListener(){
            return (appBarLayout, verticalOffset) -> {
                boolean expand = Math.abs(verticalOffset) < appBarLayout.getTotalScrollRange();
                mProfileViewModel.expand.set(expand);
            };
        }
    }
}
