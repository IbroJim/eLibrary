package hackathon.elibrary.Book;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import hackathon.elibrary.MyDataBase.BookSchema.BookTable;
import hackathon.elibrary.MyDataBase.DatabaseHelper;
import hackathon.elibrary.POJO.Favorite;
import hackathon.elibrary.R;
import hackathon.elibrary.Reader.ReaderActivity;
import hackathon.elibrary.Util.ApiInterface;
import hackathon.elibrary.Util.OkHttpHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailsBookProfil extends AppCompatActivity implements View.OnClickListener{

    private static final String ID_BOOK_SQL = "idBookSql";
    private static final String NAME_FILE_PDF = "namePdfFile";
    private static final String SAVE_TOKEN = "saveToken";
    private static final String SAVE_PROFILE = "profileID";



    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;
    private ContentValues contentValues;
    private Context mContext=DetailsBookProfil.this;
    private TextView txtNameBook,txtLastName,txtFirstName,txtPage,txtDatePublication,txtGenre,txtDiscription,txtCreatedBy;
    private ImageView imageCover;
    private String nameFile;
    private Integer idBook;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_profile);
        setupDatabase();
        setupView();
        setupDataView();
    }
    private void  setupDatabase(){
        databaseHelper=new DatabaseHelper(mContext);
        sqLiteDatabase=databaseHelper.getReadableDatabase();
        contentValues=new ContentValues();
    }
    private Integer getIdSql(){
        Intent intent=getIntent();
        Integer idBook=intent.getIntExtra(ID_BOOK_SQL,0);
       return idBook;
    }
    private void setupView(){
        txtNameBook=(TextView) findViewById(R.id.name_book);
        txtFirstName=(TextView) findViewById(R.id.first_name_avtor);
        txtLastName=(TextView) findViewById(R.id.last_name_avtor);
        txtGenre=(TextView) findViewById(R.id.book_genre) ;
        txtPage=(TextView) findViewById(R.id.book_page);
        txtDiscription=(TextView)findViewById(R.id.book_description);
        txtDatePublication=(TextView) findViewById(R.id.date_publication);
        txtCreatedBy=(TextView) findViewById(R.id.created_by);
        imageCover=(ImageView) findViewById(R.id.cover_book_image);
        Button readBook=(Button)findViewById(R.id.read_book);
        readBook.setOnClickListener(this);
        Button addReader=(Button) findViewById(R.id.add_read);
        addReader.setOnClickListener(this);
    }
    private void setupDataView(){
        Integer id=getIdSql();
        Log.d("Details","Hello world");
        String[] projection={BookTable.NameOfFields.ID,BookTable.NameOfFields.TITLE,BookTable.NameOfFields.FIRST_NAME, BookTable.NameOfFields.LAST_NAME,BookTable.NameOfFields.GENRE,BookTable.NameOfFields.PAGE,
        BookTable.NameOfFields.NAME_FILE,BookTable.NameOfFields.DISCRIPTION,BookTable.NameOfFields.DATE_PIBLICATION,BookTable.NameOfFields.CREATED_BY,BookTable.NameOfFields.ID_BOOK};
        Cursor cursor=sqLiteDatabase.query(BookTable.NAME_TABLE_TWO,projection,"_id=?",new String[]{String.valueOf(id)},null,null,null);
        if(cursor.moveToFirst()){
            int columnTitle=cursor.getColumnIndex(BookTable.NameOfFields.TITLE);
            int columnLastName=cursor.getColumnIndex(BookTable.NameOfFields.LAST_NAME);
            int columnFirstName=cursor.getColumnIndex(BookTable.NameOfFields.FIRST_NAME);
            int columnGenre=cursor.getColumnIndex(BookTable.NameOfFields.GENRE);
            int columnCreatedBy=cursor.getColumnIndex(BookTable.NameOfFields.CREATED_BY);
            int columnPage=cursor.getColumnIndex(BookTable.NameOfFields.PAGE);
            int columnDatePublication=cursor.getColumnIndex(BookTable.NameOfFields.DATE_PIBLICATION);
            int columnDiscription=cursor.getColumnIndex(BookTable.NameOfFields.DISCRIPTION);
            int fileName=cursor.getColumnIndex(BookTable.NameOfFields.NAME_FILE);
            int columnIdBook=cursor.getColumnIndex(BookTable.NameOfFields.ID_BOOK);
            do {
                txtNameBook.setText(cursor.getString(columnTitle));
                txtLastName.setText(cursor.getString(columnLastName));
                txtFirstName.setText(cursor.getString(columnFirstName));
                txtGenre.setText(cursor.getString(columnGenre));
                String pageString=getString(R.string.book_page,cursor.getInt(columnPage));
                txtPage.setText(pageString);
                String dateString=getString(R.string.book_date_publication,cursor.getInt(columnDatePublication));
                txtDatePublication.setText(dateString);
                txtDiscription.setText(cursor.getString(columnDiscription));
                String createdString=getString(R.string.add_user,cursor.getString(columnCreatedBy));
                txtCreatedBy.setText(createdString);
                nameFile=cursor.getString(fileName);
                idBook=cursor.getInt(columnIdBook);
            }while (cursor.moveToNext());
        }else {

        }


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.read_book:
                Toast.makeText(mContext,"d"+nameFile,Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(mContext,ReaderActivity.class);
                intent.putExtra(NAME_FILE_PDF,nameFile);
                startActivity(intent);
                break;
            case R.id.add_read:
                Favorite addFavorite=new Favorite(idBook,getProfileId());
                createReadeBook(addFavorite);
                break;
        }
    }
    private Integer getProfileId(){
        SharedPreferences sharedPreferences=getSharedPreferences("profileId",Context.MODE_PRIVATE);
        return sharedPreferences.getInt(SAVE_PROFILE,0);
    }
    private String getToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("myToken", Context.MODE_PRIVATE);
        return sharedPreferences.getString(SAVE_TOKEN, "");
    }
    private void createReadeBook(Favorite addFavorite){
        Retrofit retrofit=OkHttpHelper.getRetrofitToken(getToken());
        ApiInterface apiInterface=retrofit.create(ApiInterface.class);
        Call<Favorite> call=apiInterface.createReadBook(addFavorite);
        call.enqueue(new Callback<Favorite>() {
            @Override
            public void onResponse(Call<Favorite> call, Response<Favorite> response) {
                if(response.code()==201){
                    Toast.makeText(mContext,"Книга добавлена в прочитанное",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Favorite> call, Throwable t) {

            }
        });

    }
}
