package com.mooc.ppjoke.ui.state;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.mooc.ppjoke.model.SofaTab;

public class SofaViewModel extends ViewModel {

	public final ObservableField<Fragment> fragment = new ObservableField<>();
	public final ObservableField<SofaTab> tabConfig = new ObservableField<>();
	public final ObservableBoolean destroy = new ObservableBoolean();
	public final ObservableInt type = new ObservableInt();

	{
		destroy.set(false);
	}
}
