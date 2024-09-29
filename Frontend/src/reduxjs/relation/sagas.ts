import { all, put, takeEvery } from 'redux-saga/effects';
import { sagaHandling } from '@/src/utils/sagaHandling';
import { ReceiveProfileListModel } from '@/src/types/relationReceiveTypes';
import { TextReceiveType } from '@/src/types/receiveTypes';
import { relationActions } from '.';
import { ProfileTagActionType } from '../profile/types';
import { RequestPaginationListActionType } from './types';

function* unblockUserSaga(action: ProfileTagActionType) {
  yield sagaHandling<TextReceiveType>({
    method: 'POST',
    href: '/relation/unblock',
    server: 'messenger',
    body: action.payload,
  });
}

function* addFriendSaga(action: ProfileTagActionType) {
  yield sagaHandling<TextReceiveType>({
    method: 'POST',
    href: '/relation/friend',
    server: 'messenger',
    body: action.payload,
  });
}

function* deleteFriendSaga(action: ProfileTagActionType) {
  yield sagaHandling<TextReceiveType>({
    method: 'DELETE',
    href: '/relation/friend',
    server: 'messenger',
    body: action.payload,
  });
}

function* declineFriendRequestSaga(action: ProfileTagActionType) {
  yield sagaHandling<TextReceiveType>({
    method: 'POST',
    href: '/relation/friend-decline',
    server: 'messenger',
    body: action.payload,
  });
}

function* blockUserSaga(action: ProfileTagActionType) {
  yield sagaHandling<TextReceiveType>({
    method: 'POST',
    href: '/relation/block',
    server: 'messenger',
    body: action.payload,
  });
}

function* getSentFriendRequestListSaga(action: RequestPaginationListActionType) {
  yield sagaHandling<ReceiveProfileListModel>({
    method: 'GET',
    href: '/relation/sent-friend-requests',
    server: 'messenger',
    body: action.payload,
    isDataInAction: true,
    action: (data?: ReceiveProfileListModel) => put(relationActions.setSentFriendRequestList(data)),
  });
}

function* getFriendRequestListSaga(action: RequestPaginationListActionType) {
  yield sagaHandling<ReceiveProfileListModel>({
    method: 'GET',
    href: '/relation/friend-requests',
    server: 'messenger',
    body: action.payload,
    isDataInAction: true,
    action: (data?: ReceiveProfileListModel) => put(relationActions.setFriendRequestList(data)),
  });
}

function* getFriendListSaga(action: RequestPaginationListActionType) {
  yield sagaHandling<ReceiveProfileListModel>({
    method: 'GET',
    href: '/relation/friend-list',
    server: 'messenger',
    body: action.payload,
    isDataInAction: true,
    action: (data?: ReceiveProfileListModel) => put(relationActions.setFriendList(data)),
  });
}

function* getBlockListSaga(action: RequestPaginationListActionType) {
  yield sagaHandling<ReceiveProfileListModel>({
    method: 'GET',
    href: '/relation/block-list',
    server: 'messenger',
    body: action.payload,
    isDataInAction: true,
    action: (data?: ReceiveProfileListModel) => put(relationActions.setBlockList(data)),
  });
}

export default function* relationSaga() {
  yield all([
    takeEvery(relationActions.unblockUser, unblockUserSaga),
    takeEvery(relationActions.addFriend, addFriendSaga),
    takeEvery(relationActions.deleteFriend, deleteFriendSaga),
    takeEvery(relationActions.declineFriendRequest, declineFriendRequestSaga),
    takeEvery(relationActions.blockUser, blockUserSaga),
    takeEvery(relationActions.getSentFriendRequestList, getSentFriendRequestListSaga),
    takeEvery(relationActions.getFriendRequestList, getFriendRequestListSaga),
    takeEvery(relationActions.getFriendList, getFriendListSaga),
    takeEvery(relationActions.getBlockList, getBlockListSaga),
  ]);
}
