package com.petnagy.java.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

import com.petnagy.java.example.redux.AppState;
import com.petnagy.java.example.redux.DecreaseAction;
import com.petnagy.java.example.redux.IncreaseAction;
import com.petnagy.koredux.Store;
import com.petnagy.koredux.StoreSubscriber;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity implements StoreSubscriber<AppState>{

    @Inject
    Store<AppState> store;

    private TextView counter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton increaseFab = findViewById(R.id.increase);
        increaseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store.dispatch(new IncreaseAction());
            }
        });

        FloatingActionButton decreaseFab = findViewById(R.id.decrease);
        decreaseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store.dispatch(new DecreaseAction());
            }
        });

        counter = findViewById(R.id.counter_label);
    }

    @Override
    protected void onStart() {
        super.onStart();
        store.subscribe(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        store.unsubscribe(this);
    }

    @Override
    public void newState(AppState appState) {
        counter.setText("Counter: " + Integer.toString(appState.getCounter()));
    }
}
