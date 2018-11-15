package hackathon.elibrary.Util;

import hackathon.elibrary.Account.LogIn;
import hackathon.elibrary.Account.User;
import hackathon.elibrary.Home.AccountData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface ApiInterface {

    @GET("api/account")
    Call<AccountData> getUser();

    @POST("api/register")
    Call<User> createAccount(@Body User user );


    @POST("api/authenticate")
    Call<LogIn>  logInPtofile(@Body LogIn logIn);

}
