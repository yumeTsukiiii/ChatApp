package com.yumetsuki.chatapp.pages.chat;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.yumetsuki.chatapp.App;
import com.yumetsuki.chatapp.MainViewModel;
import com.yumetsuki.chatapp.R;
import com.yumetsuki.chatapp.adapters.ChatMessageAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ChatFragment extends Fragment {

    @BindView(R.id.appbar_chat)
    AppBarLayout mChatAppBar;

    @BindView(R.id.chat_message_recycler_view)
    RecyclerView mChatMessageRecyclerView;

    @BindView(R.id.send_message_edit)
    EditText mSendMessageEdit;

    @BindView(R.id.send_message_btn)
    ImageButton mSendMessageBtn;

    @BindView(R.id.bottom_send_message_appbar)
    BottomAppBar mBottomSendMessageAppbar;

    private Unbinder unbinder;

    private ChatViewModel viewModel;

    private MainViewModel mainViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        if (viewModel == null) {
            viewModel = new ViewModelProvider(this)
                    .get(ChatViewModel.class);
        }

        if (mainViewModel == null) {
            mainViewModel = new ViewModelProvider(requireActivity())
                    .get(MainViewModel.class);
        }

        initViewModel();
        initView();

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initViewModel() {
        viewModel.getTip().observe(this, tip -> {
            if (tip != null) {
                Toast.makeText(getContext(), tip.second, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getMessages().observe(this, messages -> {
            if (viewModel.getChatMessageAdapter() != null) {
                viewModel.getChatMessageAdapter().notifyDataSetChanged();
            }
        });

        mainViewModel.getFriendsMessage().observe(this, friendMessages -> {
            viewModel.getMessages().setValue(friendMessages.get(viewModel.getCurrentChatUsername()));
        });
    }

    private void initView() {
        ChatMessageAdapter chatMessageAdapter = new ChatMessageAdapter(
            App.AppContext.getSharedPreferences("default", Context.MODE_PRIVATE)
                .getString("username", ""),
            viewModel.getMessages()
        );
        viewModel.setChatMessageAdapter(chatMessageAdapter);
        mChatMessageRecyclerView.setAdapter(chatMessageAdapter);
    }

    @OnClick(R.id.send_message_btn)
    public void onMSendMessageBtnClick() {
        if (mSendMessageEdit.getText() == null || mSendMessageEdit.getText().toString().isEmpty()) return;
        viewModel.sendMessage(mSendMessageEdit.getText().toString());
        mSendMessageEdit.setText("");
    }
}
