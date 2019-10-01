package com.yumetsuki.chatapp.repo;

import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yumetsuki.chatapp.net.RxRetrofit;
import com.yumetsuki.chatapp.net.api.ChatService;
import com.yumetsuki.chatapp.net.protocol.CommonResponse;
import com.yumetsuki.chatapp.net.protocol.req.SendMessageReq;
import com.yumetsuki.chatapp.net.protocol.resp.FriendMessage;
import com.yumetsuki.chatapp.net.protocol.resp.SendChatMessage;
import com.yumetsuki.chatapp.utils.EncryptionUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;

public class ChatRepository
{
    private static final ChatRepository ourInstance = new ChatRepository();

    public static ChatRepository getInstance() {
        return ourInstance;
    }

    private ChatRepository() {
    }

    private ChatService chatService = RxRetrofit.getInstance().create(ChatService.class);

    public Observable<CommonResponse<List<FriendMessage>>> getAllHistoryFriendMessage(String username) {
        return chatService.getAllHistoryFriendMessage(username)
                .map(response -> response.getData().getMsg())
                .map(text -> {
                    byte[] encryptedContent = Base64.decode(text, Base64.DEFAULT);
                    byte[] decryptedContent = EncryptionUtils.DES_CBC_Decrypt(encryptedContent, EncryptionUtils.secretKey.getBytes());
                    String plainText = new String(decryptedContent);
                    JsonObject object = new JsonParser().parse(plainText).getAsJsonObject();
                    CommonResponse<List<FriendMessage>> commonResponse = new CommonResponse<>();
                    List<FriendMessage> list = new ArrayList<>();
                    object.get("data").getAsJsonArray().forEach(jsonElement -> {
                        JsonObject item = jsonElement.getAsJsonObject();
                        FriendMessage message = new FriendMessage();
                        message.setContent(item.get("content").getAsString());
                        message.setFrom(item.get("from").getAsString());
                        message.setTo(item.get("to").getAsString());
                        message.setTime(item.get("time").getAsLong());
                        message.setId(item.get("id").getAsInt());
                        list.add(message);
                    });
                    commonResponse.setData(list);
                    commonResponse.setStatus(object.get("status").getAsInt());
                    return commonResponse;
                }).doOnError(Throwable::printStackTrace);
    }

    public Observable<CommonResponse<SendChatMessage>> sendMessage(String username, String text) {
        SendMessageReq req = new SendMessageReq();
        req.setTo(username);
        req.setContent(text);
        String plainText = new Gson().toJson(req);
        byte[] encryptContent = EncryptionUtils.DES_CBC_Encrypt(plainText.getBytes(), EncryptionUtils.secretKey.getBytes());
        String base64Content = Base64.encodeToString(encryptContent, Base64.NO_WRAP);
        return chatService.sendMessage(base64Content);
    }
}
