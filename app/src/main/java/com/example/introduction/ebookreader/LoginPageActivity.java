package com.example.introduction.ebookreader;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class LoginPageActivity extends AppCompatActivity {
    TabLayout tableLayout;
    ViewPager viewPager;

   // float v=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);
        tableLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.view_pager);
        tableLayout.addTab(tableLayout.newTab().setText("Login"));
        tableLayout.addTab(tableLayout.newTab().setText("Signup"));
        tableLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        TabAdapter tabAdapter= new TabAdapter(this,getSupportFragmentManager(),tableLayout.getTabCount());
        viewPager.setAdapter(tabAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tableLayout));
       /* tableLayout.setTranslationY(300);
        tableLayout.setAlpha(v);
        tableLayout.animate().translationY(0).setDuration(1000).setStartDelay(800).start();*/


        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
}