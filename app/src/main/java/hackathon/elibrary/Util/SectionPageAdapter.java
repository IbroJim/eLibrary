package hackathon.elibrary.Util;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SectionPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragment=new ArrayList<>();
    private List<String> mTitleFragemt= new ArrayList<>();

    public void addFragment(Fragment fragment,String title){
        mFragment.add(fragment);
        mTitleFragemt.add(title);
    }

    public SectionPageAdapter(FragmentManager fm){
        super(fm);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleFragemt.get(position);
    }

    @Override
    public Fragment getItem(int pos) {
        return mFragment.get(pos);
    }

    @Override
    public int getCount() {
        return mFragment.size();
    }
}
