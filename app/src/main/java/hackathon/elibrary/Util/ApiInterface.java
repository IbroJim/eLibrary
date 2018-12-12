package hackathon.elibrary.Util;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hackathon.elibrary.MyDataBase.DatabaseHelper;
import hackathon.elibrary.POJO.BookHelp;
import hackathon.elibrary.POJO.Favorite;
import hackathon.elibrary.POJO.Book;
import hackathon.elibrary.POJO.BookDetails;
import hackathon.elibrary.POJO.FavoriteBook;
import hackathon.elibrary.POJO.HelpBook;
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
    Call<ArrayList<Book>> getAllBooks(@Query("approved.equals") boolean approved);

  @GET("api/books/{id}")
    Call<BookDetails> getBook(@Path("id") long id);

  @POST("api/favorite-books")
    Call<Favorite> createFavoriteBook(@Body Favorite addFavorite);


  @DELETE("api/favorite-books/{id}")
    Call<ResponseBody> deleteFavoriteBook(@Path("id") long id);


  @GET("api/books/downloadCover/{bookId}")
    Call<ResponseBody>  downloadCover(@Path("bookId")long bookId);

  @GET("api/favorite-books")
    Call<ArrayList<FavoriteBook>> getAllFavoriteBook(@Query("profileId.equals") long profileId);

  @GET("api/read-books")
    Call<ArrayList<FavoriteBook>> getAllreadBooks();

  @POST("api/read-books")
    Call<Favorite> createReadBook(@Body Favorite addFavorite);

  @GET("api/favorite-books")
    Call<ArrayList<FavoriteBook>> checkFavoriteBook(@Query("bookId.equals") long bookId,
                                                    @Query("profileId.equals") long profileId);
  @GET("api/books/")
    Call<ArrayList<Book>> getBookFiltrGenre(@Query("approved.equals") boolean approved,
                                                @Query("genreId.equals")long genre);  @GET("api/books/")
    Call<ArrayList<BookHelp>> getBookFiltrGenreHelp(@Query("approved.equals") boolean approved,
                                                @Query("genreId.equals")long genre);

  @GET("api/favorite-books/top")
    Call<ArrayList<Book>> getTopBooks(@Query("approved.equals") boolean approved);

  @GET("api/books")
    Call<ArrayList<HelpBook>> getNewBook(@Query("approved.equals") boolean approved,
                                         @Query("createdDate.greaterThan")String date);

}
