package com.sctaylor.room.application.dagger.modules;

import android.content.Context;


import com.sctaylor.room.application.dagger.scopes.RoomApplicationScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by simon on 12/3/2017.
 */

@Module
public class ContextModule {

    private final Context context;

    public ContextModule(Context context){
        this.context = context;
    }

    @Provides
    @RoomApplicationScope
    public Context context(){
        return this.context;
    }
}
