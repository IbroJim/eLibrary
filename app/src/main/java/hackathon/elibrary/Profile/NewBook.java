package hackathon.elibrary.Profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import hackathon.elibrary.R;

public class NewBook extends AppCompatActivity {

    private static final String ACCOUNT_ID="MyAccountId";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);

        SharedPreferences sharedPreferences=getSharedPreferences("myAccountId",Context.MODE_PRIVATE);
        Integer myId=sharedPreferences.getInt(ACCOUNT_ID,0);
        TextView textView=(TextView) findViewById(R.id.test_title);
        Toast.makeText(NewBook.this,"id"+myId,Toast.LENGTH_SHORT).show();
    }
}
