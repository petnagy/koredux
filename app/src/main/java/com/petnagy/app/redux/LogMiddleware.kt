package com.petnagy.app.redux

import com.petnagy.koredux.Action
import com.petnagy.koredux.DispatchFunction
import com.petnagy.koredux.Middleware
import com.petnagy.koredux.Store
import timber.log.Timber

class LogMiddleware: Middleware<AppState> {
    override fun invoke(store: Store<AppState>, action: Action, next: DispatchFunction) {
        var log = "Action: -> " + action::class.java.simpleName
        Timber.d(log)
        next.dispatch(action)
    }
}