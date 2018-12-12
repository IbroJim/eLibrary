package hackathon.elibrary.Home;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import hackathon.elibrary.Book.DetailsBook;
import hackathon.elibrary.MyDataBase.DatabaseHelper;
import hackathon.elibrary.MyDataBase.TranslateSchema;
import hackathon.elibrary.MyDataBase.TranslateSchema.TranslateTable;
import hackathon.elibrary.POJO.AccountData;
import hackathon.elibrary.POJO.Book;
import hackathon.elibrary.POJO.HelpBook;
import hackathon.elibrary.POJO.Profile;
import hackathon.elibrary.POJO.Translate;
import hackathon.elibrary.POJO.User;
import hackathon.elibrary.R;

import hackathon.elibrary.Util.ApiInterface;
import hackathon.elibrary.Util.BookRecyclerAdapter;
import hackathon.elibrary.Util.BottomNavigationSetupOptions;
import hackathon.elibrary.Util.HomeAdapterRecycler;
import hackathon.elibrary.Util.HomeNewBookRecyclerAdapter;
import hackathon.elibrary.Util.OkHttpHelper;
import hackathon.elibrary.Util.RecyclerItemClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeActivity extends AppCompatActivity {


    private static final int ACTIVITY_NUM = 1;
    private static final String SAVE_TOKEN = "saveToken";
    private static final String TAG = "HomeActivity";
    private static final String ACCOUNT_ID = "MyAccountId";
    private static final String SAVE_PROFILE = "profileID";
    private static final String ID_BOOK = "idBook";

    private ProgressBar popularProgress, newBooksProgress;
    private Context mContext = HomeActivity.this;
    private RecyclerView popularRecycler, newBookRecycler;
    private HomeAdapterRecycler adapterPopular;
    private HomeNewBookRecyclerAdapter adapterNewBook;
    private DatabaseHelper myDbHelper;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupNavigation();
        setupView();
        createFolder();
        getAccountId();
        getTopBooks();
        getNewBook();
        createDB();
    }

    private void createFolder() {
        File folder = new File(Environment.getExternalStorageDirectory() + "/eLibrary");
        boolean succes = true;
        if (!folder.exists()) {
            succes = folder.mkdir();
        }
        if (succes) {
            Log.d(TAG, "Созданно");
        } else {
            Log.d(TAG, "Ошибка");
        }

    }

    private void setupNavigation() {
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottom_navigatiom_view_id);
        BottomNavigationSetupOptions.setupBottomNavigatiomViewEx(bottomNavigationViewEx);
        BottomNavigationSetupOptions.enableBottomNavigation(bottomNavigationViewEx, mContext, ACTIVITY_NUM);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    private String getToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("myToken", Context.MODE_PRIVATE);
        return sharedPreferences.getString(SAVE_TOKEN, "");
    }

    private void getAccountId() {
        final String token = getToken();
        Retrofit retrofit = OkHttpHelper.getRetrofitToken(token);
        final ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<AccountData> call = apiInterface.getUser();
        call.enqueue(new Callback<AccountData>() {
            @Override
            public void onResponse(Call<AccountData> call, retrofit2.Response<AccountData> response) {

                if (response.code() == 200) {
                    saveAccountId(response.body().getId());
                    takeProfileId(response.body().getId());
                }
            }

            @Override
            public void onFailure(Call<AccountData> call, Throwable t) {
                Log.d(TAG, "No worold");
            }
        });
    }

    private void saveAccountId(Integer id) {
        SharedPreferences sharedPreferences1 = getSharedPreferences("myAccountId", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorInt = sharedPreferences1.edit();
        editorInt.putInt(ACCOUNT_ID, id);
        editorInt.apply();
    }

    private void takeProfileId(Integer idAcc) {
        Long longs = Long.parseLong(String.valueOf(idAcc));
        Retrofit retrofit = OkHttpHelper.getRetrofitToken(getToken());
        ApiInterface client = retrofit.create(ApiInterface.class);
        Call<Profile> call = client.getProfileId(longs);
        Log.d("NewBook", "Hello" + longs);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (response.code() == 200) {
                    saveIdProfile(response.body().getId());
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {

            }
        });

    }

    private void saveIdProfile(Integer id) {
        SharedPreferences sharedPreferences = getSharedPreferences("profileId", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SAVE_PROFILE, id);
        editor.apply();

    }

    private void getTopBooks() {
        Retrofit retrofit = OkHttpHelper.getRetrofitToken(getToken());
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ArrayList<Book>> call = apiInterface.getTopBooks(true);
        call.enqueue(new Callback<ArrayList<Book>>() {
            @Override
            public void onResponse(Call<ArrayList<Book>> call, Response<ArrayList<Book>> response) {
                if (response.code() == 200) {
                    setupRecyclerView(response.body(), mContext);
                    popularProgress.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Book>> call, Throwable t) {

            }
        });
    }

    private void setupView() {
        popularProgress = (ProgressBar) findViewById(R.id.progress_popular);
        popularProgress.setVisibility(View.VISIBLE);
        newBooksProgress = (ProgressBar) findViewById(R.id.progress_new_book);
        newBooksProgress.setVisibility(View.VISIBLE);
    }

    private void setupRecyclerView(final ArrayList<Book> listBook, Context context) {
        popularRecycler = (RecyclerView) findViewById(R.id.popular_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        popularRecycler.setLayoutManager(layoutManager);
        adapterPopular = new HomeAdapterRecycler(listBook, context);
        popularRecycler.setAdapter(adapterPopular);
        popularRecycler.addOnItemTouchListener(new RecyclerItemClickListener(context, popularRecycler, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int id = listBook.get(position).getId();
                Intent intent = new Intent(mContext, DetailsBook.class);
                intent.putExtra(ID_BOOK, id);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    private void setupNewBookRecyclerView(final ArrayList<HelpBook> listBook, Context context) {
        newBookRecycler = (RecyclerView) findViewById(R.id.new_books_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        newBookRecycler.setLayoutManager(layoutManager);
        adapterNewBook = new HomeNewBookRecyclerAdapter(listBook, context);
        newBookRecycler.setAdapter(adapterNewBook);
        newBookRecycler.addOnItemTouchListener(new RecyclerItemClickListener(context, newBookRecycler, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int id = listBook.get(position).getId();
                Intent intent = new Intent(mContext, DetailsBook.class);
                intent.putExtra(ID_BOOK, id);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    private void getNewBook() {
        Date date =new Date();
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DATE,-7);
        date=calendar.getTime();
        String s=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(date);
        Log.d("Details","date"+s);
        Retrofit retrofit = OkHttpHelper.getRetrofitToken(getToken());
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ArrayList<HelpBook>> call = apiInterface.getNewBook(true,s);
        call.enqueue(new Callback<ArrayList<HelpBook>>() {
            @Override
            public void onResponse(Call<ArrayList<HelpBook>> call, Response<ArrayList<HelpBook>> response) {
                Toast.makeText(mContext,"responce"+response.code(),Toast.LENGTH_SHORT).show();
                if (response.code() == 200) {
                    setupNewBookRecyclerView(response.body(), mContext);
                    newBooksProgress.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<HelpBook>> call, Throwable t) {
                Toast.makeText(mContext,"fail",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void createDB() {
            myDbHelper = new DatabaseHelper(this);
            try {
                myDbHelper.createDataBase();
            } catch (IOException ioe) {
                throw new Error("Unable to create database");
            }

            try {
                myDbHelper.openDataBase();
            } catch (SQLException sqle) {
                throw sqle;
            }
    }
}









