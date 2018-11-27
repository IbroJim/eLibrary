package hackathon.elibrary.Search;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import hackathon.elibrary.Book.DetailsBook;
import hackathon.elibrary.POJO.Book;
import hackathon.elibrary.R;
import hackathon.elibrary.Reader.ReaderActivity;
import hackathon.elibrary.Util.ApiInterface;
import hackathon.elibrary.Util.BookRecyclerAdapter;
import hackathon.elibrary.Util.BottomNavigationSetupOptions;
import hackathon.elibrary.Util.OkHttpHelper;
import hackathon.elibrary.Util.RecyclerItemClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchActivty extends AppCompatActivity {

    private static final String SAVE_TOKEN = "saveToken";
    private static final String ID_BOOK = "idBook";
    private static final int ACTIVITY_NUM=2;



    private RecyclerView recyclerView;
    private BookRecyclerAdapter bookRecyclerAdapter;
    private Context mContext=SearchActivty.this;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupNavigation();
        getAllBooks();
        setupView();


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
        Retrofit retrofit=OkHttpHelper.getRetrofitToken(getToken());
        ApiInterface apiInterface=retrofit.create(ApiInterface.class);
        Call<ArrayList<Book>> call=apiInterface.getAllBooks();
        call.enqueue(new Callback<ArrayList<Book>>() {
            @Override
            public void onResponse(Call<ArrayList<Book>> call, Response<ArrayList<Book>> response) {
                if(response.code()==200){
                    setupRecyclerView(response.body(),mContext);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Book>> call, Throwable t) {

            }
        });
    }
    private String getToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("myToken", Context.MODE_PRIVATE);
        return sharedPreferences.getString(SAVE_TOKEN, "");
    }
    private void setupRecyclerView(final ArrayList<Book> listBook, Context context) {
        recyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
        bookRecyclerAdapter = new BookRecyclerAdapter(listBook, context);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(bookRecyclerAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int id=listBook.get(position).getId();
                Intent intent=new Intent(mContext,DetailsBook.class);
                intent.putExtra(ID_BOOK,id);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }
    private void setupView(){
        progressBar=(ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
    }


}
