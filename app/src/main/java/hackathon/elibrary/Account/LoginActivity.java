package hackathon.elibrary.Account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;

import hackathon.elibrary.Home.HomeActivity;
import hackathon.elibrary.Profile.ProfileActivity;
import hackathon.elibrary.R;

public class LoginActivity extends AppCompatActivity {

    private AppCompatButton appCompatButton;
    private TextView textView;
    private Context mContext=LoginActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        appCompatButton=(AppCompatButton) findViewById(R.id.login_in);
        textView=(TextView) findViewById(R.id.create_account);
        appCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(mContext,HomeActivity.class);
                startActivity(intent);
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(mContext, RegistryActivity.class);
                startActivity(intent);
            }
        });
            }


}
