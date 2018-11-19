package hackathon.elibrary.Util;

import java.util.List;

import hackathon.elibrary.Account.LogIn;
import hackathon.elibrary.Account.User;
import hackathon.elibrary.Book.UploadBookPojo;
import hackathon.elibrary.Home.AccountData;
import hackathon.elibrary.Book.CreateBook;
import hackathon.elibrary.Book.Genre;
import hackathon.elibrary.Profile.Profile;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {

    @GET("api/account")
    Call<AccountData> getUser();

    @POST("api/register")
    Call<User> createAccount(@Body User user );


    @POST("api/authenticate")
    Call<LogIn>  logInPtofile(@Body LogIn logIn);

    @GET("api/profiles/user/{userId}")
    Call<Profile>  getProfileId(@Path("userId") long id);

    @GET("api/genres")
    Call<List<Genre>> getAllGenre();

    @POST("api/books")
    Call<CreateBook> createBook(@Body CreateBook createBook);

    @Multipart
    @POST("api/books/upload")
    Call<ResponseBody> uploadBookFile(@Part ("file\"; filename=\"pp.pdf\" ") RequestBody file,
                                        @Query("id") long id,
                                        @Query("type") String type );
  @Multipart
    @POST("api/books/upload")
    Call<ResponseBody> uploadImageFile(@Part ("file\"; filename=\"pp.jpg\" ") RequestBody file,
                                        @Query("id") long id,
                                        @Query("type") String type );

}
