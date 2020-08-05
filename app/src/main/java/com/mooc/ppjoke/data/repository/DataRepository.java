package com.mooc.ppjoke.data.repository;

import androidx.lifecycle.MutableLiveData;

public class DataRepository implements ILocalRequest, IRemoteRequest {

	private static final DataRepository S_REQUEST_MANAGER = new DataRepository();
	private MutableLiveData<String> responseCodeLiveData;

	private DataRepository() {
	}

	public static DataRepository getInstance() {
		return S_REQUEST_MANAGER;
	}

}
