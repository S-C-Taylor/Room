package com.sctaylor.room.application;

import android.app.Activity;
import android.app.Application;

import com.sctaylor.room.application.dagger.components.DaggerRoomApplicationComponent;
import com.sctaylor.room.application.dagger.components.RoomApplicationComponent;
import com.sctaylor.room.application.dagger.modules.ContextModule;
import com.sctaylor.room.application.network.RoomService;
import com.squareup.picasso.Picasso;

import timber.log.Timber;

/**
 * Created by simon on 12/3/2017.
 */

public class RoomApplication extends Application {

    private RoomService roomService;
    private Picasso picasso;
    private RoomApplicationComponent component;

    public static RoomApplication get(Activity activity) {
        return (RoomApplication) activity.getApplication();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

        component = DaggerRoomApplicationComponent.builder()
                .contextModule(new ContextModule(this))
                .build();

        roomService = component.getRoomService();
        picasso = component.getPicasso();
    }

    public RoomApplicationComponent getComponent(){
        return this.component;
    }
}
