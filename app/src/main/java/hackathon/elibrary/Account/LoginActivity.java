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
import hackathon.elibrary.R;
import hackathon.elibrary.Util.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private AppCompatButton appCompatButton;
    private TextView textView;
    private Context mContext=LoginActivity.this;
    private EditText editLogin,editPasswrod;
    private CheckBox remeberMe;
    SharedPreferences sharedLogin,sharedPassword;


    private  static final String BASE_URL="https://elibrary-app.herokuapp.com/#/docs/";
    private  final  String SAVE_LOGIN="save_login";
    private  final   String SAVE_PASSWORD="save_password";
    private  final String SAVE_TOKEN="saveToken";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupView();
        loadShared();
        textView=(TextView) findViewById(R.id.create_account);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupChekBox();
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
                        }
                    }
                });
            }
    private  void  setupView(){
             editLogin=(EditText) findViewById(R.id.login_in_login);
             editPasswrod=(EditText) findViewById(R.id.login_in_passwrod);
             remeberMe=(CheckBox)  findViewById(R.id.save_me);
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
                setupChekBox();
                saveMyToken(response.body().getId_token());
                Intent intent=new Intent(mContext,HomeActivity.class);
                startActivity(intent);
            }
        }
        @Override
        public void onFailure(Call<LogIn> call, Throwable t) {
            Toast.makeText(mContext," Petyx",Toast.LENGTH_SHORT).show();
        }
    });
}
    private void setupChekBox(){
        if(remeberMe.isChecked()){
       sharedLogin=getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor ed=sharedLogin.edit();
            ed.putString(SAVE_LOGIN, editLogin.getText().toString());
            ed.apply();

       sharedPassword=getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor ep=sharedLogin.edit();
            ep.putString(SAVE_PASSWORD,editPasswrod.getText().toString());
            ep.apply();
        }
}
    private void loadShared(){
        sharedLogin=getPreferences(MODE_PRIVATE);
        String login=sharedLogin.getString(SAVE_LOGIN,"");
        if(login!=null){
            editLogin.setText(login);
        }
        sharedPassword=getPreferences(MODE_PRIVATE);
        String password=sharedPassword.getString(SAVE_PASSWORD,"");
        if(password!=null){
            editPasswrod.setText(password);
        }
}
    private void saveMyToken(String nameToken){
        SharedPreferences sharedPreferences=getSharedPreferences("myToken",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(SAVE_TOKEN,nameToken);
        editor.apply();
}

}
