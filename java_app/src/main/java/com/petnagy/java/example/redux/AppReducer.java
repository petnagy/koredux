package com.petnagy.java.example.redux;

import com.petnagy.koredux.Action;
import com.petnagy.koredux.Reducer;

import org.jetbrains.annotations.NotNull;

public class AppReducer implements Reducer<AppState> {
    @Override
    public AppState invoke(@NotNull Action action, AppState appState) {
        AppState state = appState;
        if (action instanceof IncreaseAction) {
            state = new AppState(state.getCounter() + 1);
        } else if (action instanceof DecreaseAction) {
            state = new AppState(state.getCounter() - 1);
        }
        return state;
    }
}
