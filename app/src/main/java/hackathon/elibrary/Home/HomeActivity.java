package hackathon.elibrary.Home;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import hackathon.elibrary.MyDataBase.DatabaseHelper;
import hackathon.elibrary.MyDataBase.TranslateSchema;
import hackathon.elibrary.MyDataBase.TranslateSchema.TranslateTable;
import hackathon.elibrary.POJO.AccountData;
import hackathon.elibrary.POJO.Profile;
import hackathon.elibrary.POJO.Translate;
import hackathon.elibrary.POJO.User;
import hackathon.elibrary.R;

import hackathon.elibrary.Util.ApiInterface;
import hackathon.elibrary.Util.BottomNavigationSetupOptions;
import hackathon.elibrary.Util.OkHttpHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeActivity extends AppCompatActivity {


    private static final int ACTIVITY_NUM=1;
    private static final String SAVE_TOKEN="saveToken";
    private static final String TAG="HomeActivity";
    private static final String EXTRA_NAME="login";
    private static final String ACCOUNT_ID="MyAccountId";
    private static final String SAVE_PROFILE = "profileID";

    private Context mContext=HomeActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupNavigation();
        createFolder();
        getAccountId();
    }
    private void createFolder(){
        File folder=new File(Environment.getExternalStorageDirectory()+"/eLibrary");
        boolean succes=true;
        if(!folder.exists()){
            succes=folder.mkdir();
        }
        if(succes){
            Log.d(TAG,"Созданно");
        }else {
            Log.d(TAG,"Ошибка");
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
    private   String getToken(){
         SharedPreferences sharedPreferences=getSharedPreferences("myToken",Context.MODE_PRIVATE);
          return sharedPreferences.getString(SAVE_TOKEN,"");
    }
    private void  getAccountId(){
      final String token = getToken();
      Retrofit retrofit=OkHttpHelper.getRetrofitToken(token);
      final ApiInterface apiInterface=retrofit.create(ApiInterface.class);
      Call<AccountData> call=apiInterface.getUser();
      call.enqueue(new Callback<AccountData>() {
          @Override
          public void onResponse(Call<AccountData> call, retrofit2.Response<AccountData> response ) {

              if(response.code()==200) {
                  saveLogin(response.body().getLogin());
                  saveAccountId(response.body().getId());
                  takeProfileId(response.body().getId());
              }
          }

          @Override
          public void onFailure(Call<AccountData> call, Throwable t) {
              Log.d(TAG,"No worold");
          }
      });
  }
    private void saveLogin(String login){
        SharedPreferences sharedPreferences=getSharedPreferences("myLogin",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(EXTRA_NAME,login);
        editor.apply();
 }
    private void saveAccountId(Integer id){
     SharedPreferences sharedPreferences1=getSharedPreferences("myAccountId",Context.MODE_PRIVATE);
     SharedPreferences.Editor editorInt=sharedPreferences1.edit();
     editorInt.putInt(ACCOUNT_ID,id);
     editorInt.apply();
 }
    private void takeProfileId(Integer idAcc) {
        Long longs = Long.parseLong(String.valueOf(idAcc));
        Retrofit retrofit = OkHttpHelper.getRetrofitToken(getToken());
        ApiInterface client = retrofit.create(ApiInterface.class);
        Call<Profile> call = client.getProfileId(longs);
        Log.d("NewBook", "Hello" + longs);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if(response.code()==200) {
                    saveIdProfile(response.body().getId());
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {

            }
        });

    }
    private void saveIdProfile(Integer id) {
        SharedPreferences sharedPreferences = getSharedPreferences("profileId", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SAVE_PROFILE, id);
        editor.apply();

    }








}
