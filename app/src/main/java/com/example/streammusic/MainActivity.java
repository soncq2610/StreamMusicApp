package com.example.streammusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private static final int FRAGMENT_FEATURE = 0;
    private static final int FRAGMENT_LIBRARY = 1;
    private static final int FRAGMENT_Song = 2;
    private int currentFragment = FRAGMENT_FEATURE;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.viewpager);
        bottomNavigationView = findViewById(R.id.bottom_nav);

        FragmentManager fm =  getSupportFragmentManager();
        viewPagerAdapter= new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);



     bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
         @Override
         public boolean onNavigationItemSelected(@NonNull MenuItem item) {
             int id = item.getItemId();
             if(id == R.id.feature_icon){
                openFeatureFragment();
             } else if(id == R.id.library_icon) {
                 openLibraryFragment();
             }
             else if(id == R.id.mucsic_play) {
                 openSongFragment();}
             return true;
         }
     });

     viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
         @Override
         public void onPageSelected(int position) {
             switch (position){
                 case 0:
                     currentFragment = FRAGMENT_FEATURE;
                     bottomNavigationView.getMenu().findItem(R.id.feature_icon).setChecked(true);
                     break;
                 case 1:
                     currentFragment = FRAGMENT_LIBRARY;
                     bottomNavigationView.getMenu().findItem(R.id.library_icon).setChecked(true);
                     break;
                 case 2:
                     currentFragment = FRAGMENT_Song;
                     bottomNavigationView.getMenu().findItem(R.id.mucsic_play).setChecked(true);
                     break;
             }
         }
     });
    }

    private void openFeatureFragment(){
        if(currentFragment != FRAGMENT_FEATURE){
            viewPager2.setCurrentItem(0);
            currentFragment = FRAGMENT_FEATURE;
        }
    }
    private void openLibraryFragment() {
        if (currentFragment != FRAGMENT_LIBRARY) {
            viewPager2.setCurrentItem(1);
            currentFragment = FRAGMENT_LIBRARY;
        }
    }

    private void openSongFragment() {
        if (currentFragment != FRAGMENT_Song) {
            viewPager2.setCurrentItem(2);
            currentFragment = FRAGMENT_Song;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("puase","pasue");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("Stop","stop");
    }
}