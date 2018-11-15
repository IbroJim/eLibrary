package hackathon.elibrary.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OkHttpHelper {

    private   static final String BASE_URL="https://elibrary-app.herokuapp.com/#/docs/";
    private OkHttpClient.Builder okHttpClient=null;


    public static Retrofit getRetrofitToken(final String token){
        OkHttpClient.Builder okHttpClient=new OkHttpClient.Builder();
        okHttpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original=chain.request();
                Request request=original.newBuilder()
                        .addHeader("Accept","application/json")
                        .addHeader("Authorization","Bearer "+ token).method(original.method(),original.body()).build();
                return chain.proceed(request);
            }
        });
       Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.build()).build();
        return retrofit;
    }
}
