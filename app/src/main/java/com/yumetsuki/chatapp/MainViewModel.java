package com.yumetsuki.chatapp;

import androidx.core.util.Pair;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yumetsuki.chatapp.model.Friend;
import com.yumetsuki.chatapp.model.FriendRequest;
import com.yumetsuki.chatapp.net.protocol.CommonResponse;
import com.yumetsuki.chatapp.net.protocol.resp.FriendMessage;
import com.yumetsuki.chatapp.repo.ChatRepository;
import com.yumetsuki.chatapp.repo.SocialRepository;
import com.yumetsuki.chatapp.utils.TipType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends ViewModel {

    private Map<UUID, Disposable> requests = new HashMap<>();

    private SocialRepository socialRepository = SocialRepository.getInstance();

    private ChatRepository chatRepository = ChatRepository.getInstance();

    private MutableLiveData<List<Friend>> friends = new MutableLiveData<>(new ArrayList<>());

    private MutableLiveData<Map<String, List<FriendMessage>>> friendsMessage = new MutableLiveData<>(new HashMap<>());

    private MutableLiveData<List<FriendRequest>> friendRequests = new MutableLiveData<>(new ArrayList<>());

    private MutableLiveData<Pair<TipType, String>> tip = new MutableLiveData<>();

    private Disposable timer;

    public MutableLiveData<Pair<TipType, String>> getTip() {
        return tip;
    }

    public MutableLiveData<List<Friend>> getFriends() {
        return friends;
    }

    public MutableLiveData<List<FriendRequest>> getFriendRequests() {
        return friendRequests;
    }

    public MutableLiveData<Map<String, List<FriendMessage>>> getFriendsMessage() {
        return friendsMessage;
    }

    public void startTimer() {
        if (timer == null) {
            timer = Observable.interval(1000, TimeUnit.MILLISECONDS)
                    .subscribe(r -> {
                        getAllFriends();
                        getFriendsRecentMessage();
                        getAllFriendRequests();
                    }, throwable -> {});
        }
    }

    public void stopTimer() {
        if (timer != null) {
            timer.dispose();
            timer = null;
        }
    }

    public void getAllFriendRequests() {
        UUID uuid = UUID.randomUUID();
        Disposable disposable = socialRepository.getAllFriendRequests()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetAllFriendRequests, this::onRequestError);

        requests.put(uuid, disposable);
    }

    public void getAllFriends() {
        UUID uuid = UUID.randomUUID();
        Disposable disposable = socialRepository.getAllFriends()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetAllFriends, this::onRequestError);
        requests.put(uuid, disposable);
    }

    public void getFriendsRecentMessage() {
        UUID uuid = UUID.randomUUID();
        Disposable disposable = Observable.fromIterable(
                Objects.requireNonNull(friends.getValue()).stream()
                .map(friend ->
                    chatRepository.getAllHistoryFriendMessage(friend.getName())
                        .map(it -> new Pair<>(friend.getName(), it))
                )
                .collect(Collectors.toList())
        ).flatMap(it -> it).toList().subscribe(this::onGetFriendsRecentMessage, this::onRequestError);
        requests.put(uuid, disposable);
    }

    private void onGetAllFriendRequests(CommonResponse<List<com.yumetsuki.chatapp.net.protocol.resp.FriendRequest>> response) {
        if (response.getStatus() == 0) {

            List<FriendRequest> friendRequestsValue = friendRequests.getValue();

            if (friendRequestsValue != null) {
                friendRequestsValue.clear();
                List<FriendRequest> modelRequests = response.getData().stream()
                        .map(respRequest -> {
                            FriendRequest modelRequest = new FriendRequest();
                            modelRequest.setId(respRequest.getId());
                            modelRequest.setName(respRequest.getFrom());
                            return modelRequest;
                        }).collect(Collectors.toList());
                friendRequestsValue.addAll(modelRequests);
                if (friendRequestsValue.size() == 0) return;
                friendRequests.postValue(friendRequestsValue);
            }

        } else {
            tip.postValue(
                    new Pair<>(TipType.ERROR, "Get friend request error")
            );
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void onGetFriendsRecentMessage(List<Pair<String, CommonResponse<List<FriendMessage>>>> responses) {
        if(responses.stream().allMatch(it -> Objects.requireNonNull(it.second).getStatus() == 0)) {
            Map<String, List<FriendMessage>> friendsMessageValue = friendsMessage.getValue();
            responses.forEach(pair -> {
                List<FriendMessage> messages = friendsMessageValue.get(pair.first);
                if (messages == null) {
                    friendsMessageValue.put(pair.first, pair.second.getData());
                } else {
                    friendsMessageValue.put(pair.first, pair.second.getData());
                }
            });
            friendsMessage.postValue(friendsMessageValue);
        } else {
            tip.postValue(
                    new Pair<>(TipType.ERROR, "get friends latest message error")
            );
        }
    }

    private void onGetAllFriends(CommonResponse<List<String>> response) {
        if (response.getStatus() == 0) {
            if (Objects.requireNonNull(friends.getValue()).size() == response.getData().size()) return;
            List<Friend> friendsValue = friends.getValue();
            friendsValue.addAll(
                response.getData().stream()
                    .filter(it -> friendsValue.stream().noneMatch(friend -> friend.getName().equals(it)))
                    .map(name -> {
                        Friend friend = new Friend();
                        friend.setName(name);
                        friend.setRecentMessage("no message");
                        friend.setRecentMessageTime(Calendar.getInstance().getTimeInMillis());
                        return friend;
                    }).collect(Collectors.toList())
            );
            friends.postValue(friendsValue);
        } else {
            tip.postValue(
                    new Pair<>(TipType.ERROR, "get friends error")
            );
        }
    }

    private void onRequestError(Throwable throwable) {
        tip.postValue(
                new Pair<>(TipType.ERROR, "server error")
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        requests.forEach((uuid, disposable) -> disposable.dispose());
        requests.clear();
    }
}
