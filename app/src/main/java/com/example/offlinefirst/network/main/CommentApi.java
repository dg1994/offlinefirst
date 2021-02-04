package com.example.offlinefirst.network.main;

import com.example.offlinefirst.model.Comment;
import com.example.offlinefirst.model.Post;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CommentApi {
    @POST("/comments")
    Single<Comment> addComment(@Body Comment comment);

    @GET("comments/response")
    Flowable<List<Comment>> getCommentsFromUser(@Query("userId") int id);

    // /posts?userId=1/
    @GET("posts")
    Call<List<Post>> getPostsFromUser(@Query("userId") int id);
}
