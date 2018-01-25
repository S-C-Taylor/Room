package com.sctaylor.room.dagger.modules;

import com.sctaylor.room.features.home.HomeActivity;

import dagger.Module;

/**
 * Created by simon on 12/3/2017.
 */

@Module
public class HomeActivityModule {
    private final HomeActivity homeActivity;

    public HomeActivityModule(HomeActivity homeActivity){
        this.homeActivity = homeActivity;
    }

}
