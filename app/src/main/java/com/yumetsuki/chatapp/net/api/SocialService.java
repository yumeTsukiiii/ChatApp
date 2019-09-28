package com.yumetsuki.chatapp.net.api;

import com.yumetsuki.chatapp.net.protocol.CommonResponse;
import com.yumetsuki.chatapp.net.protocol.req.AcceptRequestReq;
import com.yumetsuki.chatapp.net.protocol.req.AddFriendReq;
import com.yumetsuki.chatapp.net.protocol.req.DeleteFriendReq;
import com.yumetsuki.chatapp.net.protocol.req.DenyRequestReq;
import com.yumetsuki.chatapp.net.protocol.resp.AcceptRequestMessage;
import com.yumetsuki.chatapp.net.protocol.resp.AddFriendMessage;
import com.yumetsuki.chatapp.net.protocol.resp.DeleteFriendMessage;
import com.yumetsuki.chatapp.net.protocol.resp.DenyRequestMessage;
import com.yumetsuki.chatapp.net.protocol.resp.FriendRequest;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface SocialService {

    @POST("social/request")
    Observable<CommonResponse<AddFriendMessage>> addFriend(@Body AddFriendReq req);

    @GET("social/request/all")
    Observable<CommonResponse<List<FriendRequest>>> getFriendRequests();

    @GET("social/friends/all")
    Observable<CommonResponse<List<String>>> getFriends();

    @POST("social/request/accept")
    Observable<CommonResponse<AcceptRequestMessage>> acceptFriendRequest(@Body AcceptRequestReq req);

    @POST("social/request/deny")
    Observable<CommonResponse<DenyRequestMessage>> denyFriendRequest(@Body DenyRequestReq requestReq);

    @POST("social/friends/delete")
    Observable<CommonResponse<DeleteFriendMessage>> deleteFriend(@Body DeleteFriendReq req);

}
