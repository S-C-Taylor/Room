package com.sctaylor.room.application.network;

import com.sctaylor.room.model.DHT;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by simon on 12/3/2017.
 */

public interface RoomService {

    @Headers("x-api-key: d5465a197439413e8ea8fee7ffcbf6a4")
    @GET("led")
    Completable setRGBLed(@Query("r") int red,
                          @Query("g") int green,
                          @Query("b") int blue);

    @Headers("x-api-key: d5465a197439413e8ea8fee7ffcbf6a4")
    @GET("message")
    Completable setMessage(@Query("msg") String message);

    @Headers("x-api-key: d5465a197439413e8ea8fee7ffcbf6a4")
    @GET("dht")
    Single<DHT> getDHT();

    @Headers("x-api-key: d5465a197439413e8ea8fee7ffcbf6a4")
    @GET("reset")
    Completable doReset();

}
