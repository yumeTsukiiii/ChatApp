package com.yumetsuki.chatapp.pages.home;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.yumetsuki.chatapp.R;
import com.yumetsuki.chatapp.adapters.HomePageTabPagesAdapter;
import com.yumetsuki.chatapp.pages.home.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    @BindView(R.id.open_add_friend_btn)
    FloatingActionButton mOpenAddFriendBtn;

    private Unbinder unbinder;

    private HomeViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        if (viewModel == null) {
            viewModel = new ViewModelProvider(this)
                    .get(HomeViewModel.class);
        }

        unbinder = ButterKnife.bind(this, view);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Objects.requireNonNull(activity).setSupportActionBar(mHomeToolbar);

        initViewModel();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initViewModel() {
        viewModel.getTip().removeObservers(this);

        viewModel.getTip().observe(this, tip -> {
            if (tip != null) {
                Toast.makeText(getContext(), tip.second, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        mHomeViewPager.setAdapter(new HomePageTabPagesAdapter(
                getChildFragmentManager(),
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

    @OnClick(R.id.open_add_friend_btn)
    public void onMOpenAddFriendBtnClick() {
        View view = View.inflate(getContext(), R.layout.dialog_add_friend, null);
        new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                .setTitle("添加好友")
                .setView(view)
                .setPositiveButton("确认", (dialogInterface, i) -> {
                    TextInputEditText editText = view.findViewById(R.id.add_friend_edit);
                    if (editText.getText() != null && !editText.getText().toString().isEmpty()) {
                        viewModel.addFriend(editText.getText().toString());
                    } else {
                        Toast.makeText(getContext(), "username can not be empty", Toast.LENGTH_SHORT).show();
                    }
                    dialogInterface.cancel();
                }).show();
    }

}
