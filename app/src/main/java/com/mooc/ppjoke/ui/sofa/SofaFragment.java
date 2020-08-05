package com.mooc.ppjoke.ui.sofa;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.mooc.libarchitecture.ui.page.BaseFragment;
import com.mooc.libnavannotation.FragmentDestination;
import com.mooc.ppjoke.BR;
import com.mooc.ppjoke.R;
import com.mooc.ppjoke.data.bean.SofaTab;
import com.mooc.ppjoke.ui.state.SofaViewModel;
import com.mooc.ppjoke.utils.AppConfig;

import java.util.List;


@FragmentDestination(pageUrl = "main/tabs/sofa", asStarter = false)
public class SofaFragment extends BaseFragment {

    public SofaViewModel mSofaViewModel;

    @Override
    protected void initViewModel() {
        mSofaViewModel = getFragmentViewModel(SofaViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.fragment_sofa, BR.vm, mSofaViewModel);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSofaViewModel.fragment.set(this);
        mSofaViewModel.tabConfig.set(getTabConfig());
        mSofaViewModel.type.set(0);

    }

    public SofaTab getTabConfig() {
        return AppConfig.getSofaTabConfig();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment.isAdded() && fragment.isVisible()) {
                fragment.onHiddenChanged(hidden);
                break;
            }
        }
    }

    @Override
    public void onDestroy() {
        mSofaViewModel.destroy.set(true);
        super.onDestroy();
    }
}