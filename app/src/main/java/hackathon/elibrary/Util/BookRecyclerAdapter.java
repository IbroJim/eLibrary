package hackathon.elibrary.Util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import hackathon.elibrary.POJO.Book;
import hackathon.elibrary.R;

public class BookRecyclerAdapter extends RecyclerView.Adapter<BookRecyclerAdapter.BookViewHolder>{



    private ArrayList<Book> bookList;
    private Context mContext;

    public BookRecyclerAdapter(ArrayList<Book> listBook,Context context){
        this.mContext=context;
        this.bookList=listBook;
    }


    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater=LayoutInflater.from(viewGroup.getContext());
        View view=layoutInflater.inflate(R.layout.search_card_layout,viewGroup,false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder bookViewHolder, int position) {
        bookViewHolder.txtNameBook.setText(bookList.get(position).getTitle());
        bookViewHolder.txtFirstName.setText(bookList.get(position).getAuthorFirstName());
        bookViewHolder.txtLastNameAvtor.setText(bookList.get(position).getAuthorLastName());
        bookViewHolder.txtGenreBook.setText(bookList.get(position).getGenreName());
        Integer pagesInteger=bookList.get(position).getPages();
        String pageString= mContext.getString(R.string.book_page,pagesInteger);
        bookViewHolder.txtPageBook.setText(pageString);
        Integer datePublicationInteger=bookList.get(position).getYearOfPublishing();
        String dateString=mContext.getString(R.string.book_date_publication,datePublicationInteger);
        bookViewHolder.txtDatePublication.setText(dateString);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }


    public class BookViewHolder extends  RecyclerView.ViewHolder{

        private TextView txtNameBook, txtLastNameAvtor,txtFirstName,txtPageBook,txtDatePublication,txtGenreBook;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameBook=(TextView) itemView.findViewById(R.id.name_book);
            txtLastNameAvtor=(TextView) itemView.findViewById(R.id.last_name_avtor);
            txtFirstName=(TextView) itemView.findViewById(R.id.first_name_avtor);
            txtPageBook=(TextView) itemView.findViewById(R.id.book_page);
            txtDatePublication=(TextView) itemView.findViewById(R.id.date_publication);
            txtGenreBook=(TextView) itemView.findViewById(R.id.book_genre);
        }
    }
}
