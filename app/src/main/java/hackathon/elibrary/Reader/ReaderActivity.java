package hackathon.elibrary.Reader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.IOException;

import hackathon.elibrary.MyDataBase.DatabaseHelper;
import hackathon.elibrary.MyDataBase.TranslateSchema.TranslateTable;
import hackathon.elibrary.R;
import hackathon.elibrary.Util.BottomNavigationSetupOptions;

public class ReaderActivity extends AppCompatActivity {

    private Context mContext=ReaderActivity.this;
    private static final int ACTIVITY_NUM=4;


    private EditText editWord;
    private TextView  translateWord;
    private DatabaseHelper myDbHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        setupNavigation();
        createDataBase();
        setupView();
    }
    private void createDataBase(){
        myDbHelper = new DatabaseHelper(this);
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            myDbHelper.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }
    }
    private void setupNavigation(){
        BottomNavigationViewEx bottomNavigationViewEx=(BottomNavigationViewEx) findViewById(R.id.bottom_navigatiom_view_id);
        BottomNavigationSetupOptions.setupBottomNavigatiomViewEx(bottomNavigationViewEx);
        BottomNavigationSetupOptions.enableBottomNavigation(bottomNavigationViewEx,mContext,ACTIVITY_NUM);
        Menu menu=bottomNavigationViewEx.getMenu();
        MenuItem menuItem=menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
    private void setupView() {
        editWord = (EditText) findViewById(R.id.edit_word);
        editWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

   TextView translateText=(TextView) findViewById(R.id.translate_text);
      translateText.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
           setupAlertDialog();
          }
      });
    }

    private void getWord(String word){
        String valueString=null;
       Cursor cursor=myDbHelper.query("MyTranslateTable",new String[]{"value","name"},"name=?",
                new String[]{word},null,null,null);
        if(cursor.moveToFirst()){
            int column=cursor.getColumnIndex("value");
            do {

              translateWord.setText(cursor.getString(column));
            }while (cursor.moveToNext());
       }else {
             translateWord.setText("Невозможно перевести");
        }
    }

    private void setupAlertDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        View view=getLayoutInflater().inflate(R.layout.alert_translate_dialog,null);
        translateWord=(TextView) view.findViewById(R.id.translate_word);
        getWord(editWord.getText().toString());
        builder.setView(view);
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

}

