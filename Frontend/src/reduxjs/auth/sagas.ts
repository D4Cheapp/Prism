import { all, put, takeEvery } from 'redux-saga/effects';
import { sagaHandling } from '@/src/utils/sagaHandling';
import { UserReceiveType } from '@/src/types/authReceiveTypes';
import { TextReceiveType } from '@/src/types/receiveTypes';
import { authActions } from '.';
import { baseActions } from '../base';
import {
  ChangePasswordActionType,
  ConfirmCodeActionType,
  DeleteAccountActionType,
  LoginActionType,
  RegistrationActionType,
  RestorePasswordActionType,
} from './types';

function* getCurrentUserSaga() {
  yield sagaHandling<UserReceiveType>({
    method: 'GET',
    href: '/user',
    server: 'auth',
    isDataInAction: true,
    action: (data?: UserReceiveType) => put(authActions.setCurrentUser(data ?? null)),
  });
}

function* loginSaga(action: LoginActionType) {
  yield sagaHandling<UserReceiveType>({
    method: 'POST',
    href: '/login',
    server: 'auth',
    body: action.payload,
    isDataInAction: true,
    action: (data?: UserReceiveType) => put(authActions.setCurrentUser(data ?? null)),
  });
}

function* logoutSaga() {
  yield sagaHandling<TextReceiveType>({
    method: 'DELETE',
    href: '/logout',
    server: 'auth',
    action: () => put(authActions.setCurrentUser(null)),
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

function* restorePasswordSaga(action: RestorePasswordActionType) {
  yield sagaHandling<TextReceiveType>({
    method: 'POST',
    href: '/restore-password',
    server: 'auth',
    body: action.payload,
  });
}

function* confirmRestorePasswordSaga(action: ConfirmCodeActionType) {
  yield sagaHandling<TextReceiveType>({
    method: 'PATCH',
    href: '/restore-password',
    server: 'auth',
    body: action.payload,
  });
}

function* changePasswordSaga(action: ChangePasswordActionType) {
  yield sagaHandling<UserReceiveType>({
    method: 'PATCH',
    href: '/password',
    server: 'auth',
    body: action.payload,
    action: () => put(baseActions.setMessagesState({ info: 'Password changed' })),
  });
}

function* deleteAccountSaga(action: DeleteAccountActionType) {
  yield sagaHandling<TextReceiveType>({
    method: 'DELETE',
    href: '/user',
    server: 'auth',
    body: action.payload,
  });
}

export default function* authSaga() {
  yield all([
    takeEvery(authActions.getCurrentUser, getCurrentUserSaga),
    takeEvery(authActions.login, loginSaga),
    takeEvery(authActions.logout, logoutSaga),
    takeEvery(authActions.registration, registrationSaga),
    takeEvery(authActions.confirmRegistration, confirmRegistrationSaga),
    takeEvery(authActions.restorePassword, restorePasswordSaga),
    takeEvery(authActions.confirmRestorePassword, confirmRestorePasswordSaga),
    takeEvery(authActions.changePassword, changePasswordSaga),
    takeEvery(authActions.deleteAccount, deleteAccountSaga),
  ]);
}
