package com.yumetsuki.chatapp.pages.login;

import android.content.Context;

import androidx.core.util.Pair;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yumetsuki.chatapp.App;
import com.yumetsuki.chatapp.repo.AuthRepository;
import com.yumetsuki.chatapp.utils.EncryptionUtils;
import com.yumetsuki.chatapp.utils.TipType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {

    private List<Disposable> requests = new ArrayList<>();

    private AuthRepository authRepository = AuthRepository.getInstance();

    private MutableLiveData<Boolean> inLoginOrRegister = new MutableLiveData<>(false);

    private MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>(false);

    private MutableLiveData<Boolean> registerSuccess = new MutableLiveData<>(false);

    private MutableLiveData<Pair<TipType, String>> tip = new MutableLiveData<>();

    public MutableLiveData<Boolean> isInLoginOrRegister() {
        return inLoginOrRegister;
    }

    public MutableLiveData<Boolean> isLoginSuccess() {
        return loginSuccess;
    }

    public MutableLiveData<Boolean> isRegisterSuccess() {
        return registerSuccess;
    }

    public MutableLiveData<Pair<TipType, String>> getTip() {
        return tip;
    }

    public void login(String username, String password) {
        Disposable request = authRepository.login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> inLoginOrRegister.postValue(true))
                .subscribe(response -> {
                    inLoginOrRegister.postValue(false);
                    if (Objects.requireNonNull(response.body()).getStatus() == 0) {
                        App.AppContext.getSharedPreferences("default", Context.MODE_PRIVATE)
                                .edit()
                                .putString("cookie", response.headers().get("Set-Cookie"))
                                .putString("username", username)
                                .apply();
                        EncryptionUtils.secretKey = EncryptionUtils.generateSecreteKey(response.body().getData().getPublic_key());
                        loginSuccess.postValue(true);
                        tip.postValue(
                                new Pair<>(TipType.SUCCESS, "login success")
                        );
                    } else {
                        tip.postValue(new Pair<>(TipType.ERROR, response.body().getData().getMsg()));
                    }
                }, throwable -> {
                    inLoginOrRegister.postValue(false);
                    tip.postValue(
                            new Pair<>(TipType.ERROR, "server error")
                    );
                });
        requests.add(request);
    }

    public void register(String username, String password) {
        Disposable request = authRepository.register(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> inLoginOrRegister.postValue(true))
                .subscribe(response -> {
                    inLoginOrRegister.postValue(false);
                    if (response.getStatus() == 0) {
                        registerSuccess.postValue(true);
                        tip.postValue(
                                new Pair<>(TipType.SUCCESS, "register success")
                        );
                    } else {
                        tip.postValue(new Pair<>(TipType.ERROR, response.getData().getMsg()));
                    }
                }, throwable -> {
                    inLoginOrRegister.postValue(false);
                    tip.postValue(
                            new Pair<>(TipType.ERROR, "server error")
                    );
                });
        requests.add(request);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        requests.forEach(Disposable::dispose);
        requests.clear();
    }
}
