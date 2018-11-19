package hackathon.elibrary.Util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import hackathon.elibrary.Book.Genre;
import hackathon.elibrary.R;

public class SpinnerGenreAdapter extends ArrayAdapter<Genre> {

    public SpinnerGenreAdapter(Context context, List<Genre> list){
       super(context,0,list);
    }


    @NonNull
    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView=LayoutInflater.from(getContext()).inflate(R.layout.text_spinner,parent,false);
        }
        TextView textGenre=convertView.findViewById(R.id.text_spinner_title);

            Genre genre=getItem(position);
            textGenre.setText(genre.getName());

            return convertView;
    }
}
