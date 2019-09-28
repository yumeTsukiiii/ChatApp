package com.yumetsuki.chatapp.pages.home.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yumetsuki.chatapp.adapters.FriendListAdapter;
import com.yumetsuki.chatapp.model.Friend;
import com.yumetsuki.chatapp.repo.SocialRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ContactsTabViewModel extends ViewModel {

    private List<Disposable> requests = new ArrayList<>();

    private FriendListAdapter friendListAdapter;

    public ContactsTabViewModel() {
    }

    public void setFriendListAdapter(FriendListAdapter friendListAdapter) {
        this.friendListAdapter = friendListAdapter;
    }

    public FriendListAdapter getFriendListAdapter() {
        return friendListAdapter;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        requests.forEach(Disposable::dispose);
        requests.clear();
    }
}
