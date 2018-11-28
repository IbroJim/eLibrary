package hackathon.elibrary.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import hackathon.elibrary.POJO.Book;
import hackathon.elibrary.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BookRecyclerAdapter extends RecyclerView.Adapter<BookRecyclerAdapter.BookViewHolder>{

    private static final String SAVE_TOKEN="saveToken";

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
        downloadCover(bookViewHolder.imageCover,bookList.get(position).getId());
    }
    @Override
    public int getItemCount() {
        return bookList.size();
    }
    public class BookViewHolder extends  RecyclerView.ViewHolder{

        private TextView txtNameBook, txtLastNameAvtor,txtFirstName,txtPageBook,txtDatePublication,txtGenreBook;
        private ImageView imageCover;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameBook=(TextView) itemView.findViewById(R.id.name_book);
            txtLastNameAvtor=(TextView) itemView.findViewById(R.id.last_name_avtor);
            txtFirstName=(TextView) itemView.findViewById(R.id.first_name_avtor);
            txtGenreBook=(TextView) itemView.findViewById(R.id.book_genre);
            imageCover=(ImageView) itemView.findViewById(R.id.book_cover);
        }
    }
    public  String getToken(){
        SharedPreferences sharedPreferences=mContext.getSharedPreferences("myToken",Context.MODE_PRIVATE);
        return sharedPreferences.getString(SAVE_TOKEN,"");
    }
    private void downloadCover(final ImageView imageView,Integer id){
        Long idLong=Long.parseLong(String.valueOf(id));
       Retrofit retrofit=OkHttpHelper.getRetrofitToken(getToken());
       ApiInterface apiInterface=retrofit.create(ApiInterface.class);
       Call<ResponseBody> call=apiInterface.downloadCover(idLong);
       call.enqueue(new Callback<ResponseBody>() {
           @Override
           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
       if(response.code()==200){
           try {
               InputStream byteArrayInputStream= new ByteArrayInputStream(response.body() != null ? response.body().bytes() : new byte[0]);
               ByteArrayOutputStream buffer=new ByteArrayOutputStream();
               int inRead;
               byte[] data=new byte[4096];
               while ((inRead=byteArrayInputStream.read(data,0,data.length))!=-1){
                   buffer.write(data,0,inRead);
               }

               buffer.flush();
               byte[] byteArray=buffer.toByteArray();
               buffer.close();
               Bitmap bitmap=BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
               if(bitmap!=null) {
                   Bitmap bitmapImage = Bitmap.createScaledBitmap(bitmap, imageView.getWidth(), imageView.getHeight(), true);
                   imageView.setImageBitmap(bitmapImage);
               }

           } catch (IOException e) {
               e.printStackTrace();
           }
       }
           }

           @Override
           public void onFailure(Call<ResponseBody> call, Throwable t) {

           }
       });
   }
    public void setFiltrBook(ArrayList<Book> newList){
        bookList=new ArrayList<>();
        bookList.addAll(newList);
        notifyDataSetChanged();


    }



}
