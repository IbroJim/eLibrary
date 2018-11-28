package hackathon.elibrary.Util;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hackathon.elibrary.POJO.Genre;
import hackathon.elibrary.R;

public class GenreRecyclerAdapter extends RecyclerView.Adapter<GenreRecyclerAdapter.GenreRecyclerViewHolder> {


    private List<Genre> genresList;
    private Context mContext;

    public GenreRecyclerAdapter(List<Genre> genresList,Context context) {
        this.genresList = genresList;
        this.mContext=context;
    }

    @NonNull
    @Override
    public GenreRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater=LayoutInflater.from(viewGroup.getContext());
        View view=layoutInflater.inflate(R.layout.filtr_genre_list,viewGroup,false);
        return new GenreRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreRecyclerViewHolder genreRecyclerViewHolder, int position) {
               genreRecyclerViewHolder.txtNameGenre.setText(genresList.get(position).getName());
               genreRecyclerViewHolder.imageGenre.setImageResource(R.drawable.ic_genre_image);
    }

    @Override
    public int getItemCount() {
        return genresList.size();
    }

    public class GenreRecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNameGenre;
        private ImageView imageGenre;
        public GenreRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameGenre=(TextView) itemView.findViewById(R.id.name_genre);
            imageGenre=(ImageView) itemView.findViewById(R.id.genre_image);
        }
    }
}
