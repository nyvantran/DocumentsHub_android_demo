package com.ptithcm.documentshub.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.ptithcm.documentshub.R;
import com.ptithcm.documentshub.adapter.HomeViewPagerAdapter;

/**
 * Activity chính quản lý ViewPager2 đồng bộ với BottomNavigationView.
 */
public class HomeActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewPager = findViewById(R.id.view_pager_home);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Thiết lập Adapter cho ViewPager2
        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // 1. Khi nhấn vào BottomNavigationView thì đổi trang ViewPager2
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    viewPager.setCurrentItem(0);
                } else if (itemId == R.id.nav_upload) {
                    viewPager.setCurrentItem(1);
                } else if (itemId == R.id.nav_profile) {
                    viewPager.setCurrentItem(2);
                }
                return true;
            }
        });

        // 2. Khi vuốt ViewPager2 thì cập nhật lại mục đang chọn ở BottomNavigationView
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.nav_home);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.nav_upload);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
                        break;
                }
            }
        });

        // Vô hiệu hóa tính năng vuốt nếu cần (ở đây đang bật theo yêu cầu)
        // viewPager.setUserInputEnabled(true);
    }
}