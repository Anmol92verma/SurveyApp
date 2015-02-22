package com.prototype.soundservey;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import android.app.Application;

public class ApplicationClass extends Application {

	private RestClientInterface interface1;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		RequestInterceptor requestInterceptor = new RequestInterceptor() {
			@Override
			public void intercept(RequestFacade request) {
				request.addHeader("User-Agent", "Survey-APP");
			}
		};

		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint(GlobalVariable.API_URL)
				.setRequestInterceptor(requestInterceptor).build();

		interface1 = restAdapter.create(RestClientInterface.class);

	}

	public RestClientInterface getInterface1() {
		return interface1;
	}

	public void setInterface1(RestClientInterface interface1) {
		this.interface1 = interface1;
	}

}
