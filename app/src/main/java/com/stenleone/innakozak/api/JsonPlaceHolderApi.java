package com.stenleone.innakozak.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {

    @GET("users")
    Call<List<PostUser>> getPostsUser();

    @GET("repos")
    Call<List<PostRepos>> getPostsRepos();
}