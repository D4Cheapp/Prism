import { all } from 'redux-saga/effects';
import authSaga from './auth/sagas';
import profileSaga from './profile/sagas';
import relationSaga from './relation/sagas';
import groupSaga from './group/sagas';
import chatSaga from './chat/sagas';

export function* rootSaga() {
  yield all([authSaga(), profileSaga(), relationSaga(), groupSaga(), chatSaga()]);
}
