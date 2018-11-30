package hackathon.elibrary.Favorites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;


import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

import hackathon.elibrary.Book.DetailsBook;
import hackathon.elibrary.POJO.Book;
import hackathon.elibrary.POJO.FavoriteBook;
import hackathon.elibrary.R;
import hackathon.elibrary.Util.ApiInterface;
import hackathon.elibrary.Util.BookRecyclerAdapter;
import hackathon.elibrary.Util.BottomNavigationSetupOptions;
import hackathon.elibrary.Util.FavoriteBookRecyclerAdapter;
import hackathon.elibrary.Util.OkHttpHelper;
import hackathon.elibrary.Util.RecyclerItemClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FavoritesActivity extends AppCompatActivity {


    private static final String SAVE_TOKEN = "saveToken";
    private static final int ACTIVITY_NUM=0;
    private static final String ID_BOOK = "idBook";
    private static final String SAVE_PROFILE = "profileID";

    private Context mContext=FavoritesActivity.this;
    private ProgressBar progressBar;
    private RecyclerView recyclrView;
    private FavoriteBookRecyclerAdapter bookRecyclerAdapter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_favourites);
        setupNavigation();
        progressBar=(ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        getAllBooks();
    }
    private void setupNavigation(){
        BottomNavigationViewEx bottomNavigationViewEx=(BottomNavigationViewEx) findViewById(R.id.bottom_navigatiom_view_id);
        BottomNavigationSetupOptions.setupBottomNavigatiomViewEx(bottomNavigationViewEx);
        BottomNavigationSetupOptions.enableBottomNavigation(bottomNavigationViewEx,mContext,ACTIVITY_NUM);
        Menu menu=bottomNavigationViewEx.getMenu();
        MenuItem menuItem=menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
    private void getAllBooks(){
        Integer integer=getProfile();
        Long idLong=Long.parseLong(String.valueOf(integer));
        Retrofit retrofit=OkHttpHelper.getRetrofitToken(getToken());
        ApiInterface apiInterface=retrofit.create(ApiInterface.class);
        Call<ArrayList<FavoriteBook>> call=apiInterface.getAllFavoriteBook(idLong);
        call.enqueue(new Callback<ArrayList<FavoriteBook>>() {
            @Override
            public void onResponse(Call<ArrayList<FavoriteBook>> call, Response<ArrayList<FavoriteBook>> response) {
                if(response.code()==200){
                    progressBar.setVisibility(View.INVISIBLE);
                    setupRecyclerView(response.body(),mContext);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FavoriteBook>> call, Throwable t) {

            }
        });

    }
    private String getToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("myToken", Context.MODE_PRIVATE);
        return sharedPreferences.getString(SAVE_TOKEN, "");
    }
    private void setupRecyclerView(final ArrayList<FavoriteBook> listBook, Context context) {
        recyclrView = (RecyclerView) findViewById(R.id.recycler_view);
        bookRecyclerAdapter = new FavoriteBookRecyclerAdapter(listBook, context);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclrView.setLayoutManager(layoutManager);
        recyclrView.setAdapter(bookRecyclerAdapter);
        recyclrView.addOnItemTouchListener(new RecyclerItemClickListener(context, recyclrView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int id=listBook.get(position).getBookId();
                Intent intent=new Intent(mContext,DetailsBook.class);
                intent.putExtra(ID_BOOK,id);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }
    private Integer getProfile(){
        SharedPreferences sharedPreferences = getSharedPreferences("profileId", Context.MODE_PRIVATE);
        return sharedPreferences.getInt(SAVE_PROFILE, 0);
    }

}
