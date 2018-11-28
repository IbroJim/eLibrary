package hackathon.elibrary.Book;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.like.LikeButton;
import com.like.OnLikeListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

import hackathon.elibrary.MyDataBase.BookSchema.BookTable;
import hackathon.elibrary.MyDataBase.DatabaseHelper;
import hackathon.elibrary.POJO.Favorite;
import hackathon.elibrary.POJO.BookDetails;
import hackathon.elibrary.POJO.FavoriteBook;
import hackathon.elibrary.R;
import hackathon.elibrary.Reader.ReaderActivity;
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

public class DetailsBook extends AppCompatActivity implements View.OnClickListener {

    private static final String SAVE_TOKEN = "saveToken";
    private static final String BASE_URL="https://elibrary-app.herokuapp.com/#/docs/";
    private static final String ID_BOOK = "idBook";
    private static final String TAG = "DetailsBook";
    private static final String SAVE_PROFILE = "profileID";
    private static final String NAME_FILE_PDF = "namePdfFile";


    private Button downlaodBookButton;
    private LikeButton addFavoritesBook;
    private String nameFile;
    private Context mContext=DetailsBook.this;
    private TextView txtNameBook,txtLastName,txtFirstName,txtPage,txtDatePublication,txtGenre,txtDiscription,txtCreatedBy;
    private int idFavorite;
    private ImageView imageCover;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;
    private ContentValues contentValues;
    private BookDetails bookDetails;
    private Button buttonRead;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        setupView();
        clickListenner();
        getBook();
        setupDatabase();
        checkBookDownload();

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
        imageCover=(ImageView) findViewById(R.id.cover_book_image);
        buttonRead=(Button) findViewById(R.id.read_book);
        addFavoritesBook=(LikeButton) findViewById(R.id.hear_button);
        buttonRead.setVisibility(View.INVISIBLE);
        buttonRead.setOnClickListener(this);
        downlaodBookButton.setOnClickListener(this);
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
                Log.d("Details","is "+response.code());
                if (response.code() == 200) {
                    assert response.body() != null;
                    boolean succes = writeResponceBodyToDisk(nameFile, response.body());
                    downloadInformation(nameFile);
                    Log.d("Details","is "+succes);
                    if(succes==true){
                        buttonRead.setVisibility(View.VISIBLE);
                    }
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
                       bookDetails = new BookDetails();
                               bookDetails.setId(response.body().getId());
                               bookDetails.setTitle(response.body().getTitle());
                               bookDetails.setDescription(response.body().getDescription());
                               bookDetails.setPages(response.body().getPages());
                               bookDetails.setCreatedBy(response.body().getCreatedBy());
                               bookDetails.setYearOfPublishing(response.body().getYearOfPublishing());
                               bookDetails.setAuthorFirstName(response.body().getAuthorFirstName());
                               bookDetails.setAuthorLastName(response.body().getAuthorLastName());
                               bookDetails.setGenreName(response.body().getGenreName());
                               downloadCover();
                               setBookInformation();
                               chekedFavorite(response.body().getId());

                   }
            }

            @Override
            public void onFailure(Call<BookDetails> call, Throwable t) {

            }
        });
    }
    private void setBookInformation(){
        txtNameBook.setText(bookDetails.getTitle());
        txtLastName.setText(bookDetails.getAuthorLastName());
        txtFirstName.setText(bookDetails.getAuthorFirstName());
        txtGenre.setText(bookDetails.getGenreName());
        String pageString=getString(R.string.book_page,bookDetails.getPages());
        txtPage.setText(pageString);
        String dateString=getString(R.string.book_date_publication,bookDetails.getYearOfPublishing());
        txtDatePublication.setText(dateString);
        txtDiscription.setText(bookDetails.getDescription());
        String createdString=getString(R.string.add_user,bookDetails.getCreatedBy());
        txtCreatedBy.setText(createdString);
        nameFile="book"+bookDetails.getId()+".pdf";


    }
    private void createFavoriteBook(Favorite addFavorite){
        Retrofit retrofit=OkHttpHelper.getRetrofitToken(getToken());
        ApiInterface apiInterface=retrofit.create(ApiInterface.class);
        Call<Favorite> call=apiInterface.createFavoriteBook(addFavorite);
        call.enqueue(new Callback<Favorite>() {
            @Override
            public void onResponse(Call<Favorite> call, retrofit2.Response<Favorite> response) {
                if(response.code()==201){
                    Toast.makeText(mContext,"Книга добавлена в избранное ",Toast.LENGTH_SHORT).show();
                    idFavorite=response.body().getId();
                }
            }

            @Override
            public void onFailure(Call<Favorite> call, Throwable t) {
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
        final Integer id=Integer.parseInt(String.valueOf(getIdBook()));

        addFavoritesBook.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                Favorite addFavorite=new Favorite(id,getProfileId());
                createFavoriteBook(addFavorite);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                deleteFavoriteBook(idFavorite);
            }
        });

    }
    private void downloadCover(){
            Retrofit retrofit=OkHttpHelper.getRetrofitToken(getToken());
            ApiInterface apiInterface=retrofit.create(ApiInterface.class);
            Call<ResponseBody> call=apiInterface.downloadCover(getIdBook());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    if(response.code()==200){
                        try {
                            InputStream byteArrayInputStream= new ByteArrayInputStream(response.body() != null ? response.body().bytes() : new byte[0]);
                            ByteArrayOutputStream buffer=new ByteArrayOutputStream();
                            int inRead;
                            byte[] data=new byte[4096];
                            while ((inRead=byteArrayInputStream.read(data,0,data.length))!=-1){
                                buffer.write(data,0,inRead);
                            }

                            buffer.flush();
                            byte[] byteArray=buffer.toByteArray();
                            buffer.close();
                            Bitmap bitmap=BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
                            if(bitmap!=null) {
                                Bitmap bitmapImage = Bitmap.createScaledBitmap(bitmap, imageCover.getWidth(), imageCover.getHeight(), true);
                                imageCover.setImageBitmap(bitmapImage);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    private void downloadInformation(String nameFile){
       contentValues.put(BookTable.NameOfFields.ID_BOOK,bookDetails.getId());
       contentValues.put(BookTable.NameOfFields.FIRST_NAME,bookDetails.getAuthorFirstName());
       contentValues.put(BookTable.NameOfFields.LAST_NAME, bookDetails.getAuthorLastName());
       contentValues.put(BookTable.NameOfFields.TITLE,bookDetails.getTitle());
       contentValues.put(BookTable.NameOfFields.PAGE,bookDetails.getPages());
       contentValues.put(BookTable.NameOfFields.DATE_PIBLICATION,bookDetails.getYearOfPublishing());
       contentValues.put(BookTable.NameOfFields.CREATED_BY,bookDetails.getCreatedBy());
       contentValues.put(BookTable.NameOfFields.GENRE,bookDetails.getGenreName());
       contentValues.put(BookTable.NameOfFields.DISCRIPTION,bookDetails.getDescription());
       contentValues.put(BookTable.NameOfFields.NAME_FILE,nameFile);
       sqLiteDatabase.insert(BookTable.NAME_TABLE_TWO,null,contentValues);
        Cursor cursor=sqLiteDatabase.query(BookTable.NAME_TABLE_TWO,null,null,null,null,null,null,null);
            if (cursor.moveToFirst()){
                int id=cursor.getColumnIndex(BookTable.NameOfFields.ID);
                int firstName=cursor.getColumnIndex(BookTable.NameOfFields.FIRST_NAME);
                int lastName=cursor.getColumnIndex(BookTable.NameOfFields.LAST_NAME);
                int title=cursor.getColumnIndex(BookTable.NameOfFields.TITLE);
                int page=cursor.getColumnIndex(BookTable.NameOfFields.PAGE);
                int datePublication=cursor.getColumnIndex(BookTable.NameOfFields.DATE_PIBLICATION);
                int createdBy=cursor.getColumnIndex(BookTable.NameOfFields.CREATED_BY);
                int genre=cursor.getColumnIndex(BookTable.NameOfFields.GENRE);
                int discripon=cursor.getColumnIndex(BookTable.NameOfFields.DISCRIPTION);
                int namePdf=cursor.getColumnIndex(BookTable.NameOfFields.NAME_FILE);
                Log.d("Details","id "+cursor.getString(id)+" "+
                                            "firtName "+cursor.getString(firstName)+" "+
                "lastName "+cursor.getString(lastName)+" "+
                "title "+cursor.getString(title)+" "+"page "+cursor.getString(page)+" datePublication "+cursor.getString(datePublication)+
                " createdBy "+cursor.getString(createdBy)+" genre "+cursor.getString(genre)+" discription "+cursor.getString(discripon)+" name PDf "+cursor.getString(namePdf));
            }

    }
    private void  setupDatabase(){
        databaseHelper=new DatabaseHelper(mContext);
        sqLiteDatabase=databaseHelper.getWritableDatabase();
        contentValues=new ContentValues();
    }
    private Integer getProfileId(){
        SharedPreferences sharedPreferences=getSharedPreferences("profileId",Context.MODE_PRIVATE);
        return sharedPreferences.getInt(SAVE_PROFILE,0);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.download_books:
                Log.d("Details","Enter");
                Cursor cursor=sqLiteDatabase.query(BookTable.NAME_TABLE_TWO,new String[]{BookTable.NameOfFields.ID_BOOK},"idBook=?",
                        new String[]{String.valueOf(getIdBook())},null,null,null,null);
                if(cursor.moveToFirst()){
                    do{
                        buttonRead.setVisibility(View.VISIBLE);
                        Toast.makeText(mContext,"Эта книга уже скачена",Toast.LENGTH_SHORT).show();
                    }while (cursor.moveToNext());
                }else {  downloadBook();}
                break;
            case R.id.read_book:
                Intent intent=new Intent(mContext,ReaderActivity.class);
                intent.putExtra(NAME_FILE_PDF,nameFile);
                startActivity(intent);
                break;
        }
    }
    private void checkBookDownload(){
        Log.d("Details","Enter");
        Cursor cursor=sqLiteDatabase.query(BookTable.NAME_TABLE_TWO,new String[]{BookTable.NameOfFields.ID_BOOK},"idBook=?",
                new String[]{String.valueOf(getIdBook())},null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                buttonRead.setVisibility(View.VISIBLE);
                Toast.makeText(mContext,"Эта книга уже скачена",Toast.LENGTH_SHORT).show();
            }while (cursor.moveToNext());
        }else {
        }
    }
    private void chekedFavorite(Integer idBook){
        Integer  idProfile=getProfileId();
        long idLongProfile=Long.parseLong(String.valueOf(idProfile));
        long idLongBook=Long.parseLong(String.valueOf(idBook));
        Retrofit retrofit=OkHttpHelper.getRetrofitToken(getToken());
        ApiInterface apiInterface=retrofit.create(ApiInterface.class);
        Call<ArrayList<FavoriteBook>> call=apiInterface.checkFavoriteBook(idLongBook,idLongProfile);
        call.enqueue(new Callback<ArrayList<FavoriteBook>>() {
            @Override
            public void onResponse(Call<ArrayList<FavoriteBook>> call, retrofit2.Response<ArrayList<FavoriteBook>> response) {
                Toast.makeText(mContext,"responce"+response.code(),Toast.LENGTH_SHORT).show();
                if(response.code()==200){
                    addFavoritesBook.setLiked(true);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FavoriteBook>> call, Throwable t) {
                 Toast.makeText(mContext,"Fail",Toast.LENGTH_SHORT).show();
            }
        });
    }
}

