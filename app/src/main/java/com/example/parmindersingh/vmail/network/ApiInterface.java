package com.example.parmindersingh.vmail.network;

import com.example.parmindersingh.vmail.model.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public class ApiInterface {
    @GET("inbox.json")
    public Call<List<Message>> getInbox() {

        return null;
    }
}
