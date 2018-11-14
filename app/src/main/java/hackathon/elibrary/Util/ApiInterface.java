package hackathon.elibrary.Util;

import android.accounts.Account;
import android.content.SharedPreferences;

import hackathon.elibrary.Account.LogIn;
import hackathon.elibrary.Account.User;
import hackathon.elibrary.Home.AcoountData;
import hackathon.elibrary.Home.HomeActivity;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;



public interface ApiInterface {

    @GET("api/account")
    Call<AcoountData> getUser();

    @POST("api/register")
    Call<User> createAccount(@Body User user );


    @POST("api/authenticate")
    Call<LogIn>  logInPtofile(@Body LogIn logIn);

}
