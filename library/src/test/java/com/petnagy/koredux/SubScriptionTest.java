package com.petnagy.koredux;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class SubScriptionTest {

    private class TestState {
    }

    private class TestSubscriber implements StoreSubscriber<TestState> {

        @Override
        public void newState(TestState testState) {
        }
    }

    private class TestAction implements Action {
    }

    private Store<TestState> store;

    @Mock
    private Reducer<TestState> mockReducer;

    @Mock
    private Middleware<TestState> mockMiddleware;

    @Mock
    private TestSubscriber underTest;

    private TestState INIT_STATE = new TestState();

    private TestAction testAction;

    @Before
    public void setUp() {
        testAction = new TestAction();
    }

    @Test
    public void testSubscribeGetLatestStateAfterSubscription() {
        //GIVEN
        store = new Store<>(mockReducer, Collections.singletonList(mockMiddleware), INIT_STATE);

        //WHEN
        store.subscribe(underTest);

        //THEN
        Mockito.verify(underTest).newState(INIT_STATE);
    }

    @Test
    public void testWithoutSubscribeNoNewState() {
        //GIVEN
        store = new Store<>(mockReducer, Collections.singletonList(mockMiddleware), INIT_STATE);

        //WHEN
        store.dispatch(testAction);

        //THEN
        Mockito.verifyNoMoreInteractions(underTest);
    }

    @Test
    public void testAfterUnsubscribeNoInteractionWithNewState() {
        //GIVEN
        store = new Store<>(mockReducer, Collections.singletonList(mockMiddleware), INIT_STATE);
        store.subscribe(underTest);
        Mockito.verify(underTest, Mockito.times(1)).newState((TestState) Mockito.any());
        store.unsubscribe(underTest);

        //WHEN
        store.dispatch(testAction);

        //THEN
        Mockito.verifyNoMoreInteractions(underTest);
    }
}
