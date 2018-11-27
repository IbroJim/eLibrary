package hackathon.elibrary.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import hackathon.elibrary.Book.DetailsBook;
import hackathon.elibrary.POJO.FavoriteBook;
import hackathon.elibrary.R;
import hackathon.elibrary.Util.ApiInterface;
import hackathon.elibrary.Util.FavoriteBookRecyclerAdapter;
import hackathon.elibrary.Util.OkHttpHelper;
import hackathon.elibrary.Util.RecyclerItemClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReadBookFragment extends Fragment {

    private static final String SAVE_TOKEN = "saveToken";
    private static final String ID_BOOK = "idBook";

    private ProgressBar progressBar;
    private RecyclerView recyclrView;
    private FavoriteBookRecyclerAdapter bookRecyclerAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_read_book,container,false);
        setupView(view);
        getAllReadBooks();
        return view;
    }
    private void getAllReadBooks(){
        Retrofit retrofit=OkHttpHelper.getRetrofitToken(getToken());
        ApiInterface apiInterface=retrofit.create(ApiInterface.class);
        Call<ArrayList<FavoriteBook>> call=apiInterface.getAllreadBooks();
        call.enqueue(new Callback<ArrayList<FavoriteBook>>() {
            @Override
            public void onResponse(Call<ArrayList<FavoriteBook>> call, Response<ArrayList<FavoriteBook>> response) {
                progressBar.setVisibility(View.INVISIBLE);
                setupReadList(response.body(),getActivity());
            }

            @Override
            public void onFailure(Call<ArrayList<FavoriteBook>> call, Throwable t) {

            }
        });
    }
    private void setupReadList(final ArrayList<FavoriteBook> listBook, Context context){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        bookRecyclerAdapter = new FavoriteBookRecyclerAdapter(listBook, context);
        recyclrView.setLayoutManager(layoutManager);
        recyclrView.setAdapter(bookRecyclerAdapter);
        recyclrView.addOnItemTouchListener(new RecyclerItemClickListener(context, recyclrView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int id=listBook.get(position).getBookId();
                Intent intent=new Intent(getActivity(),DetailsBook.class);
                intent.putExtra(ID_BOOK,id);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

    }
    private String getToken() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myToken", Context.MODE_PRIVATE);
        return sharedPreferences.getString(SAVE_TOKEN, "");
    }
    private void setupView(View view){
        progressBar=(ProgressBar) view.findViewById(R.id.progress_bar);
        recyclrView=(RecyclerView) view.findViewById(R.id.read_recycler_view);
        progressBar.setVisibility(View.VISIBLE);
    }
}
