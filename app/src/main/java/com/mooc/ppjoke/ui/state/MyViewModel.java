package com.mooc.ppjoke.ui.state;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mooc.ppjoke.model.User;

public class MyViewModel extends ViewModel {

	public final MutableLiveData<User> user = new MutableLiveData<>();

}
