package hackathon.elibrary.Util;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import hackathon.elibrary.Favorites.FavoritesActivity;
import hackathon.elibrary.Home.HomeActivity;
import hackathon.elibrary.Profile.ProfileActivity;
import hackathon.elibrary.R;
import hackathon.elibrary.Reader.ReaderActivity;
import hackathon.elibrary.Search.SearchActivty;

public class BottomNavigationSetupOptions {

    public static void setupBottomNavigatiomViewEx(BottomNavigationViewEx bottomNavigationViewEx){
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
        bottomNavigationViewEx.enableAnimation(false);
    }

    public static void enableBottomNavigation(final BottomNavigationViewEx bottomNavigationViewEx, final Context context, final  int positionActivty){
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.ic_aheart:
                        if(positionActivty!=0){
                            Intent intent=new Intent(context, FavoritesActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                            break;
                        }
                        break;
                    case R.id.ic_home:
                        if(positionActivty!=1){
                            Intent intent=new Intent(context, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                            break;
                        }
                        break;
                    case R.id.ic_search:
                        if(positionActivty!=2){
                            Intent intent=new Intent(context, SearchActivty.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                            break;
                        }
                        break;
                    case R.id.ic_user:
                        if(positionActivty!=3){
                            Intent intent=new Intent(context,ProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                            break;
                        }
                        break;
                    case R.id.ic_reader:
                        if(positionActivty!=4){
                            Intent intent=new Intent(context, ReaderActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                            break;
                        }
                    break;


                }
                return false;
            }
        });
    }


}
