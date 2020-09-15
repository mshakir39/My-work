package com.example.poetrious.Services;

import com.example.poetrious.Notification.MyResponse;
import com.example.poetrious.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAIsOrLGo:APA91bGTC7aS4Q8r6d4xIx81RDYsB9NIqtoZODpPuYZ0qPyeUSxU9zJ394yHGXpH1EeW8ZAdxzn7JmkKEs_pLlqMKg9QqxbKE-2HtoEYmxg8VVpgMyMC7He89qvAAS6-ODnFPkJCy23n"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
