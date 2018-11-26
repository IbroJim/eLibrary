package hackathon.elibrary.Util;

import java.util.ArrayList;
import java.util.List;

import hackathon.elibrary.POJO.AddFavorite;
import hackathon.elibrary.POJO.Book;
import hackathon.elibrary.POJO.BookDetails;
import hackathon.elibrary.POJO.LogIn;
import hackathon.elibrary.POJO.User;
import hackathon.elibrary.POJO.AccountData;
import hackathon.elibrary.POJO.CreateBook;
import hackathon.elibrary.POJO.Genre;
import hackathon.elibrary.POJO.Profile;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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


  @GET("api/books/{id}/download")
    Call<ResponseBody> downLoadBook(@Path ("id") long id);

  @GET("api/books/")
    Call<ArrayList<Book>> getAllBooks();

  @GET("api/books/{id}")
    Call<BookDetails> getBook(@Path("id") long id);

  @POST("api/favorite-books")
    Call<AddFavorite> createFavoriteBook(@Body AddFavorite addFavorite);


  @DELETE("api/favorite-books/{id}")
    Call<ResponseBody> deleteFavoriteBook(@Path("id") long id);


  @GET("api/books/downloadCover/{bookId}")
    Call<ResponseBody>  downloadCover(@Path("bookId")long bookId);

}
