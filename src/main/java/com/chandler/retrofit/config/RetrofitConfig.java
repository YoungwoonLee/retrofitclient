package com.chandler.retrofit.config;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.chandler.retrofit.service.RetrofitService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class RetrofitConfig {

    private static Retrofit buildRetrofit(String baseUrl, OkHttpClient client) {
        return new Retrofit.Builder()
					                .addConverterFactory(defaultConverter())
					                .addCallAdapterFactory(defaultCallAdapter())
					                .client(client)
					                .baseUrl(baseUrl)
					                .build();
    }

    private static retrofit2.Converter.Factory defaultConverter() {
        return JacksonConverterFactory.create();
    }

    private static retrofit2.CallAdapter.Factory defaultCallAdapter() {
        return RxJava2CallAdapterFactory.create();
    }

    @Bean("RetrofitService")
    public RetrofitService retrofitService() {
    	HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    	
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();
        
        final Retrofit retrofit = buildRetrofit("http://127.0.0.1:5801", okHttpClient);
        return retrofit.create(RetrofitService.class);
    }
/**    
    @Bean("jsonPlaceholderService")
    public JsonPlaceholderService jsonPlaceholderService(@Qualifier("jsonPlaceholderRetrofit") Retrofit jsonPlaceHolderRetrofit) {
        return jsonPlaceHolderRetrofit.create(JsonPlaceholderService.class);
    }
*/    
}
