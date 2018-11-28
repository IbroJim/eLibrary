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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import hackathon.elibrary.Book.DetailsBook;
import hackathon.elibrary.Book.NewBook;
import hackathon.elibrary.POJO.AccountData;
import hackathon.elibrary.POJO.Profile;
import hackathon.elibrary.R;
import hackathon.elibrary.Util.ApiInterface;
import hackathon.elibrary.Util.BottomNavigationSetupOptions;
import hackathon.elibrary.Util.OkHttpHelper;
import hackathon.elibrary.Util.SectionPageAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileActivity extends AppCompatActivity {


    private static  final int ACTIVITY_NUM=3;
    private  final String SAVE_TOKEN="saveToken";
    private static final String ACCOUNT_ID="MyAccountId";

    private SectionPageAdapter sectionPageAdapter;
    private ViewPager mViewPager;
    private Context mContext=ProfileActivity.this;
    private TextView lastName,firstName,login;
    private boolean banned=false;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupNavigation();
        setupViewProfile();
        setupView();
        getUserInformation();
        createBook();
    }
    private void setupView(){
        lastName=(TextView) findViewById(R.id.last_name_user);
        firstName=(TextView) findViewById(R.id.name_user);
        login=(TextView)findViewById(R.id.login_user);
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
    private   String getToken(){
        SharedPreferences sharedPreferences=getSharedPreferences("myToken",Context.MODE_PRIVATE);
        return sharedPreferences.getString(SAVE_TOKEN,"");
    }
    private void getUserInformation(){
        Integer id=getId();
        Long idLong=Long.parseLong(String.valueOf(id));
        Retrofit retrofit=OkHttpHelper.getRetrofitToken(getToken());
        ApiInterface apiInterface=retrofit.create(ApiInterface.class);
        Call<Profile> call=apiInterface.getProfileId(idLong);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if(response.code()==200){
                    setupInformationUser(response.body().getUserLogin(),response.body().getUserFirstName(),response.body().getUserLastName(),response.body().getBanned());
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {

            }
        });
    }
    private Integer getId() {
        SharedPreferences sharedPreferences = getSharedPreferences("myAccountId", Context.MODE_PRIVATE);
        return sharedPreferences.getInt(ACCOUNT_ID, 0);
    }
    private void setupInformationUser(String loginResponce,String nameUserResponce,String lastNameResponce, boolean bannedResponce){
        login.setText(loginResponce);
        firstName.setText(nameUserResponce);
        lastName.setText(lastNameResponce);
        banned=bannedResponce;
    }
    private void createBook(){
        LinearLayout linearCreate=(LinearLayout) findViewById(R.id.create_book);
        linearCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(banned==false) {
                    Intent intent = new Intent(mContext, NewBook.class);
                    startActivity(intent);
                }else {Toast.makeText(mContext,"Вы заблокированы",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
