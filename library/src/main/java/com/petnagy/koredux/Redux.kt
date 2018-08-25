package com.petnagy.koredux

import java.util.concurrent.CopyOnWriteArrayList

interface Action

interface Reducer<State> {
    fun invoke(action: Action, state: State): State
}

interface Middleware<State> {
    fun invoke(store: Store<State>, action: Action, next: DispatchFunction)
}

interface DispatchFunction {
    fun dispatch(action: Action)
}

interface StoreSubscriber<State> {
    fun newState(state: State)
}

class Store<State>(reducer: Reducer<State>, middlewareList: List<Middleware<State>>, initState: State) {

    private val dispatchFunctions: MutableList<DispatchFunction> = arrayListOf()
    var state: State = initState
        private set(value) {
            field = value
            subscriptions.forEach { subscriber -> subscriber.newState(field) }
        }

    private var subscriptions: MutableList<StoreSubscriber<State>> = CopyOnWriteArrayList()

    init {
        dispatchFunctions.add(object: DispatchFunction {
            @Synchronized
            override fun dispatch(action: Action) {
                val newState = reducer.invoke(action, state)
                if (state != newState) {
                    state = newState
                }
            }
        })
        middlewareList.reversed().map { middleware ->
            val next = dispatchFunctions.first()
            dispatchFunctions.add(0, object: DispatchFunction {
                override fun dispatch(action: Action) {
                    middleware.invoke(this@Store, action, next)
                }
            })
        }
    }

    fun dispatch(action: Action) {
        dispatchFunctions.first().dispatch(action)
    }

    fun subscribe(subscriber: StoreSubscriber<State>) {
        subscriptions.add(subscriber)
        subscriber.newState(state)
    }

    fun unsubscribe(subscriber: StoreSubscriber<State>) {
        subscriptions.remove(subscriber)
    }
}