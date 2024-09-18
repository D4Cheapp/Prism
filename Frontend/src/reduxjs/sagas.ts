import { all } from 'redux-saga/effects';
import authSaga from './auth/sagas';
import messengerSaga from './messenger/sagas';

export function* rootSaga() {
  yield all([authSaga(), messengerSaga()]);
}
