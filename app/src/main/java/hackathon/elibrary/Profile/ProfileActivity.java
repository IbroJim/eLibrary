package hackathon.elibrary.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import hackathon.elibrary.R;
import hackathon.elibrary.Util.BottomNavigationSetupOptions;
import hackathon.elibrary.Util.SectionPageAdapter;

public class ProfileActivity extends AppCompatActivity {

    private SectionPageAdapter sectionPageAdapter;
    private ViewPager mViewPager;
    private Context mContext=ProfileActivity.this;
    private static  final int ACTIVITY_NUM=3;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupNavigation();

        sectionPageAdapter=new SectionPageAdapter((getSupportFragmentManager()));
        mViewPager=(ViewPager) findViewById(R.id.view_pager);
        setupViewPager(mViewPager);

        TabLayout tabLayout=(TabLayout) findViewById(R.id.profile_tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
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
}
