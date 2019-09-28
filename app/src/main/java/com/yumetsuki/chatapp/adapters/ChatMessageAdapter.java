package com.yumetsuki.chatapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.yumetsuki.chatapp.R;
import com.yumetsuki.chatapp.net.protocol.resp.FriendMessage;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ChatMessageViewHolder> {

    private String hostUsername;

    private MutableLiveData<List<FriendMessage>> messages;

    public ChatMessageAdapter(String hostUsername, MutableLiveData<List<FriendMessage>> messages) {
        this.hostUsername = hostUsername;
        this.messages = messages;
    }

    @NonNull
    @Override
    public ChatMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatMessageViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(viewType == 0 ? R.layout.item_own_text_message_adapter : R.layout.item_others_text_message_adapter
                                , parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageViewHolder holder, int position) {
        holder.bind(Objects.requireNonNull(messages.getValue()).get(position));
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(messages.getValue()).size();
    }

    @Override
    public int getItemViewType(int position) {
        return Objects.requireNonNull(messages.getValue()).get(position).getFrom().equals(hostUsername) ? 1 : 0;
    }

    class ChatMessageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.avatar_chat)
        CircleImageView mChatAvatar;
        @BindView(R.id.message_text_view)
        TextView mMessageTv;
        @BindView(R.id.time_chat_message)
        TextView mChatMessageTime;

        public ChatMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(FriendMessage message) {
            mMessageTv.setText(message.getContent());
            mChatMessageTime.setText(buildTimeText(message.getTime()));
        }

        private String buildTimeText(long time) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        }
    }
}
