package com.sctaylor.room.application.dagger.components;

import com.sctaylor.room.application.dagger.modules.PicassoModule;
import com.sctaylor.room.application.dagger.modules.RoomServiceModule;
import com.sctaylor.room.application.dagger.scopes.RoomApplicationScope;
import com.sctaylor.room.application.network.RoomService;
import com.squareup.picasso.Picasso;

import dagger.Component;

/**
 * Created by simon on 12/3/2017.
 */

@RoomApplicationScope
@Component(modules = {RoomServiceModule.class, PicassoModule.class})
public interface RoomApplicationComponent {

    Picasso getPicasso();

    RoomService getRoomService();
}
