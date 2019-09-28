package com.yumetsuki.chatapp.pages.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yumetsuki.chatapp.MainViewModel;
import com.yumetsuki.chatapp.R;
import com.yumetsuki.chatapp.adapters.NewFriendRequestAdapter;
import com.yumetsuki.chatapp.pages.home.viewmodel.NewFriendMessageTabViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NewFriendMessageTabFragment extends Fragment {

    @BindView(R.id.new_friend_list_recycler_view)
    RecyclerView mNewFriendListRecyclerView;

    private NewFriendMessageTabViewModel viewModel;

    private MainViewModel mainViewModel;

    private Unbinder unbinder;

    public static NewFriendMessageTabFragment newInstance() {
        return new NewFriendMessageTabFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_view_new_friend_message, container, false);
        if (viewModel == null) {
            viewModel = new ViewModelProvider(this)
                    .get(NewFriendMessageTabViewModel.class);
        }

        if (mainViewModel == null) {
            mainViewModel = new ViewModelProvider(requireActivity())
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
        mainViewModel.getFriendRequests().observe(this, friendRequests -> {
            if (viewModel.getNewFriendRequestAdapter() != null) {
                viewModel.getNewFriendRequestAdapter().notifyDataSetChanged();
            }
        });
        viewModel.getTip().observe(this, tip -> {
            if (tip != null) {
                Toast.makeText(getContext(), tip.second, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        NewFriendRequestAdapter newFriendRequestAdapter = new NewFriendRequestAdapter(
                mainViewModel.getFriendRequests(),
                acceptRequest -> viewModel.acceptFriendRequest(acceptRequest),
                denyRequest -> viewModel.denyFriendRequest(denyRequest)
        );
        viewModel.setNewFriendRequestAdapter(newFriendRequestAdapter);
        mNewFriendListRecyclerView.setAdapter(newFriendRequestAdapter);
        mNewFriendListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
