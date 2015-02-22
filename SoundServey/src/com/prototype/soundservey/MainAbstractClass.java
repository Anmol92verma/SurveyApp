package com.prototype.soundservey;

import android.app.Activity;
import android.support.v4.app.Fragment;

public abstract class MainAbstractClass extends Fragment {

	private ApplicationClass application;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		application = (ApplicationClass) getActivity().getApplication();

	}

	public RestClientInterface getService() {
		return application.getInterface1();
	}

}
