package com.petnagy.koredux;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class StoreTest {

    private class TestState {
    }

    private class TestAction implements Action {
    }

    private class TestAction2 implements Action {
    }

    @Mock
    private Reducer<TestState> mockReducer;

    @Mock
    private Middleware<TestState> mockMiddleware;

    private Store<TestState> underTest;

    private TestState initState;

    @Before
    public void setUp() {
        initState = new TestState();
    }

    @Test
    public void testReducerInvokedAfterMiddlewareCallNext() {
        //GIVEN
        TestAction action = new TestAction();
        Middleware<TestState> middleware = new Middleware<TestState>() {
            @Override
            public void invoke(@NotNull Store<TestState> store, @NotNull Action action, @NotNull DispatchFunction next) {
                next.dispatch(action);
            }
        };

        underTest = new Store<>(mockReducer, Collections.singletonList(middleware), initState);

        //WHEN
        underTest.dispatch(action);

        //THEN
        Mockito.verify(mockReducer).invoke(action, initState);
    }

    @Test
    public void testReducerInvokedAfterMiddlewareInvoked() {
        //GIVEN
        TestAction action = new TestAction();
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                Object[] arguments = invocation.getArguments();
                Action action = (TestAction) arguments[1];
                DispatchFunction next = (DispatchFunction) arguments[2];
                next.dispatch(action);
                return null;
            }
        }).when(mockMiddleware).invoke(Mockito.any(Store.class), Mockito.any(TestAction.class), Mockito.any(DispatchFunction.class));

        underTest = new Store<>(mockReducer, Collections.singletonList(mockMiddleware), initState);

        //WHEN
        underTest.dispatch(action);

        //THEN
        Mockito.verify(mockMiddleware).invoke(Mockito.any(Store.class), Mockito.any(TestAction.class), Mockito.any(DispatchFunction.class));
        Mockito.verify(mockReducer).invoke(action, initState);
    }

    @Test
    public void testReducerNotInvokedIfMiddlewareDoNotCallNext() {
        //GIVEN
        TestAction action = new TestAction();
        Middleware<TestState> middleware = new Middleware<TestState>() {
            @Override
            public void invoke(@NotNull Store<TestState> store, @NotNull Action action, @NotNull DispatchFunction next) {
            }
        };

        underTest = new Store<>(mockReducer, Collections.singletonList(middleware), initState);

        //WHEN
        underTest.dispatch(action);

        //THEN
        Mockito.verifyZeroInteractions(mockReducer);
    }

    @Test
    public void testMiddlewareInvoked() {
        //GIVEN
        TestAction action = new TestAction();
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return null;
            }
        }).when(mockMiddleware).invoke(Mockito.any(Store.class), Mockito.any(TestAction.class), Mockito.any(DispatchFunction.class));

        underTest = new Store<>(mockReducer, Collections.singletonList(mockMiddleware), initState);

        //WHEN
        underTest.dispatch(action);

        //THEN
        Mockito.verify(mockMiddleware).invoke(Mockito.any(Store.class), Mockito.any(TestAction.class), Mockito.any(DispatchFunction.class));
        Mockito.verifyZeroInteractions(mockReducer);
    }

    @Test
    public void testReducerInvokedAfterMiddlewareCallStoreWithOtherAction() {
        //GIVEN
        final TestAction action = new TestAction();
        final TestAction2 action2 = new TestAction2();

        Middleware<TestState> middleware = new Middleware<TestState>() {
            @Override
            public void invoke(@NotNull Store<TestState> store, @NotNull Action action, @NotNull DispatchFunction next) {
                if (action instanceof TestAction) {
                    store.dispatch(action2);
                } else {
                    next.dispatch(action);
                }
            }
        };

        underTest = new Store<>(mockReducer, Collections.singletonList(middleware), initState);

        //WHEN
        underTest.dispatch(action);

        //THEN

        Mockito.verify(mockReducer, Mockito.times(0)).invoke(action, initState);
        Mockito.verify(mockReducer).invoke(action2, initState);
    }

    @Test
    public void testReducerInvokedAfterMiddlewareInvokedWithStoreDispatch() {
        //GIVEN
        final TestAction action = new TestAction();
        final TestAction2 action2 = new TestAction2();

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                Object[] arguments = invocation.getArguments();
                Store store = (Store) arguments[0];
                Action action = (Action) arguments[1];
                if (action instanceof TestAction) {
                    store.dispatch(action2);
                }

                return null;
            }
        }).when(mockMiddleware).invoke(Mockito.any(Store.class), Mockito.any(TestAction.class), Mockito.any(DispatchFunction.class));

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                Object[] arguments = invocation.getArguments();
                Action action = (Action) arguments[1];
                DispatchFunction next = (DispatchFunction) arguments[2];
                if (action instanceof TestAction2) {
                    next.dispatch(action);
                }

                return null;
            }
        }).when(mockMiddleware).invoke(Mockito.any(Store.class), Mockito.any(TestAction2.class), Mockito.any(DispatchFunction.class));


        underTest = new Store<>(mockReducer, Collections.singletonList(mockMiddleware), initState);

        //WHEN
        underTest.dispatch(action);

        //THEN
        Mockito.verify(mockMiddleware).invoke(Mockito.any(Store.class), Mockito.any(TestAction.class), Mockito.any(DispatchFunction.class));
        Mockito.verify(mockMiddleware).invoke(Mockito.any(Store.class), Mockito.any(TestAction2.class), Mockito.any(DispatchFunction.class));
        Mockito.verify(mockReducer).invoke(action2, initState);
    }

    @Test
    public void testReducerInvokedIfNoMiddleware() {
        //GIVEN
        TestAction action = new TestAction();
        underTest = new Store<>(mockReducer, Collections.<Middleware<TestState>>emptyList(), initState);

        //WHEN
        underTest.dispatch(action);

        //THEN
        Mockito.verify(mockReducer).invoke(action, initState);
    }
}
