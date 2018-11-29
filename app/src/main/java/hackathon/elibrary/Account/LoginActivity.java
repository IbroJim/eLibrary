package hackathon.elibrary.Account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import hackathon.elibrary.Home.HomeActivity;
import hackathon.elibrary.POJO.LogIn;
import hackathon.elibrary.POJO.Profile;
import hackathon.elibrary.R;
import hackathon.elibrary.Util.ApiInterface;
import hackathon.elibrary.Util.OkHttpHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {


    private  static final String BASE_URL="https://elibrary-app.herokuapp.com/#/docs/";
    private  final String SAVE_TOKEN="saveToken";
    private static final String ACCOUNT_ID="MyAccountId";

    private AppCompatButton appCompatButton;
    private TextView textView;
    private Context mContext=LoginActivity.this;
    private EditText editLogin,editPasswrod;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupView();
        clickedListenner();
        String token=getToken();
        if(token!=null){
            Intent intent=new Intent(mContext,HomeActivity.class);
            startActivity(intent);
        }
    }
    private  void  setupView(){
             editLogin=(EditText) findViewById(R.id.login_in_login);
             editPasswrod=(EditText) findViewById(R.id.login_in_passwrod);
            }
    private void logInProfile(LogIn logIn){
    Retrofit.Builder builder= new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());
    Retrofit retrofit=builder.build();
    ApiInterface client=retrofit.create(ApiInterface.class);
    Call<LogIn> logInCall=client.logInPtofile(logIn);
    logInCall.enqueue(new Callback<LogIn>() {
        @Override
        public void onResponse(Call<LogIn> call, Response<LogIn> response) {
            if (response.code()==200){
                saveMyToken(response.body().getId_token());
                Intent intent=new Intent(mContext,HomeActivity.class);
                startActivity(intent);
            }else {Toast.makeText(mContext,"Проверьте логин или пароль",Toast.LENGTH_SHORT).show();}
        }
        @Override
        public void onFailure(Call<LogIn> call, Throwable t) {
            Toast.makeText(mContext,"Произошла ошибка ",Toast.LENGTH_SHORT).show();
        }
    });
}
    private void saveMyToken(String nameToken){
        SharedPreferences sharedPreferences=getSharedPreferences("myToken",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(SAVE_TOKEN,nameToken);
        editor.apply();
}
    private void clickedListenner(){
        textView=(TextView) findViewById(R.id.create_account);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, RegistryActivity.class);
                startActivity(intent);
            }
        });
        appCompatButton=(AppCompatButton) findViewById(R.id.login_in);
        appCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editLogin.getText()!=null&&editPasswrod.getText()!=null) {
                    LogIn logIn = new LogIn(editLogin.getText().toString(),
                            editPasswrod.getText().toString());
                    logInProfile(logIn);
                }else {Toast.makeText(mContext,"Введите данные",Toast.LENGTH_SHORT).show();}
            }
        });
    }
    private String getToken(){
        SharedPreferences sharedPreferences=getSharedPreferences("myToken",Context.MODE_PRIVATE);
        return sharedPreferences.getString(SAVE_TOKEN,null);
    }



}
