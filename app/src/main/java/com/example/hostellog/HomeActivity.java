package com.example.hostellog;

import android.os.Bundle;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends AppCompatActivity {
    Window window;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.blue_color));


        TabLayout tabLayout = findViewById(R.id.tablayout);
        ViewPager viewPager2 = findViewById(R.id.viewPager);

        tabLayout.setupWithViewPager(viewPager2);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.blue_900));

        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new Home_Fragment(), "Home");
        vpAdapter.addFragment(new Complain_Fragment(), "Complain");
        viewPager2.setAdapter(vpAdapter);
    }
}