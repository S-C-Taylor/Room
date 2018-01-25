package com.sctaylor.room.application.dagger.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.sctaylor.room.application.dagger.scopes.RoomApplicationScope;
import com.sctaylor.room.application.network.RoomService;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by simon on 12/3/2017.
 */

@Module(includes = {NetworkModule.class})
public class RoomServiceModule {

    @Provides
    @RoomApplicationScope
    public RoomService roomService(Retrofit retrofit){
        return retrofit.create(RoomService.class);
    }

    @Provides
    @RoomApplicationScope
    public Gson gson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        //gsonBuilder.registerTypeAdapter(DateTime.class, new DateTimeConverter());
        return gsonBuilder.create();
    }

    @Provides
    @RoomApplicationScope
    public Retrofit retrofit(OkHttpClient okHttpClient, Gson gson){
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .baseUrl("http://124.190.102.243/")
                //.baseUrl("http://192.168.0.16/")
                //.baseUrl("https://10ebecc7-fe52-4466-ab68-0cb2fce626ce.mock.pstmn.io")
                .build();
    }



}
