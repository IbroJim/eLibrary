package hackathon.elibrary.Account;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import hackathon.elibrary.POJO.User;
import hackathon.elibrary.R;
import hackathon.elibrary.Util.ApiInterface;
import hackathon.elibrary.Util.OkHttpHelper;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistryActivity extends AppCompatActivity {

    private  static final String BASE_URL="https://elibrary-app.herokuapp.com/#/docs/";
    private static final String TAG="RegistryActivity";

    private Context mContext=RegistryActivity.this;
    private EditText editLastName,editFirstName,editLogin,editPassword,editEmaill,reapitPassword;
    private AppCompatButton createAccount;
    private TextView textResponce;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);
        setupView();
        final String langKey="ru";
        final boolean activated=false;
        createAccount=(AppCompatButton) findViewById(R.id.create_account_user);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editEmaill.getText().toString()!=null&&editLastName.getText().toString()!=null&&editFirstName.getText().toString()!=null&&
                editLogin.getText().toString()!=null&&editPassword.getText().toString()!=null){
                    if(editPassword.getText().toString()==reapitPassword.getText().toString()) {
                        User user = new User(activated,
                                editEmaill.getText().toString(),
                                editLastName.getText().toString(),
                                editFirstName.getText().toString(),
                                editLogin.getText().toString(),
                                editPassword.getText().toString(),
                                langKey);
                        setupConnectionApi(user);
                    }else {Toast.makeText(mContext,"Пароль не совпадает",Toast.LENGTH_SHORT).show();}
                }else {Toast.makeText(mContext,"Введите данные",Toast.LENGTH_SHORT).show();}
            }
        });
    }
    private void setupView(){
        editEmaill=(EditText) findViewById(R.id.edit_email);
        editLogin=(EditText) findViewById(R.id.edit_login);
        editLastName=(EditText) findViewById(R.id.edit_last_name);
        editFirstName=(EditText) findViewById(R.id.edit_first_name);
        editPassword=(EditText) findViewById(R.id.edit_password);
        textResponce=(TextView) findViewById(R.id.sett_me);
        reapitPassword=(EditText) findViewById(R.id.reapit_password);


    }
    private void setupConnectionApi(User user) {
        OkHttpClient.Builder httpClient=new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original=chain.request();
                Request request=original.newBuilder()
                        .addHeader("Content-Type","application/json")
                        .addHeader("Accept","*/*")
                        .method(original.method(),original.body()).build();
                return chain.proceed(request);
            }
        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build()).build();
        ApiInterface client=retrofit.create(ApiInterface.class);
        Call<User> call=client.createAccount(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
               Toast.makeText(mContext, "Responce" + response.code(), Toast.LENGTH_SHORT).show();
              if(response.code()==400){
                  Toast.makeText(mContext,"Логин или Email занят",Toast.LENGTH_SHORT).show();
              }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                textResponce.setText("Проверте почту");
            }
        });

    }
}
