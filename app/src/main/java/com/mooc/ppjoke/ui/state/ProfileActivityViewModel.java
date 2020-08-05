package com.mooc.ppjoke.ui.state;

import androidx.databinding.ObservableField;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;

import com.mooc.ppjoke.data.bean.User;

public class ProfileActivityViewModel extends ViewModel {

	public final ObservableField<User> user = new ObservableField<>();
	public final ObservableField<Boolean> expand = new ObservableField<>();

	//初始化tablayout
	public final ObservableField<FragmentActivity> activity = new ObservableField<>();
	public final ObservableField<String> initTab = new ObservableField<>();

	{
		expand.set(true);
	}
}
