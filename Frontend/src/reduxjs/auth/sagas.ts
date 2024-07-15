import { all, put, takeEvery } from 'redux-saga/effects';
import { sagaHandling } from '@/src/utils/sagaHandling';
import { UserReceiveType } from '@/src/types/authRecieveTypes';
import { authActions, setCurrentUser } from '.';
import { LoginActionType } from './types';

function* getCurrentUserSaga() {
  yield sagaHandling<UserReceiveType>({
    method: 'GET',
    href: '/user',
    server: 'auth',
    isDataInAction: true,
    action: (data?: UserReceiveType) => put(setCurrentUser(data ?? null)),
  });
}

function* loginSaga(action: LoginActionType) {
  yield sagaHandling<UserReceiveType>({
    method: 'POST',
    href: '/login',
    server: 'auth',
    body: action.payload,
    isDataInAction: true,
    action: (data?: UserReceiveType) => put(setCurrentUser(data ?? null)),
  });
}

function* logoutSaga() {
  yield sagaHandling({
    method: 'DELETE',
    href: '/logout',
    server: 'auth',
    action: () => put(setCurrentUser(null)),
  });
}

export default function* authSaga() {
  yield all([
    takeEvery(authActions.getCurrentUser, getCurrentUserSaga),
    takeEvery(authActions.login, loginSaga),
    takeEvery(authActions.logout, logoutSaga),
  ]);
}
