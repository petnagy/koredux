# koredux

[ ![Download](https://api.bintray.com/packages/petnagy/koredux/koredux/images/download.svg) ](https://bintray.com/petnagy/koredux/koredux/_latestVersion)

Redux library for Android, based on Kotlin language

# Introduction

Redux is: "Redux is a predictable state container for JavaScript apps."
(you can find it: https://redux.js.org/)

Koredux library was implemented by (and ispirated by) dart implementation.
See it: (https://pub.dartlang.org/packages/redux)

# Installation

Add this line in your build.gradle file:
```
buildscript {
    repositories {
        ...
        jcenter()
        ...
    }    
}
```
and add this dependency:

```
compile 'com.petnagy.koredux:koredux:0.1.1
```
# Components

Redux has some components:
1. Store - it will hold the states and emit the state changes.
2. State - it could be an app state or view state.
3. Action - it is an input, an event, or message from ui or from inside of the store. Action could cause a state change.
4. Reducer - it must be a PURE function. It will change the state(s). Only reducer can change the state.
5. Middleware - in middleware can we run any asynchron calls (e.g.: network call) and a middleware could start a new action.

# Usage

### Store
We must create a Store

```
val store: Store<AppState> = Store(AppReducer(), listOfMiddlewares(), AppState())
```
When we call the constructor than we must define the reducer, list of middlewares and a initial app state.

### State
AppState could be simple or composite:

```
data class AppState(val stateComponentA: StateComponentA, val stateComponentB: StateComponentB, ....)
```
We must be careful a state must be immutable!!! (and it will be changed by only Reducer)

### Reducer

```
class AppReducer : Reducer<AppState> {
    override fun invoke(action: Action, state: AppState): AppState {
        return AppState(fragmentState = fragmentStateReducer(action, state.fragmentState),
                        mainPageState = mainPageStateReducer(action, state.mainPageState), 
                        ...
    }
    
    private fun fragmentStateReducer(action: Action, oldFragmentState: FragmentState): FragmentState {
        var state = oldFragmentState
        when (action) {
            is OneTypeOfAction -> state = state.copy(someState = true, otherState = action.value)
        }
        return state
    }
}
```

Do not block the reducer!!! It must be short, fast, and understandable.

### Middleware

```
class MyMiddleware : Middleware<AppState> {

    override fun invoke(store: Store<AppState>, action: Action, next: DispatchFunction) {
        when (action) {
            is AnyKindOfAction -> doSomethingLong(store)
            ....
        }
        next.dispatch(action)
    }
    
    private fun queryLatestRate(store: Store<AppState>) {
        // We can run something in the background thread or do something long operation...
        Single.just(true)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { success -> store.dispatch(SuccessAction()) },
                { error -> store.dispatch(ErrorAction()) }
    }
}
```

In a middleware we can generate a new Action. It is possible transform the original action to other action, but in this case be careful, order of the middleware is important. If we do not want to transform the original action than we need to pass action to the next middlewate (next.dispatch(action) call).
Be careful! Middleware is running on the UI thread by default, if you want to use any worker thread than you have to use RX, AsyncTask, Thread, etc...
Because middleware is running on the UI thread, be careful if you block the middleware with long running method than the action will be stopped and it will be not handled by other middleware or reducer!

### In the Activity

```
class MainActivity : StoreSubscriber<AppState> {

    override fun onStart() {
        super.onStart()
        store.subscribe(this)
    }

    override fun onStop() {
        super.onStop()
        store.unsubscribe(this)
    }

    override fun newState(state: AppState) {
        doSomethingWithNewState(state)
    }

}
```

If you want to get any event about the state change than you must implement StoreSubscriber<AppState> interface.
And you must call subscribe method. Reduce the wrong behaviour please unsubscribe after Actvitiy is not in the foreground.
We will get the changed state in the newState callback. Be careful, when you subscribe, you will get the actual state immediately.
  
### Tricks

If you do not want to get any information about state changes than it is possible to use store object to dispatch any action.
For example in a RecyclerView item we do not want to get any state changes but we can send action by user.

# Examples

You can find Kotlin example here: https://github.com/petnagy/koredux/tree/master/app/src/main
Java example: https://github.com/petnagy/koredux/tree/master/java_app/src/main

Complete project: https://github.com/petnagy/redux_playground
