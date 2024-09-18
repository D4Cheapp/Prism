import { all, put, takeEvery } from 'redux-saga/effects';
import { sagaHandling } from '@/src/utils/sagaHandling';
import { CurrentUserProfileType } from '@/src/types/messengerTypes';
import { messengerActions } from '.';

function* getCurrentUserProfileSaga() {
  yield sagaHandling<CurrentUserProfileType | null>({
    href: '',
    method: 'GET',
    server: 'messenger',
    isDataInAction: true,
    action: (profile) => put(messengerActions.setCurrentUserProfile(profile ?? null)),
  });
}

export default function* messengerSaga() {
  yield all([takeEvery(messengerActions.getCurrentUserProfile, getCurrentUserProfileSaga)]);
}
