import { combineReducers } from 'redux';
import createSagaMiddleware from 'redux-saga';
import { configureStore } from '@reduxjs/toolkit';
import { rootSaga } from './sagas';
import baseReducer, { baseActions } from './base';
import authReducer, { authActions } from './auth';

const sagaMiddleware = createSagaMiddleware();
const rootReducer = combineReducers({
  base: baseReducer,
  auth: authReducer,
});
export const reducersActions = { ...baseActions, ...authActions };
export const store = configureStore({
  reducer: rootReducer,
  //@ts-ignore
  middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(sagaMiddleware),
});
sagaMiddleware.run(rootSaga);
export type RootStateType = ReturnType<typeof store.getState>;
export type AppDispatchType = typeof store.dispatch;
