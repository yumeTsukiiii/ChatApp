package com.yumetsuki.chatapp.repo;

import android.content.Context;

import com.yumetsuki.chatapp.App;
import com.yumetsuki.chatapp.R;
import com.yumetsuki.chatapp.net.RxRetrofit;
import com.yumetsuki.chatapp.net.api.AuthService;
import com.yumetsuki.chatapp.net.protocol.CommonResponse;
import com.yumetsuki.chatapp.net.protocol.req.LoginReq;
import com.yumetsuki.chatapp.net.protocol.req.RegisterReq;
import com.yumetsuki.chatapp.net.protocol.resp.LoginMessage;
import com.yumetsuki.chatapp.net.protocol.resp.RegisterMessage;
import com.yumetsuki.chatapp.utils.EncryptionUtils;

import io.reactivex.Observable;
import retrofit2.HttpException;
import retrofit2.Response;

public class AuthRepository {

    private static final AuthRepository ourInstance = new AuthRepository();

    public static AuthRepository getInstance() {
        return ourInstance;
    }

    private AuthRepository() {}

    private AuthService authService = RxRetrofit.getInstance().create(AuthService.class);

    public Observable<Response<CommonResponse<LoginMessage>>> login(
            String username,
            String password
    ) {
        LoginReq req = new LoginReq();
        req.setUsername(username);
        req.setPassword(password);
        req.setPublic_key(EncryptionUtils.generatePublicKey());
        return authService.login(req).map(response -> {
            if (response.code() != 200) {
                throw new HttpException(response);
            }
            return response;
        });
    }

    public Observable<CommonResponse<RegisterMessage>> register(
            String username,
            String password
    ) {
        RegisterReq req = new RegisterReq();
        req.setUsername(username);
        req.setPassword(password);
        return authService.register(req);
    }

}
