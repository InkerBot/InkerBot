package com.eloli.inkerbot.api;

import com.google.inject.Injector;

import javax.annotation.Nonnull;

public final class InkerBot {
    private InkerBot() {
        throw new IllegalCallerException("Static class shouldn't be instance.");
    }

    private static Injector injector;

    @Nonnull
    public static Injector getInjector(){
        return injector;
    }
}
