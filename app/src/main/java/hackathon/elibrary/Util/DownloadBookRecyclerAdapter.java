package hackathon.elibrary.Util;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import hackathon.elibrary.R;

public class DownloadBookRecyclerAdapter extends RecyclerView.Adapter<DownloadBookRecyclerAdapter.DownlaodBookViewHolder> {

    private ArrayList<BookUtils> bookDowloadList;
    public DownloadBookRecyclerAdapter(ArrayList<BookUtils> bookUtils) {

        this.bookDowloadList=bookUtils;
    }

    @NonNull
    @Override
    public DownlaodBookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater=LayoutInflater.from(viewGroup.getContext());
        View view=layoutInflater.inflate(R.layout.search_card_layout,viewGroup,false);
        return new DownlaodBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DownlaodBookViewHolder downlaodBookViewHolder, int position) {
        downlaodBookViewHolder.txtNameBook.setText(bookDowloadList.get(position).getTitle());
        downlaodBookViewHolder.txtFirstName.setText(bookDowloadList.get(position).getFirstAutor());
        downlaodBookViewHolder.txtLastNameAvtor.setText(bookDowloadList.get(position).getLastAutor());
        downlaodBookViewHolder.txtGenreBook.setText(bookDowloadList.get(position).getGenre());

    }

    @Override
    public int getItemCount() {
        return bookDowloadList.size();
    }

    public class DownlaodBookViewHolder extends RecyclerView.ViewHolder{

        private TextView txtNameBook, txtLastNameAvtor,txtFirstName,txtPageBook,txtDatePublication,txtGenreBook;
        private ImageView imageCover;

        public DownlaodBookViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameBook=(TextView) itemView.findViewById(R.id.name_book);
            txtLastNameAvtor=(TextView) itemView.findViewById(R.id.last_name_avtor);
            txtFirstName=(TextView) itemView.findViewById(R.id.first_name_avtor);
            txtGenreBook=(TextView) itemView.findViewById(R.id.book_genre);
            imageCover=(ImageView) itemView.findViewById(R.id.book_cover);
        }
    }
}
