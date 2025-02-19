package com.example.broadcastreceiver;

import android.os.Bundle;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.broadcastreceiver.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnOkHttp = findViewById(R.id.btnOkHttp);
        Button btnRetrofit = findViewById(R.id.btnRetrofit);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        // Set Adapter cho ViewPager2
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Liên kết TabLayout với ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(position == 0 ? "OkHttp" : "Retrofit")
        ).attach();

        // Xử lý sự kiện bấm nút để chuyển tab
        btnOkHttp.setOnClickListener(v -> viewPager.setCurrentItem(0));
        btnRetrofit.setOnClickListener(v -> viewPager.setCurrentItem(1));
    }
}
