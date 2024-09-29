import { combineReducers } from 'redux';
import createSagaMiddleware from 'redux-saga';
import { configureStore } from '@reduxjs/toolkit';
import { rootSaga } from './sagas';
import baseReducer, { baseActions } from './base';
import authReducer, { authActions } from './auth';
import profileReducer, { profileActions } from './profile';
import relationReducer, { relationActions } from './relation';
import groupReducer, { groupActions } from './group';
import chatReducer, { chatActions } from './chat';

const sagaMiddleware = createSagaMiddleware();
const rootReducer = combineReducers({
  base: baseReducer,
  auth: authReducer,
  profile: profileReducer,
  relation: relationReducer,
  group: groupReducer,
  chat: chatReducer,
});
export const reducersActions = {
  ...baseActions,
  ...authActions,
  ...profileActions,
  ...relationActions,
  ...groupActions,
  ...chatActions,
};
export const store = configureStore({
  reducer: rootReducer,
  //@ts-ignore
  middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(sagaMiddleware),
});
sagaMiddleware.run(rootSaga);
export type RootStateType = ReturnType<typeof store.getState>;
export type AppDispatchType = typeof store.dispatch;
