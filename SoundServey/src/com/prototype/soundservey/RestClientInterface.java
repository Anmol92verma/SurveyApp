package com.prototype.soundservey;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

public interface RestClientInterface {

	@Multipart
	@POST("/user/photo")
	void updateUser(@Part("recording") CountingTypedFile recording,
			@Part("name") String description, @Part("address") String address,
			Callback<MODEL_RESPONSE> callback);

}
