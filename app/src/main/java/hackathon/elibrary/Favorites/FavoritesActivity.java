package hackathon.elibrary.Favorites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import de.hdodenhof.circleimageview.CircleImageView;
import hackathon.elibrary.Profile.ProfileActivity;
import hackathon.elibrary.R;
import hackathon.elibrary.Reader.ReaderActivity;
import hackathon.elibrary.Util.BottomNavigationSetupOptions;

public class FavoritesActivity extends AppCompatActivity {

    private Context mContext=FavoritesActivity.this;
    private static final int ACTIVITY_NUM=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_favourites);
        setupNavigation();

        Toolbar toolbar=findViewById(R.id.toolbar_favorites);
        setSupportActionBar(toolbar);

        TextView textView=(TextView) findViewById(R.id.name_Asd);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,ProfileActivity.class);
                startActivity(intent);
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





}
