package hackathon.elibrary.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import hackathon.elibrary.Profile.ProfileActivity;
import hackathon.elibrary.R;

import hackathon.elibrary.Util.ApiInterface;
import hackathon.elibrary.Util.BottomNavigationSetupOptions;
import hackathon.elibrary.Util.OkHttpHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class HomeActivity extends AppCompatActivity {

    private Context mContext=HomeActivity.this;
    private TextView titleToken;


    private static final int ACTIVITY_NUM=1;
    private static final String SAVE_TOKEN="save_token";
    private static final String TAG="Home";
    private static final String EXTRA_NAME="login";
    private static final String ACCOUNT_ID="MyAccountId";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupNavigation();
        setupRetrofit();
    }

    private void setupNavigation(){
        BottomNavigationViewEx bottomNavigationViewEx=(BottomNavigationViewEx) findViewById(R.id.bottom_navigatiom_view_id);
        BottomNavigationSetupOptions.setupBottomNavigatiomViewEx(bottomNavigationViewEx);
        BottomNavigationSetupOptions.enableBottomNavigation(bottomNavigationViewEx,mContext,ACTIVITY_NUM);
        Menu menu=bottomNavigationViewEx.getMenu();
        MenuItem menuItem=menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
    public  String getToken(){
         SharedPreferences sharedPreferences=getSharedPreferences("myToken",Context.MODE_PRIVATE);
          return sharedPreferences.getString(SAVE_TOKEN,"");
    }


  private void setupRetrofit(){
      final String token = getToken();
      Retrofit retrofit=OkHttpHelper.getRetrofitToken(token);
      final ApiInterface apiInterface=retrofit.create(ApiInterface.class);
      Call<AccountData> call=apiInterface.getUser();
      call.enqueue(new Callback<AccountData>() {
          @Override
          public void onResponse(Call<AccountData> call, retrofit2.Response<AccountData> response ) {

              saveLogin(response.body().getLogin());
              saveAccountId(response.body().getId());
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

}
