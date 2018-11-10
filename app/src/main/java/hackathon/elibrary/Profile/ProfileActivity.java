package hackathon.elibrary.Profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import hackathon.elibrary.R;
import hackathon.elibrary.Util.SectionPageAdapter;

public class ProfileActivity extends AppCompatActivity {

    private SectionPageAdapter sectionPageAdapter;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
}
