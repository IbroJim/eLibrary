package hackathon.elibrary.Home;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.IOException;

import hackathon.elibrary.R;

import hackathon.elibrary.Util.ApiInterface;
import hackathon.elibrary.Util.BottomNavigationSetupOptions;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {

    private Context mContext=HomeActivity.this;
    private static final int ACTIVITY_NUM=1;

    private TextView titleToken;
    private final String SAVE_TOKEN="save_token";
    private static final String TAG="Home";
    private   static final String BASE_URL="https://elibrary-app.herokuapp.com/#/docs/";
    private AcoountData acoountData;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupNavigation();
        titleToken=(TextView) findViewById(R.id.title_token);
        final String token = getToken();

        OkHttpClient.Builder okHttpClient=new OkHttpClient.Builder();
        okHttpClient.addInterceptor(new Interceptor() {
            @Override
    public Response intercept(Chain chain) throws IOException {
        Request original=chain.request();
        Request request=original.newBuilder()
                .addHeader("Accept","application/json")
                .addHeader("Authorization","Bearer "+ token).method(original.method(),original.body()).build();
        Log.d(TAG,"Jivoi");
        return chain.proceed(request);
    }
});
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.build()).build();
        ApiInterface apiInterface=retrofit.create(ApiInterface.class);
        Call<AcoountData> call=apiInterface.getUser();
        call.enqueue(new Callback<AcoountData>() {
            @Override
            public void onResponse(Call<AcoountData> call, retrofit2.Response<AcoountData> response ) {
                Toast.makeText(mContext,"responce"+response.code(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<AcoountData> call, Throwable t) {
                Log.d(TAG,"No worold");
            }
        });

    }

    private void setupNavigation(){
        BottomNavigationViewEx bottomNavigationViewEx=(BottomNavigationViewEx) findViewById(R.id.bottom_navigatiom_view_id);
        BottomNavigationSetupOptions.setupBottomNavigatiomViewEx(bottomNavigationViewEx);
        BottomNavigationSetupOptions.enableBottomNavigation(bottomNavigationViewEx,mContext,ACTIVITY_NUM);
        Menu menu=bottomNavigationViewEx.getMenu();
        MenuItem menuItem=menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
    private    String getToken(){

         SharedPreferences sharedPreferences=getSharedPreferences("myToken",Context.MODE_PRIVATE);
          return sharedPreferences.getString(SAVE_TOKEN,"");

    }
  /**  private void getAccountId(String name){
        Retrofit.Builder builder=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

         Retrofit retrofit=builder.build();
         ApiInterface clinet=retrofit.create(ApiInterface.class);
         Call<AcoountData> call=clinet.getUser(name);
         call.enqueue(new Callback<AcoountData>() {
            @Override
            public void onResponse(Call<AcoountData> call, Response<AcoountData> response) {

                Log.i(TAG,"message"+response.message());
                Log.i(TAG,"body"+response.body());
                try {
                    Log.i(TAG,"responce is"+response.errorBody().string());
                    if (response.code()==401){
                  Log.v(TAG,"responce is"+response.errorBody().string());
                  Toast.makeText(mContext,"401 ",Toast.LENGTH_SHORT).show();
                         }
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(Call<AcoountData> call, Throwable t) {
                    Toast.makeText(mContext,"Very good",Toast.LENGTH_SHORT).show();
            }
        });
    };
   **/

  private void setupRetrofit(final String token){
      Log.d(TAG,"SetypRetrofit");




  }



}
