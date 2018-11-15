package hackathon.elibrary.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import hackathon.elibrary.R;
import hackathon.elibrary.Util.BottomNavigationSetupOptions;
import hackathon.elibrary.Util.SectionPageAdapter;

public class ProfileActivity extends AppCompatActivity {

    private SectionPageAdapter sectionPageAdapter;
    private ViewPager mViewPager;
    private Context mContext=ProfileActivity.this;
    private TextView textLogin;

    private static  final int ACTIVITY_NUM=3;
    private static  final String EXTRA_NAME="login";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupNavigation();
        setupViewProfile();
        getShariedMylogin();

        Button button=(Button) findViewById(R.id.add_book);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,NewBook.class);
                startActivity(intent);
            }
        });



    }
    private void setupViewProfile(){
        sectionPageAdapter=new SectionPageAdapter((getSupportFragmentManager()));
        mViewPager=(ViewPager) findViewById(R.id.view_pager);
        setupViewPager(mViewPager);

        TabLayout tabLayout=(TabLayout) findViewById(R.id.profile_tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
        Toolbar toolbar=findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);

    }
    private void setupViewPager(ViewPager viewPager){
        SectionPageAdapter adapter=new SectionPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new DownloadedBookFragment(),"Загруженные");
        adapter.addFragment(new ReadBookFragment(),"Прочитанные");
        viewPager.setAdapter(adapter);
    }
    private void setupNavigation(){
        BottomNavigationViewEx bottomNavigationViewEx=(BottomNavigationViewEx) findViewById(R.id.bottom_navigatiom_view_id);
        BottomNavigationSetupOptions.setupBottomNavigatiomViewEx(bottomNavigationViewEx);
        BottomNavigationSetupOptions.enableBottomNavigation(bottomNavigationViewEx,mContext,ACTIVITY_NUM);
        Menu menu=bottomNavigationViewEx.getMenu();
        MenuItem menuItem=menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
    private void getShariedMylogin(){
        SharedPreferences sharedPreferences=getSharedPreferences("myLogin",Context.MODE_PRIVATE);
        String login=sharedPreferences.getString(EXTRA_NAME,"");
        textLogin=(TextView) findViewById(R.id.name_user);
        textLogin.setText(login);

    }

}
