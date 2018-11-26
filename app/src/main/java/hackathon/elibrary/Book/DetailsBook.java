package hackathon.elibrary.Book;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.like.LikeButton;
import com.like.OnLikeListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import hackathon.elibrary.POJO.AddFavorite;
import hackathon.elibrary.POJO.BookDetails;
import hackathon.elibrary.R;
import hackathon.elibrary.Util.ApiInterface;
import hackathon.elibrary.Util.OkHttpHelper;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsBook extends AppCompatActivity  {

    private static final String SAVE_TOKEN = "saveToken";
    private static final String BASE_URL="https://elibrary-app.herokuapp.com/#/docs/";
    private static final String ID_BOOK = "idBook";
    private static final String TAG = "DetailsBook";


    private Button downlaodBookButton;
    private LikeButton addFavoritesBook;
    private String nameFile;
    private Context mContext=DetailsBook.this;
    private TextView txtNameBook,txtLastName,txtFirstName,txtPage,txtDatePublication,txtGenre,txtDiscription,txtCreatedBy;
    private int idFavorite;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Details","Hello world");
        setContentView(R.layout.activity_detailed_discription);
        setupView();
        clickListenner();
        getBook();
    }
    private void setupView(){
        txtNameBook=(TextView) findViewById(R.id.name_book);
        txtFirstName=(TextView) findViewById(R.id.first_name_avtor);
        txtLastName=(TextView) findViewById(R.id.last_name_avtor);
        txtGenre=(TextView) findViewById(R.id.book_genre) ;
        txtPage=(TextView) findViewById(R.id.book_page);
        txtDiscription=(TextView)findViewById(R.id.book_description);
        txtDatePublication=(TextView) findViewById(R.id.date_publication);
        downlaodBookButton=(Button) findViewById(R.id.download_books);
        txtCreatedBy=(TextView) findViewById(R.id.created_by);
    }
    private void downloadBook(){
        OkHttpClient.Builder httpClient=new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original=chain.request();
                Request request=original.newBuilder()
                        .addHeader("Accept","application/pdf")
                        .addHeader("Authorization","Bearer "+ getToken()).method(original.method(),original.body()).build();
                return chain.proceed(request);
            }
        });
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        ApiInterface apiInterface= retrofit.create(ApiInterface.class);
        Call<ResponseBody> call=apiInterface.downLoadBook(getIdBook());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    boolean succes = writeResponceBodyToDisk(nameFile, response.body());
                    Log.d("Details", "is " + succes);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
    private String getToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("myToken", Context.MODE_PRIVATE);
        return sharedPreferences.getString(SAVE_TOKEN, "");
    }
    private boolean writeResponceBodyToDisk(String name, ResponseBody body){
        try {
            File pdfFile=new File(Environment.getExternalStoragePublicDirectory("/eLibrary"),name);
            InputStream inputStream=null;
            OutputStream outputStream=null;
            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownLoad = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(pdfFile);

                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownLoad += read;

                    Log.d("Details","download"+fileSizeDownLoad+" of "+fileSize);
                }
                outputStream.flush();
                return true;
            }catch (IOException e){
                return false;
            }finally {
                if(inputStream!=null){
                    inputStream.close();
                }
                if(outputStream!=null){
                    outputStream.close();
                }
            }
        }catch (IOException e){
            return  false;
        }
    }
    private long getIdBook(){
        Intent intent=getIntent();
        int idBook=intent.getIntExtra(ID_BOOK,0);
        Toast.makeText(mContext,"id"+idBook,Toast.LENGTH_SHORT).show();
        Long id=Long.parseLong(String.valueOf(idBook));
        return id;
    }
    private void getBook(){
        Retrofit retrofit=OkHttpHelper.getRetrofitToken(getToken());
        ApiInterface apiInterface=retrofit.create(ApiInterface.class);
        Call<BookDetails> call=apiInterface.getBook(getIdBook());
        call.enqueue(new Callback<BookDetails>() {
            @Override
            public void onResponse(Call<BookDetails> call, retrofit2.Response<BookDetails> response) {
                   if(response.code()==200){
                     setBookInformation(response.body().getTitle(),response.body().getAuthorLastName(),response.body().getAuthorFirstName(),
                             response.body().getPages(),response.body().getYearOfPublishing(),response.body().getGenreName(),
                             response.body().getDescription(),response.body().getCreatedBy(),response.body().getId());
                   }
            }

            @Override
            public void onFailure(Call<BookDetails> call, Throwable t) {

            }
        });
    }
    private void setBookInformation(String nameBook,String lastName,String firstName,Integer page, Integer datePublication,String genre,String discription, String createdBy,Integer id){
     txtNameBook.setText(nameBook);
     txtLastName.setText(lastName);
     txtFirstName.setText(firstName);
     txtGenre.setText(genre);
     String pageString=getString(R.string.book_page,page);
     txtPage.setText(pageString);
     String dateString=getString(R.string.book_date_publication,datePublication);
     txtDatePublication.setText(dateString);
     txtDiscription.setText(discription);
     String createdString=getString(R.string.add_user,createdBy);
     txtCreatedBy.setText(createdString);
     nameFile=nameBook+id+".pdf";

    }
    private void createFavoriteBook(AddFavorite addFavorite){
        Retrofit retrofit=OkHttpHelper.getRetrofitToken(getToken());
        ApiInterface apiInterface=retrofit.create(ApiInterface.class);
        Call<AddFavorite> call=apiInterface.createFavoriteBook(addFavorite);
        call.enqueue(new Callback<AddFavorite>() {
            @Override
            public void onResponse(Call<AddFavorite> call, retrofit2.Response<AddFavorite> response) {
                if(response.code()==201){
                    Toast.makeText(mContext,"Книга добавлена в избранное ",Toast.LENGTH_SHORT).show();
                    idFavorite=response.body().getId();
                }
            }

            @Override
            public void onFailure(Call<AddFavorite> call, Throwable t) {
                  Toast.makeText(mContext,"Произошла ошибка",Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void deleteFavoriteBook(int id){
            Long idFavorite=Long.parseLong(String.valueOf(id));
            Retrofit retrofit=OkHttpHelper.getRetrofitToken(getToken());
            ApiInterface apiInterface=retrofit.create(ApiInterface.class);
            Call<ResponseBody> call=apiInterface.deleteFavoriteBook(idFavorite);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    Toast.makeText(mContext,"responce"+response.code(),Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
    }
    private void clickListenner(){
        downlaodBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Details","Enter");
                downloadBook();
            }
        });
        final Integer id=Integer.parseInt(String.valueOf(getIdBook()));
        addFavoritesBook=(LikeButton) findViewById(R.id.hear_button);
        addFavoritesBook.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                AddFavorite addFavorite=new AddFavorite(id,1001);
                createFavoriteBook(addFavorite);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                deleteFavoriteBook(idFavorite);
            }
        });
    }
}
