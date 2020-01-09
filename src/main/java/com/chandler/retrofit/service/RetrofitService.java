package com.chandler.retrofit.service;

import org.springframework.util.MultiValueMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

public interface RetrofitService {
	
	@GET("/api/liders/tracksinfos")
	Call<ResponseBody> getPing();
	
	@POST("/api/liders/tracksinfos")
//	Call<ResponseBody> postOtetStream(@HeaderMap MultiValueMap<String, Long> headers, @Body RequestBody bytes);
	Call<ResponseBody> postOtetStream(@Header("liderId") Long liderId, @Header("vehicleId") Long vehicleId, @Body RequestBody bytes);
	
	@POST("/api/liders/tracksinfos")
	Call<ResponseBody> postOtetStream2(@Body RequestBody bytes, Callback<ResponseBody> cb);
}
