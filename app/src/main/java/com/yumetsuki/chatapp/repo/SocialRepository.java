package com.yumetsuki.chatapp.repo;

import com.yumetsuki.chatapp.net.RxRetrofit;
import com.yumetsuki.chatapp.net.api.SocialService;
import com.yumetsuki.chatapp.net.protocol.CommonResponse;
import com.yumetsuki.chatapp.net.protocol.req.AcceptRequestReq;
import com.yumetsuki.chatapp.net.protocol.req.DenyRequestReq;
import com.yumetsuki.chatapp.net.protocol.resp.AcceptRequestMessage;
import com.yumetsuki.chatapp.net.protocol.resp.DenyRequestMessage;
import com.yumetsuki.chatapp.net.protocol.resp.FriendRequest;

import java.util.List;

import io.reactivex.Observable;

public class SocialRepository {

    private static SocialRepository socialRepository = new SocialRepository();

    private SocialService socialService = RxRetrofit.getInstance().create(SocialService.class);

    private SocialRepository() {}

    public static SocialRepository getInstance() {
        return socialRepository;
    }

    public Observable<CommonResponse<List<String>>> getAllFriends() {
        return socialService.getFriends();
    }

    public Observable<CommonResponse<List<FriendRequest>>> getAllFriendRequests() {
        return socialService.getFriendRequests();
    }

    public Observable<CommonResponse<AcceptRequestMessage>> acceptFriendRequest(int requestId) {
        AcceptRequestReq acceptRequestReq = new AcceptRequestReq();
        acceptRequestReq.setId(requestId);
        return socialService.acceptFriendRequest(acceptRequestReq);
    }

    public Observable<CommonResponse<DenyRequestMessage>> denyFriendRequest(int requestId) {
        DenyRequestReq denyRequestReq = new DenyRequestReq();
        denyRequestReq.setId(requestId);
        return socialService.denyFriendRequest(denyRequestReq);
    }
}
