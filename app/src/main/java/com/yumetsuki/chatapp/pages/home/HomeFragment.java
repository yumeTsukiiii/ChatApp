package com.yumetsuki.chatapp.pages.home;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.yumetsuki.chatapp.R;
import com.yumetsuki.chatapp.adapters.HomePageTabPagesAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends Fragment {

    @BindView(R.id.toolbar_home)
    Toolbar mHomeToolbar;

    @BindView(R.id.tab_layout_home)
    TabLayout mHomeTabLayout;

    @BindView(R.id.appbar_layout_home)
    AppBarLayout mHomeAppBarLayout;

    @BindView(R.id.view_pager_home)
    ViewPager mHomeViewPager;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Objects.requireNonNull(activity).setSupportActionBar(mHomeToolbar);

        initView();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initView() {
        mHomeViewPager.setAdapter(new HomePageTabPagesAdapter(
                Objects.requireNonNull(getActivity()).getSupportFragmentManager(),
                buildViewPagerFragments()
        ));
        mHomeTabLayout.setupWithViewPager(mHomeViewPager);
    }

    private List<Pair<String, Fragment>> buildViewPagerFragments() {
        List<Pair<String, Fragment>> fragmentsWithTitle = new ArrayList<>();
        fragmentsWithTitle.add(
                new Pair<>("Contact", ContactsTabFragment.newInstance())
        );
        fragmentsWithTitle.add(
                new Pair<>("New Friend", NewFriendMessageTabFragment.newInstance())
        );
        return fragmentsWithTitle;
    }

}
