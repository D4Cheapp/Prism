import { all, put, takeEvery } from 'redux-saga/effects';
import { sagaHandling } from '@/src/utils/sagaHandling';
import { CurrentUserProfileType } from '@/src/types/profileReceiveTypes';
import { TextReceiveType } from '@/src/types/receiveTypes';
import { profileActions } from '.';
import {
  ProfileTagActionType,
  SetProfileDataActionType,
  SetProfilePictureActionType,
  SetProfileTagActionType,
} from './types';

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

function* setProfilePictureSaga(action: SetProfilePictureActionType) {
  const formData = new FormData();
  const arr = action.payload.split(',');
  //@ts-ignore
  const mime = arr[0].match(/:(.*?);/)[1];
  const bstr = atob(arr[arr.length - 1]);
  let n = bstr.length;
  const u8arr = new Uint8Array(n);
  while (n--) {
    u8arr[n] = bstr.charCodeAt(n);
  }
  const file = new File([u8arr], 'profilePicture.jpg', { type: mime });
  formData.append('file', file);
  yield sagaHandling<TextReceiveType>({
    href: '/profile/picture',
    method: 'PATCH',
    server: 'messenger',
    multipart: true,
    body: formData,
  });
}

function* deleteProfilePictureSaga() {
  yield sagaHandling<TextReceiveType>({
    href: '/profile/picture',
    method: 'DELETE',
    server: 'messenger',
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
    takeEvery(profileActions.setProfilePicture, setProfilePictureSaga),
    takeEvery(profileActions.deleteProfilePicture, deleteProfilePictureSaga),
  ]);
}
