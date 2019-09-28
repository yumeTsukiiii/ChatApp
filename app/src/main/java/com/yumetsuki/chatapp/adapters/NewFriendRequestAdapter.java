package com.yumetsuki.chatapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.yumetsuki.chatapp.R;
import com.yumetsuki.chatapp.model.FriendRequest;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class NewFriendRequestAdapter extends RecyclerView.Adapter<NewFriendRequestAdapter.NewFriendRequestViewHolder> {

    private MutableLiveData<List<FriendRequest>> friendRequests;
    private Consumer<FriendRequest> onAccept;
    private Consumer<FriendRequest> onDeny;

    public NewFriendRequestAdapter(
        MutableLiveData<List<FriendRequest>> friendRequests,
        Consumer<FriendRequest> onAccept,
        Consumer<FriendRequest> onDeny
    ) {
        this.friendRequests = friendRequests;
        this.onAccept = onAccept;
        this.onDeny = onDeny;
    }

    @NonNull
    @Override
    public NewFriendRequestAdapter.NewFriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewFriendRequestViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_new_friend_request_adapter, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NewFriendRequestAdapter.NewFriendRequestViewHolder holder, int position) {
        holder.bind(Objects.requireNonNull(friendRequests.getValue()).get(position));
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(friendRequests.getValue()).size();
    }

    class NewFriendRequestViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.avatar_friend_request)
        CircleImageView mAvatarFriendRequest;

        @BindView(R.id.friend_request_name_text)
        TextView mFriendRequestNameText;

        @BindView(R.id.request_message_text)
        TextView mRequestMessageText;

        @BindView(R.id.accept_request_btn)
        MaterialButton mAcceptRequestBtn;

        @BindView(R.id.deny_request_btn)
        MaterialButton mDenyRequestBtn;

        public NewFriendRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(FriendRequest friendRequest) {
            mFriendRequestNameText.setText(friendRequest.getName());
            mAcceptRequestBtn.setOnClickListener(view -> {
                onAccept.accept(friendRequest);
            });
            mDenyRequestBtn.setOnClickListener(view -> {
                onDeny.accept(friendRequest);
            });
        }

    }
}
