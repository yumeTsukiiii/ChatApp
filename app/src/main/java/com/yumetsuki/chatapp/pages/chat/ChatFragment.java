package com.yumetsuki.chatapp.pages.chat;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.yumetsuki.chatapp.App;
import com.yumetsuki.chatapp.MainViewModel;
import com.yumetsuki.chatapp.R;
import com.yumetsuki.chatapp.adapters.ChatMessageAdapter;

import java.util.Objects;

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

    @BindView(R.id.toolbar_chat)
    Toolbar mChatToolbar;

    @BindView(R.id.title_text_view)
    TextView mTitleTextView;

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

        AppCompatActivity activity = (AppCompatActivity) requireActivity();

        activity.setSupportActionBar(mChatToolbar);

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
        viewModel.setCurrentChatUsername(Objects.requireNonNull(getArguments()).getString("username", ""));
        mTitleTextView.setText(viewModel.getCurrentChatUsername());
        viewModel.getTip().removeObservers(this);
        viewModel.getMessages().removeObservers(this);
        mainViewModel.getFriendsMessage().removeObservers(this);

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
            if (friendMessages.get(viewModel.getCurrentChatUsername()) == null){
                return;
            }
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
        mChatMessageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @OnClick(R.id.send_message_btn)
    public void onMSendMessageBtnClick() {
        if (mSendMessageEdit.getText() == null || mSendMessageEdit.getText().toString().isEmpty()) return;
        viewModel.sendMessage(mSendMessageEdit.getText().toString());
        mChatMessageRecyclerView.scrollToPosition(Objects.requireNonNull(viewModel.getMessages().getValue()).size() - 1);
        mSendMessageEdit.setText("");
    }
}
