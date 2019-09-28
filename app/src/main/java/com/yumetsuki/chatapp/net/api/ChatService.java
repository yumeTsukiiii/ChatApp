package com.yumetsuki.chatapp.net.api;

import com.yumetsuki.chatapp.net.protocol.CommonResponse;
import com.yumetsuki.chatapp.net.protocol.req.SendMessageReq;
import com.yumetsuki.chatapp.net.protocol.resp.FriendMessage;
import com.yumetsuki.chatapp.net.protocol.resp.SendChatMessage;


import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChatService {

    @POST("chat/message/send")
    Observable<CommonResponse<SendChatMessage>> sendMessage(@Body SendMessageReq req);

    @GET("chat/message/all/{username}")
    Observable<CommonResponse<List<FriendMessage>>> getAllHistoryFriendMessage(@Path("username") String username);

    @GET("chat/message/after/{username}/{id}")
    Observable<CommonResponse<List<FriendMessage>>> getAfterFriendMessageById(@Path("username") String username, @Path("id") int id);

}
