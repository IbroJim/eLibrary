package hackathon.elibrary.Account;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import hackathon.elibrary.R;
import hackathon.elibrary.Util.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistryActivity extends AppCompatActivity {

    private Context mContext=RegistryActivity.this;
    private EditText editLastName,editFirstName,editLogin,editPassword,editEmaill;
    private AppCompatButton createAccount;

    private final static String BASE_URL="https://elibrary-app.herokuapp.com/#/docs/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);
        setupView();

        createAccount=(AppCompatButton) findViewById(R.id.create_account_user);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user=new User(editEmaill.getText().toString(),
                         editLastName.getText().toString(),
                          editFirstName.getText().toString(),
                           editLogin.getText().toString(),
                            editPassword.getText().toString(),
                            "ru");
                setupConnectionApi(user);

            }
        });
    }

    private void setupView(){
        editEmaill=(EditText) findViewById(R.id.edit_email);
        editLogin=(EditText) findViewById(R.id.edit_login);
        editLastName=(EditText) findViewById(R.id.edit_last_name);
        editFirstName=(EditText) findViewById(R.id.edit_first_name);
        editPassword=(EditText) findViewById(R.id.edit_password);

    }

    private void setupConnectionApi(final User user){

        Retrofit.Builder builder= new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit=builder.build();

        ApiInterface client=retrofit.create(ApiInterface.class);
        Call<User> call=client.createAccount(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Toast.makeText(RegistryActivity.this,"Df"+response.body().,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(mContext,"Криворукий",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
