package com.yumetsuki.chatapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.yumetsuki.chatapp.R;
import com.yumetsuki.chatapp.model.Friend;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class FriendListAdapter
        extends RecyclerView.Adapter<FriendListAdapter.FriendListViewHolder> {

    private MutableLiveData<List<Friend>> friends;

    public FriendListAdapter(MutableLiveData<List<Friend>> friends) {
        this.friends = friends;
    }

    @NonNull
    @Override
    public FriendListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FriendListViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_friend_list_adapter, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull FriendListViewHolder holder, int position) {
        holder.bind(Objects.requireNonNull(friends.getValue()).get(position));
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(friends.getValue()).size();
    }

    class FriendListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.avatar_friend)
        CircleImageView mFriendAvatar;

        @BindView(R.id.friend_name_text)
        TextView mFriendNameText;

        @BindView(R.id.recent_message_text_view)
        TextView mRecentMessageText;

        @BindView(R.id.time_recent_message_text_view)
        TextView mTimeRecentMessageText;

        FriendListViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Friend friend) {
            mFriendNameText.setText(friend.getName());
            mRecentMessageText.setText(friend.getRecentMessage());
            mTimeRecentMessageText.setText(getFormatRecentTime(friend.getRecentMessageTime()));
        }

        private String getFormatRecentTime(long time) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            return calendar.get(Calendar.HOUR_OF_DAY) +
                    ":" +
                    calendar.get(Calendar.MINUTE) +
                    " " +
                    calendar.get(Calendar.YEAR) +
                    "/" +
                    (calendar.get(Calendar.MONTH) + 1) +
                    "/" +
                    calendar.get(Calendar.DAY_OF_MONTH);
        }
    }
}
