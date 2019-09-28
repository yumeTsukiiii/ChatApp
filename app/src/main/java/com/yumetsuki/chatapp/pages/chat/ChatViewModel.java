package com.yumetsuki.chatapp.pages.chat;

import androidx.core.util.Pair;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yumetsuki.chatapp.adapters.ChatMessageAdapter;
import com.yumetsuki.chatapp.net.protocol.CommonResponse;
import com.yumetsuki.chatapp.net.protocol.resp.FriendMessage;
import com.yumetsuki.chatapp.net.protocol.resp.SendChatMessage;
import com.yumetsuki.chatapp.repo.ChatRepository;
import com.yumetsuki.chatapp.utils.TipType;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ChatViewModel extends ViewModel {

    private ChatRepository chatRepository = ChatRepository.getInstance();

    private MutableLiveData<Pair<TipType, String>> tip = new MutableLiveData<>();

    private MutableLiveData<List<FriendMessage>> messages = new MutableLiveData<>(new ArrayList<>());

    private String currentChatUsername;

    private ChatMessageAdapter chatMessageAdapter;

    public MutableLiveData<Pair<TipType, String>> getTip() {
        return tip;
    }

    public MutableLiveData<List<FriendMessage>> getMessages() {
        return messages;
    }

    public String getCurrentChatUsername() {
        return currentChatUsername;
    }

    public void setCurrentChatUsername(String currentChatUsername) {
        this.currentChatUsername = currentChatUsername;
    }

    public ChatMessageAdapter getChatMessageAdapter() {
        return chatMessageAdapter;
    }

    public void setChatMessageAdapter(ChatMessageAdapter chatMessageAdapter) {
        this.chatMessageAdapter = chatMessageAdapter;
    }

    public void sendMessage(String message) {
        Disposable disposable = chatRepository.sendMessage(
                currentChatUsername,
                message
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSendMessage, this::onServerError);
    }

    private void onSendMessage(CommonResponse<SendChatMessage> response) {
        if (response.getStatus() == 0) {

        } else {
            tip.postValue(
                    new Pair<>(TipType.ERROR, response.getData().getMsg())
            );
        }
    }

    private void onServerError(Throwable throwable) {
        tip.postValue(
                new Pair<>(TipType.ERROR, "server error")
        );
    }

}
