package com.sctaylor.room.dagger.components;

import com.sctaylor.room.features.home.HomeActivity;
import com.sctaylor.room.application.dagger.components.RoomApplicationComponent;
import com.sctaylor.room.dagger.modules.HomeActivityModule;
import com.sctaylor.room.dagger.scopes.HomeActivityScope;

import dagger.Component;

/**
 * Created by simon on 12/3/2017.
 */

@HomeActivityScope
@Component(modules = {HomeActivityModule.class}, dependencies = {RoomApplicationComponent.class})
public interface HomeActivityComponent {

    void injectHomeActivity(HomeActivity homeActivity);

}
