package hackathon.elibrary.Search;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import hackathon.elibrary.Book.DetailsBook;
import hackathon.elibrary.POJO.Book;
import hackathon.elibrary.POJO.Genre;
import hackathon.elibrary.R;
import hackathon.elibrary.Reader.ReaderActivity;
import hackathon.elibrary.Util.ApiInterface;
import hackathon.elibrary.Util.BookRecyclerAdapter;
import hackathon.elibrary.Util.BottomNavigationSetupOptions;
import hackathon.elibrary.Util.GenreRecyclerAdapter;
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
    private static final int SEARCH_BY_TITLE=0;
    private static final int SEARCH_BY_FIRST_NAME=1;
    private static final int SEARCH_BY_LAST_NAME=1;



    private RecyclerView recyclerView,recyclerGenre;
    private BookRecyclerAdapter bookRecyclerAdapter;
    private GenreRecyclerAdapter genreRecyclerAdapter;
    private Context mContext=SearchActivty.this;
    private ProgressBar progressBar;
    private SearchView searchView;
    private ArrayList<Book> arrayListBook;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupNavigation();
        getAllGenre();
        getAllBooks();
        setupView();
        setupTypeFiltrBook();

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
        Call<ArrayList<Book>> call=apiInterface.getAllBooks(true);
        call.enqueue(new Callback<ArrayList<Book>>() {
            @Override
            public void onResponse(Call<ArrayList<Book>> call, Response<ArrayList<Book>> response) {
                if(response.code()==200){
                    setupRecyclerView(response.body(),mContext);
                    progressBar.setVisibility(View.INVISIBLE);
                    searchBooksByTitle(response.body());
                    arrayListBook=response.body();
                    }
            }

            @Override
            public void onFailure(Call<ArrayList<Book>> call, Throwable t) {

            }
        });
    }
    private void getAllGenre(){
        Retrofit retrofit=OkHttpHelper.getRetrofitToken(getToken());
        ApiInterface apiInterface=retrofit.create(ApiInterface.class);
        Call<List<Genre>> call=apiInterface.getAllGenre();
        call.enqueue(new Callback<List<Genre>>() {
            @Override
            public void onResponse(Call<List<Genre>> call, Response<List<Genre>> response) {
                if(response.code()==200){
                    setupGenreRecycler(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Genre>> call, Throwable t) {

            }
        });
    }
    private String getToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("myToken", Context.MODE_PRIVATE);
        return sharedPreferences.getString(SAVE_TOKEN, "");
    }
    private void setupRecyclerView(final ArrayList<Book> listBook, Context context) {
        recyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        bookRecyclerAdapter = new BookRecyclerAdapter(listBook, context);
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
        searchView=(SearchView) findViewById(R.id.search_book);
        progressBar=(ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
    }
    private void setupGenreRecycler(final List<Genre> genreList){
      recyclerGenre=(RecyclerView) findViewById(R.id.genre_recycler);
      genreRecyclerAdapter=new GenreRecyclerAdapter(genreList,mContext);
      RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false);
      recyclerGenre.setLayoutManager(layoutManager);
      recyclerGenre.setAdapter(genreRecyclerAdapter);
      recyclerGenre.addOnItemTouchListener(new RecyclerItemClickListener(mContext, recyclerGenre, new RecyclerItemClickListener.OnItemClickListener() {
          @Override
          public void onItemClick(View view, int position) {
               if(genreList.get(position).getId()==1851){
                   getAllBooks();
                   progressBar.setVisibility(View.VISIBLE);
                   Toast.makeText(mContext,"Все книги",Toast.LENGTH_SHORT).show();
               }else{
                   getBookGenre(genreList.get(position).getId());
                   progressBar.setVisibility(View.VISIBLE);
                   Toast.makeText(mContext,"Выбран жанр-"+genreList.get(position).getName(),Toast.LENGTH_SHORT).show();
               }
          }

          @Override
          public void onLongItemClick(View view, int position) {

          }
      }));

    }
    private void getBookGenre(Integer id){
        long idLong=Long.parseLong(String.valueOf(id));
        Retrofit retrofit=OkHttpHelper.getRetrofitToken(getToken());
        ApiInterface apiInterface=retrofit.create(ApiInterface.class);
        Call<ArrayList<Book>> call=apiInterface.getBookFiltrGenre(true,idLong);
        call.enqueue(new Callback<ArrayList<Book>>() {
            @Override
            public void onResponse(Call<ArrayList<Book>> call, Response<ArrayList<Book>> response) {
                if(response.code()==200) {
                    progressBar.setVisibility(View.INVISIBLE);
                    setupRecyclerView(response.body(), mContext);
                    arrayListBook=response.body();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Book>> call, Throwable t) {

            }
        });
    }
    private void searchBooksByTitle(final ArrayList<Book> arrayList){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText=newText.toLowerCase();
                ArrayList<Book> newList=new ArrayList<>();
                for(Book book:arrayList){
                    String nameBook=book.getTitle().toLowerCase();
                    if(nameBook.contains(newText)) {
                        newList.add(book);
                    }
                }
                bookRecyclerAdapter.setFiltrBook(newList);
                return true;
            }
        });
    }
    private void searchBooksByFirstNameAvtor(final ArrayList<Book> arrayList){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText=newText.toLowerCase();
                ArrayList<Book> newList=new ArrayList<>();
                for(Book book:arrayList){
                    String avtorFirstName=book.getAuthorFirstName().toLowerCase();
                    if(avtorFirstName.contains(newText)) {
                        newList.add(book);
                    }
                }
                bookRecyclerAdapter.setFiltrBook(newList);
                return true;
            }
        });
    }
    private void searchBooksByLastNameAvtor(final  ArrayList<Book> arrayList){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText=newText.toLowerCase();
                ArrayList<Book> newList=new ArrayList<>();
                for(Book book:arrayList){
                    String avtorLastName=book.getAuthorLastName().toLowerCase();
                    if(avtorLastName.contains(newText)) {
                        newList.add(book);
                    }
                }
                bookRecyclerAdapter.setFiltrBook(newList);
                return true;
            }
        });
    }
    private void setupTypeFiltrBook(){
        ImageView imageAlert=(ImageView) findViewById(R.id.menu_filtr);
        imageAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        builder.setTitle("Выберите тип поиска");
        builder.setSingleChoiceItems(R.array.serch_title_array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        searchBooksByTitle(arrayListBook);
                    case 1:
                        searchBooksByFirstNameAvtor(arrayListBook);
                    case 2:
                        searchBooksByLastNameAvtor(arrayListBook);
                }

            }
        });
        builder.setPositiveButton("Готово", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog mDialog=builder.create();
        mDialog.show();
            }
        });
    }
}
