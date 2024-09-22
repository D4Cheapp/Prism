import { all, put, takeEvery } from 'redux-saga/effects';
import { sagaHandling } from '@/src/utils/sagaHandling';
import { CurrentUserProfileType } from '@/src/types/profileReceiveTypes';
import { TextReceiveType } from '@/src/types/receiveTypes';
import { profileActions } from '.';
import { ProfileTagActionType, SetProfileDataActionType, SetProfileTagActionType } from './types';

function* unblockUserSaga(action: ProfileTagActionType) {
  yield sagaHandling<TextReceiveType>({
    href: '/profile/unblock',
    method: 'POST',
    server: 'messenger',
    body: action.payload,
  });
}

function* getCurrentUserProfileSaga() {
  yield sagaHandling<CurrentUserProfileType>({
    href: '/profile',
    method: 'GET',
    server: 'messenger',
    isDataInAction: true,
    action: (profile) => put(profileActions.setCurrentUserProfile(profile ?? null)),
  });
}

function* setProfileBioSaga(action: SetProfileDataActionType) {
  yield sagaHandling<TextReceiveType>({
    href: '/profile/status',
    method: 'PATCH',
    server: 'messenger',
    body: action.payload,
  });
}

function* setProfileNickNameSaga(action: SetProfileDataActionType) {
  yield sagaHandling<TextReceiveType>({
    href: '/profile/name',
    method: 'PATCH',
    server: 'messenger',
    body: action.payload,
  });
}

function* setProfileTagSaga(action: SetProfileTagActionType) {
  yield sagaHandling<TextReceiveType>({
    href: '/profile/tag',
    method: 'PATCH',
    server: 'messenger',
    body: action.payload,
  });
}

function* setProfilePhoneNumberSaga(action: SetProfileDataActionType) {
  yield sagaHandling<TextReceiveType>({
    href: '/profile/phone',
    method: 'PATCH',
    server: 'messenger',
    body: action.payload,
  });
}

export default function* profileSaga() {
  yield all([
    takeEvery(profileActions.unblockUser, unblockUserSaga),
    takeEvery(profileActions.getCurrentUserProfile, getCurrentUserProfileSaga),
    takeEvery(profileActions.setProfileBio, setProfileBioSaga),
    takeEvery(profileActions.setProfileNickName, setProfileNickNameSaga),
    takeEvery(profileActions.setProfileTag, setProfileTagSaga),
    takeEvery(profileActions.setProfilePhoneNumber, setProfilePhoneNumberSaga),
  ]);
}
