package com.petnagy.app.redux

import com.petnagy.koredux.Action
import com.petnagy.koredux.Reducer

class AppReducer: Reducer<AppState> {
    override fun invoke(action: Action, state: AppState): AppState {
        return state.copy(counter = counterReducer(action, state.counter))
    }

    private fun counterReducer(action: Action, actualCounter: Int): Int {
        return when(action) {
            is IncreaseAction -> actualCounter + 1
            is DecreaseAction -> actualCounter - 1
            else -> 0
        }
    }
}