package com.yumetsuki.chatapp.net.api;

import com.yumetsuki.chatapp.net.protocol.CommonResponse;
import com.yumetsuki.chatapp.net.protocol.req.LoginReq;
import com.yumetsuki.chatapp.net.protocol.req.RegisterReq;
import com.yumetsuki.chatapp.net.protocol.resp.LoginMessage;
import com.yumetsuki.chatapp.net.protocol.resp.RegisterMessage;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST("auth/login")
    Observable<Response<CommonResponse<LoginMessage>>> login(@Body LoginReq req);

    @POST("auth/register")
    Observable<CommonResponse<RegisterMessage>> register(@Body RegisterReq req);

}
