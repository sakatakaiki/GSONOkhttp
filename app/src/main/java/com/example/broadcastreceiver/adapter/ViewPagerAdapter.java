package com.example.broadcastreceiver.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.broadcastreceiver.fragments.OkHttpFragment;
import com.example.broadcastreceiver.fragments.RetrofitFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return position == 0 ? new OkHttpFragment() : new RetrofitFragment();
    }

    @Override
    public int getItemCount() {
        return 2; // Có 2 tab
    }
}
