package hackathon.elibrary.Profile;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import hackathon.elibrary.Book.DetailsBook;
import hackathon.elibrary.Book.DetailsBookProfil;
import hackathon.elibrary.MyDataBase.DatabaseHelper;
import hackathon.elibrary.POJO.Book;
import hackathon.elibrary.R;
import hackathon.elibrary.Util.BookUtils;
import hackathon.elibrary.Util.DownloadBookRecyclerAdapter;
import hackathon.elibrary.Util.RecyclerItemClickListener;

public class DownloadedBookFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DownloadBookRecyclerAdapter adapter;
    private ArrayList<BookUtils> bookDownloadList=new ArrayList<>();

    private static final String ID_BOOK_SQL = "idBookSql";



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_downloader_book,container,false);
        setupDownloadList(view);
        return view;
    }
    private void setupDownloadList(View view){
        DatabaseHelper databaseHelper=new DatabaseHelper(getActivity());
        SQLiteDatabase sqLiteDatabase=databaseHelper.getReadableDatabase();
        Cursor cursor=databaseHelper.getInformation(sqLiteDatabase);
        if( cursor != null && cursor.moveToFirst() ) {
            do {
                BookUtils bookUtils = new BookUtils(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4));
                bookDownloadList.add(bookUtils);

            } while (cursor.moveToNext());
        }
        recyclerView=(RecyclerView) view.findViewById(R.id.download_recycler_view);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter=new DownloadBookRecyclerAdapter(bookDownloadList);
        recyclerView.setAdapter(adapter);
        databaseHelper.close();
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int id=bookDownloadList.get(position).getId();
                Intent intent=new Intent(getActivity(),DetailsBookProfil.class);
                intent.putExtra(ID_BOOK_SQL,id);
                startActivity(intent);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }
}
