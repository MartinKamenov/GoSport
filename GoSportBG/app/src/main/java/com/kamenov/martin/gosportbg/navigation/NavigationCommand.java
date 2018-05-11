package com.kamenov.martin.gosportbg.navigation;

public interface NavigationCommand<T> {
    void navigate();

    void putExtraInteger(String name, int obj);
}
