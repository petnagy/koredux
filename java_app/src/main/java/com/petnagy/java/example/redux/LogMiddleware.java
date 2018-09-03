package com.petnagy.java.example.redux;

import com.petnagy.koredux.Action;
import com.petnagy.koredux.DispatchFunction;
import com.petnagy.koredux.Middleware;
import com.petnagy.koredux.Store;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class LogMiddleware implements Middleware<AppState> {
    @Override
    public void invoke(@NotNull Store<AppState> store, @NotNull Action action, @NotNull DispatchFunction next) {
        Timber.d("Action: -> %s", action.getClass().getSimpleName());
        next.dispatch(action);
    }
}
