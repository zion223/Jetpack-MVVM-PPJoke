package com.mooc.ppjoke.ui.find;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.mooc.libarchitecture.ui.page.BaseFragment;
import com.mooc.libnavannotation.FragmentDestination;
import com.mooc.ppjoke.BR;
import com.mooc.ppjoke.R;
import com.mooc.ppjoke.model.SofaTab;
import com.mooc.ppjoke.ui.state.FindViewModel;
import com.mooc.ppjoke.ui.state.TagListViewModel;
import com.mooc.ppjoke.utils.AppConfig;


@FragmentDestination(pageUrl = "main/tabs/find")
public class FindFragment extends BaseFragment {

    public FindViewModel mFindViewModel;


    @Override
    protected void initViewModel() {
        mFindViewModel = getFragmentViewModel(FindViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.fragment_find, BR.vm, mFindViewModel);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFindViewModel.fragment.set(this);
        mFindViewModel.tabConfig.set(getTabConfig());
        mFindViewModel.destroy.set(false);
        mFindViewModel.type.set(1);

    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        String tagType = childFragment.getArguments().getString(TagListFragment.KEY_TAG_TYPE);
        if (TextUtils.equals(tagType, "onlyFollow")) {
            //跳转去推荐页面
            ViewModelProviders.of(childFragment).get(TagListViewModel.class)
                    .getSwitchTabLiveData().observe(this, new Observer() {
                @Override
                public void onChanged(Object o) {
                    //TODO  设置同样的值 BindingAdapter中的方法不会重复执行
                    // 采用了笨方法 后续待改进
                    mFindViewModel.currentItem.set(1);
                    mFindViewModel.currentItem.set(2);
                }
            });
        }
    }

    public SofaTab getTabConfig() {
        return AppConfig.getFindTabConfig();
    }

}