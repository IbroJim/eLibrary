package hackathon.elibrary.Book;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import hackathon.elibrary.POJO.CreateBook;
import hackathon.elibrary.POJO.Genre;
import hackathon.elibrary.POJO.Profile;
import hackathon.elibrary.R;
import hackathon.elibrary.Util.ApiInterface;
import hackathon.elibrary.Util.OkHttpHelper;
import hackathon.elibrary.Util.SpinnerGenreAdapter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewBook extends AppCompatActivity implements View.OnClickListener {

    public  static final int FILE_PICKER_REQUEST_CODE_PDF = 1;
    public  static final int FILE_PICKER_REQUEST_CODE_IMAGE = 2;
    private static final String ACCOUNT_ID = "MyAccountId";
    private static final String SAVE_TOKEN = "saveToken";
    private static final String SAVE_PROFILE = "profileID";

    private Spinner spinner;
    private SpinnerGenreAdapter mAdapter;
    private Context mContext = NewBook.this;
    private Button selectFile, upload,selecetImage;
    private EditText titleBook,lastNameAvtor, firstNameAvtor, description,page,datePublication;
    private Integer genreId;
    private ImageView imageCover,checkMarkImage,checkMarkPdf;
    private String imagePath,pdfPath,token;
    private long idBook;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);
        takeProfileId();
        getAllGenre();
        setupView();
     token=getToken();
    }
    private void setupView() {
        selectFile=(Button)findViewById(R.id.select_pdf);
        selectFile.setOnClickListener(this);
        upload=(Button) findViewById(R.id.upload_book);
        upload.setOnClickListener(this);
        selecetImage=(Button) findViewById(R.id.select_image_cover);
        selecetImage.setOnClickListener(this);
        imageCover=(ImageView) findViewById(R.id.cover_book_image);
        titleBook=(EditText) findViewById(R.id.name_book);
        lastNameAvtor=(EditText) findViewById(R.id.last_name_avtor);
        firstNameAvtor=(EditText) findViewById(R.id.fisrt_name_avtor);
        page=(EditText) findViewById(R.id.pages);
        datePublication=(EditText) findViewById(R.id.date_publication);
        description=(EditText) findViewById(R.id.description_edit);
        checkMarkImage=(ImageView) findViewById(R.id.check_mark_image);
        checkMarkPdf=(ImageView) findViewById(R.id.chek_mark_pdf);
        checkMarkPdf.setVisibility(View.INVISIBLE);
        checkMarkImage.setVisibility(View.INVISIBLE);
        progressBar=(ProgressBar) findViewById(R.id.progress_new_book);
        progressBar.setVisibility(View.INVISIBLE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==FILE_PICKER_REQUEST_CODE_PDF&&resultCode==RESULT_OK){
            String path=data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
           checkMarkPdf.setVisibility(View.VISIBLE);
            if(path!=null){
                Log.d("Path: ", path);
                pdfPath = path;
                Toast.makeText(this, "Picked file: " + path, Toast.LENGTH_LONG).show();
            }
        }
        if(requestCode==FILE_PICKER_REQUEST_CODE_IMAGE&&resultCode==RESULT_OK&& data!=null){
            String path=data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            checkMarkImage.setVisibility(View.VISIBLE);
            imageCover.setImageBitmap(BitmapFactory.decodeFile(path));
            if(path!=null){
                Log.d("Path: ", path);
                imagePath = path;
                Toast.makeText(this, "Picked file: " + path, Toast.LENGTH_LONG).show();
            }


            }



        }
    private void takeProfileId() {
        String token = getToken();
        Integer userId = getId();
        long longs = (long) (userId);
        Retrofit retrofit = OkHttpHelper.getRetrofitToken(token);
        ApiInterface client = retrofit.create(ApiInterface.class);
        Call<Profile> call = client.getProfileId(longs);
        Log.d("NewBook", "Hello" + longs);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                saveIdProfile(response.body().getId());
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {

            }
        });

    }
    private String getToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("myToken", Context.MODE_PRIVATE);
        return sharedPreferences.getString(SAVE_TOKEN, "");
    }
    private Integer getId() {
        SharedPreferences sharedPreferences = getSharedPreferences("myAccountId", Context.MODE_PRIVATE);
        return sharedPreferences.getInt(ACCOUNT_ID, 0);
    }
    private void saveIdProfile(Integer id) {
        SharedPreferences sharedPreferences = getSharedPreferences("profileId", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SAVE_PROFILE, id);
        editor.apply();

    }
    private Integer getProfileId(){
        SharedPreferences sharedPreferences=getSharedPreferences("profileId",Context.MODE_PRIVATE);
        return sharedPreferences.getInt(SAVE_PROFILE,0);
    }
    private void getAllGenre() {

        String token = getToken();
        Retrofit retrofit = OkHttpHelper.getRetrofitToken(token);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<List<Genre>> listCall = apiInterface.getAllGenre();
        listCall.enqueue(new Callback<List<Genre>>() {
            @Override
            public void onResponse(Call<List<Genre>> call, Response<List<Genre>> response) {
                if (response.code() == 200) {
                    setupSpinnerAdapter(response.body());
                    Log.d("Home", "200 good");
                }
            }

            @Override
            public void onFailure(Call<List<Genre>> call, Throwable t) {

            }
        });
    }
    private void setupSpinnerAdapter(List<Genre> listCall) {
        spinner = (Spinner) findViewById(R.id.genre_spinner);
        mAdapter = new SpinnerGenreAdapter(mContext, listCall);
        spinner.setAdapter(mAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Genre clikedGenre = (Genre) parent.getItemAtPosition(position);
                genreId = clikedGenre.getId();
                Toast.makeText(mContext, "Genre id: " + genreId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void createBookRetrofit(CreateBook createBook){
        String token=getToken();
        Retrofit retrofit=OkHttpHelper.getRetrofitToken(token);
        ApiInterface apiInterface=retrofit.create(ApiInterface.class);
        Call<CreateBook> call=apiInterface.createBook(createBook);
        call.enqueue(new Callback<CreateBook>() {
            @Override
            public void onResponse(Call<CreateBook> call, Response<CreateBook> response) {
                if(response.code()==201){
                    String id= String.valueOf(response.body().getId());
                    idBook=Long.parseLong(id);
                    Log.d("Home","Text data upload! Responce"+idBook);
                 if(imagePath!=null){
                     if (pdfPath!=null){
                         uploadSelectPdf();
                     }else {
                         Toast.makeText(mContext,"Выберете pdf-ку",Toast.LENGTH_SHORT).show();
                     }
                 }else
                     Toast.makeText(mContext,"Выберете картинку",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<CreateBook> call, Throwable t) {

            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_pdf:
               launchPickerPdf();
                break;
            case R.id.upload_book:
                if (titleBook.getText().length()!=0 && description.getText().length()!=0 && lastNameAvtor.getText().length()!=0  && firstNameAvtor.getText().length()!=0  && page.getText().length()!=0  && datePublication.getText().length()!=0 ) {
                    progressBar.setVisibility(View.VISIBLE);
                    uploadBookInformation();
                }
                else {
                    Toast.makeText(mContext,"Введите данные",Toast.LENGTH_SHORT).show();
                }
                break;
            case  R.id.select_image_cover:
                launchPickerImage();
                break;

        }
    }
    private void uploadBookInformation() {

            Integer profileId = getProfileId();
            String value = page.getText().toString();
            Integer pageInteger = Integer.parseInt(value);

            String valueDatePublication = datePublication.getText().toString();
            Integer datePublication = Integer.parseInt(valueDatePublication);
            CreateBook createBook = new CreateBook(false, firstNameAvtor.getText().toString(), lastNameAvtor.getText().toString()
                    , description.getText().toString(), genreId, pageInteger, profileId, titleBook.getText().toString(), datePublication);
            createBookRetrofit(createBook);



    }
    private void uploadSelectPdf(){
        if(pdfPath==null){
            Toast.makeText(this, "please select an image ", Toast.LENGTH_LONG).show();
        }

        File file=new File(pdfPath);
        String type="book";
       Retrofit retrofit=OkHttpHelper.multipartRetrofitTokent(token);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/pdf"), file);
        ApiInterface apiInterface=retrofit.create(ApiInterface.class);
        Call<ResponseBody> call=apiInterface.uploadBookFile(requestBody,idBook,type);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200){
                    Log.d("Home","Book upload");
                    uploadSelectImage();
                }else {
                }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    private void uploadSelectImage (){
        if(imagePath==null) {
            Toast.makeText(this, "please select an image ", Toast.LENGTH_LONG).show();
        }
        File file=new File(imagePath);
        String type="cover";
      Retrofit retrofit=OkHttpHelper.multipartRetrofitTokent(token);
        final RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        ApiInterface apiInterface=retrofit.create(ApiInterface.class);
        Call<ResponseBody> call=apiInterface.uploadImageFile(requestBody,idBook,type);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.d("Home","responce"+response.code());
                if(response.code()==200){
                Log.d("Home ","Image uplaod");

           }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
    private void launchPickerPdf() {
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(FILE_PICKER_REQUEST_CODE_PDF)
                .withHiddenFiles(true)
                .withFilter(Pattern.compile(".*\\.pdf$"))
                .withTitle("Select PDF file")
                .start();
    }
    private void launchPickerImage() {
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(FILE_PICKER_REQUEST_CODE_IMAGE)
                .withHiddenFiles(true)
                .withFilter(Pattern.compile(".*\\.jpg$"))
                .withTitle("Select jpg file")
                .start();
    }
}





