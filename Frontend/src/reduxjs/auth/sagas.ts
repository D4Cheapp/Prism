import { all, put, takeEvery } from 'redux-saga/effects';
import { sagaHandling } from '@/src/utils/sagaHandling';
import { UserReceiveType } from '@/src/types/authRecieveTypes';
import { TextReceiveType } from '@/src/types/receiveTypes';
import { authActions, setCurrentUser } from '.';
import { ConfirmCodeActionType, LoginActionType, RegistrationActionType } from './types';

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

function* registrationSaga(action: RegistrationActionType) {
  yield sagaHandling<TextReceiveType>({
    method: 'POST',
    href: '/registration',
    server: 'auth',
    body: action.payload,
  });
}

function* confirmRegistrationSaga(action: ConfirmCodeActionType) {
  yield sagaHandling<UserReceiveType>({
    method: 'PATCH',
    href: '/registration',
    server: 'auth',
    body: action.payload,
  });
}

function* logoutSaga() {
  yield sagaHandling<TextReceiveType>({
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
    takeEvery(authActions.registration, registrationSaga),
    takeEvery(authActions.confirmRegistration, confirmRegistrationSaga),
    takeEvery(authActions.logout, logoutSaga),
  ]);
}
