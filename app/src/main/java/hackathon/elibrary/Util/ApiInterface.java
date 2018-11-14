package hackathon.elibrary.Util;

import hackathon.elibrary.Account.LogIn;
import hackathon.elibrary.Account.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("api/register")
    Call<User> createAccount(@Body User user );
    @POST("api/authenticate")
    Call<LogIn>  logInPtofile(@Body LogIn logIn);

}
