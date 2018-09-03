package com.petnagy.app

import android.os.Bundle
import com.petnagy.app.redux.AppState
import com.petnagy.app.redux.DecreaseAction
import com.petnagy.app.redux.IncreaseAction
import com.petnagy.koredux.Store
import com.petnagy.koredux.StoreSubscriber
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), StoreSubscriber<AppState> {

    @Inject
    lateinit var store: Store<AppState>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        decrease.setOnClickListener {
            store.dispatch(DecreaseAction())
        }

        increase.setOnClickListener {
            store.dispatch(IncreaseAction())
        }
    }

    override fun onStart() {
        super.onStart()
        store.subscribe(this)
    }

    override fun onStop() {
        super.onStop()
        store.unsubscribe(this)
    }

    override fun newState(state: AppState) {
        counter_label.text = "Counter: ${state.counter}"
    }
}
