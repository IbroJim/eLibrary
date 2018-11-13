package hackathon.elibrary.Util;

import hackathon.elibrary.Account.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("/api/register")
    Call<User> createAccount(@Body User user );
}
