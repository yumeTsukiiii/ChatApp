package com.yumetsuki.chatapp.repo;

import com.yumetsuki.chatapp.net.RxRetrofit;
import com.yumetsuki.chatapp.net.api.ChatService;
import com.yumetsuki.chatapp.net.protocol.CommonResponse;
import com.yumetsuki.chatapp.net.protocol.req.SendMessageReq;
import com.yumetsuki.chatapp.net.protocol.resp.FriendMessage;
import com.yumetsuki.chatapp.net.protocol.resp.SendChatMessage;

import java.util.List;

import io.reactivex.Observable;

public class ChatRepository
{
    private static final ChatRepository ourInstance = new ChatRepository();

    public static ChatRepository getInstance() {
        return ourInstance;
    }

    private ChatRepository() {
    }

    private ChatService chatService = RxRetrofit.getInstance().create(ChatService.class);

    public Observable<CommonResponse<List<FriendMessage>>> getAllHistoryFriendMessage(String username) {
        return chatService.getAllHistoryFriendMessage(username);
    }

    public Observable<CommonResponse<SendChatMessage>> sendMessage(String username, String text) {
        SendMessageReq req = new SendMessageReq();
        req.setTo(username);
        req.setContent(text);
        return chatService.sendMessage(req);
    }
}
