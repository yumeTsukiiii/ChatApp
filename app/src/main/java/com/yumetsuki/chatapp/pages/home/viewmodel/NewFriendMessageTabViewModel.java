package com.yumetsuki.chatapp.pages.home.viewmodel;

import androidx.core.util.Pair;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yumetsuki.chatapp.adapters.NewFriendRequestAdapter;
import com.yumetsuki.chatapp.model.FriendRequest;
import com.yumetsuki.chatapp.net.protocol.CommonResponse;
import com.yumetsuki.chatapp.net.protocol.resp.AcceptRequestMessage;
import com.yumetsuki.chatapp.net.protocol.resp.DenyRequestMessage;
import com.yumetsuki.chatapp.repo.SocialRepository;
import com.yumetsuki.chatapp.utils.TipType;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewFriendMessageTabViewModel extends ViewModel {

    private NewFriendRequestAdapter newFriendRequestAdapter;

    private SocialRepository socialRepository = SocialRepository.getInstance();

    private MutableLiveData<Pair<TipType, String>> tip = new MutableLiveData<>();

    public NewFriendRequestAdapter getNewFriendRequestAdapter() {
        return newFriendRequestAdapter;
    }

    public void setNewFriendRequestAdapter(NewFriendRequestAdapter newFriendRequestAdapter) {
        this.newFriendRequestAdapter = newFriendRequestAdapter;
    }

    public MutableLiveData<Pair<TipType, String>> getTip() {
        return tip;
    }

    public void acceptFriendRequest(FriendRequest friendRequest) {
        Disposable disposable = socialRepository.acceptFriendRequest(friendRequest.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onAcceptFriendRequest, this::onServerError);
    }

    public void denyFriendRequest(FriendRequest friendRequest) {
        Disposable disposable = socialRepository.denyFriendRequest(friendRequest.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onDenyFriendRequest, this::onServerError);
    }

    private void onAcceptFriendRequest(CommonResponse<AcceptRequestMessage> response) {
        if (response.getStatus() == 0) {
            tip.postValue(new Pair<>(TipType.SUCCESS, "accept success"));
        } else {
            tip.postValue(new Pair<>(TipType.ERROR, response.getData().getMsg()));
        }
    }

    private void onDenyFriendRequest(CommonResponse<DenyRequestMessage> response) {
        if (response.getStatus() == 0) {
            tip.postValue(new Pair<>(TipType.SUCCESS, "deny success"));
        } else {
            tip.postValue(new Pair<>(TipType.ERROR, response.getData().getMsg()));
        }
    }

    private void onServerError(Throwable throwable) {
        tip.postValue(new Pair<>(TipType.ERROR, "server error"));
    }
}
