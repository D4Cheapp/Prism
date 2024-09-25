import { all } from 'redux-saga/effects';
import authSaga from './auth/sagas';
import profileSaga from './profile/sagas';

export function* rootSaga() {
  yield all([authSaga(), profileSaga()]);
}
