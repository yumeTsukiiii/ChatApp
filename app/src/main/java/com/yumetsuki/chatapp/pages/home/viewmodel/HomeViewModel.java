package com.yumetsuki.chatapp.pages.home.viewmodel;

import androidx.core.util.Pair;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yumetsuki.chatapp.net.protocol.CommonResponse;
import com.yumetsuki.chatapp.net.protocol.resp.AddFriendMessage;
import com.yumetsuki.chatapp.repo.SocialRepository;
import com.yumetsuki.chatapp.utils.TipType;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends ViewModel {

    private SocialRepository socialRepository = SocialRepository.getInstance();

    private MutableLiveData<Pair<TipType, String>> tip = new MutableLiveData<>();


    public MutableLiveData<Pair<TipType, String>> getTip() {
        return tip;
    }

    public void addFriend(String username) {
        Disposable disposable = socialRepository.addFriend(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onAddFriend, this::onServerError);
    }

    private void onAddFriend(CommonResponse<AddFriendMessage> response) {
        if (response.getStatus() == 0) {
            tip.postValue(
                    new Pair<>(TipType.SUCCESS, "add friend success")
            );
        } else {
            tip.postValue(
                    new Pair<>(TipType.ERROR, response.getData().getMsg())
            );
        }
    }

    private void onServerError(Throwable throwable) {
        tip.postValue(new Pair<>(TipType.ERROR, "server error"));
    }
}
