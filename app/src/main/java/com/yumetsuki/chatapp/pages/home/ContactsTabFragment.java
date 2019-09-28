package com.yumetsuki.chatapp.pages.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.yumetsuki.chatapp.MainViewModel;
import com.yumetsuki.chatapp.R;
import com.yumetsuki.chatapp.adapters.FriendListAdapter;
import com.yumetsuki.chatapp.pages.home.viewmodel.ContactsTabViewModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ContactsTabFragment extends Fragment {

    @BindView(R.id.friend_list_recycler_view)
    RecyclerView mFriendListRecyclerView;

    private Unbinder unbinder;

    private ContactsTabViewModel viewModel;

    private MainViewModel mainViewModel;

    public static ContactsTabFragment newInstance() {
        return new ContactsTabFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_view_contacts, container, false);
        if (viewModel == null) {
            viewModel = new ViewModelProvider(this)
                    .get(ContactsTabViewModel.class);
        }

        if (mainViewModel == null) {
            mainViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                    .get(MainViewModel.class);
        }

        unbinder = ButterKnife.bind(this, view);

        initViewModel();

        initView();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initViewModel() {
        mainViewModel.getFriends().removeObservers(this);

        mainViewModel.getFriendsMessage().observe(this, friends -> {
            if (viewModel.getFriendListAdapter() != null) {
                viewModel.getFriendListAdapter().notifyDataSetChanged();
            }
        });
    }

    private void initView() {
        FriendListAdapter adapter = new FriendListAdapter(mainViewModel.getFriends());
        viewModel.setFriendListAdapter(adapter);
        mFriendListRecyclerView.setAdapter(adapter);
        mFriendListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
