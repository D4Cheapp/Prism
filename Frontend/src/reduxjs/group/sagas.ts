import { all, put, takeEvery } from 'redux-saga/effects';
import { sagaHandling } from '@/src/utils/sagaHandling';
import { FileListReceiveType, TextReceiveType } from '@/src/types/receiveTypes';
import { GroupReceiveType } from '@/src/types/groupReceiveTypes';
import { groupActions } from '.';
import {
  CreateGroupActionType,
  DeleteGroupActionType,
  EditGroupPhotoActionType,
  EditGroupNameActionType,
  EditGroupDescriptionActionType,
  GetGroupListActionType,
  GetGroupFilesActionType,
  GroupMemberActionType,
} from './types';

function* createGroupSaga(action: CreateGroupActionType) {
  yield sagaHandling<GroupReceiveType>({
    method: 'POST',
    href: '/group',
    server: 'messenger',
    body: action.payload,
    isDataInAction: true,
    action: (data?: GroupReceiveType) => put(groupActions.setCreatedGroup(data)),
  });
}

function* deleteGroupSaga(action: DeleteGroupActionType) {
  yield sagaHandling<TextReceiveType>({
    method: 'DELETE',
    href: '/group',
    server: 'messenger',
    body: action.payload,
  });
}

function* addMemberToGroupSaga(action: GroupMemberActionType) {
  yield sagaHandling<TextReceiveType>({
    method: 'POST',
    href: '/group/member',
    server: 'messenger',
    body: action.payload,
  });
}

function* deleteMemberFromGroupSaga(action: GroupMemberActionType) {
  yield sagaHandling<TextReceiveType>({
    method: 'DELETE',
    href: '/group/member',
    server: 'messenger',
    body: action.payload,
  });
}

function* addGroupAdminSaga(action: GroupMemberActionType) {
  yield sagaHandling<GroupReceiveType>({
    method: 'POST',
    href: '/group/admin',
    server: 'messenger',
    body: action.payload,
    isDataInAction: true,
    action: (data?: GroupReceiveType) => put(groupActions.setUpdatedGroup(data)),
  });
}

function* deleteGroupAdminSaga(action: GroupMemberActionType) {
  yield sagaHandling<TextReceiveType>({
    method: 'DELETE',
    href: '/group/admin',
    server: 'messenger',
    body: action.payload,
  });
}

function* editGroupPhotoSaga(action: EditGroupPhotoActionType) {
  yield sagaHandling<GroupReceiveType>({
    method: 'PATCH',
    href: '/group/photo',
    server: 'messenger',
    body: action.payload,
    isDataInAction: true,
    action: (data?: GroupReceiveType) => put(groupActions.setUpdatedGroup(data)),
  });
}

function* editGroupNameSaga(action: EditGroupNameActionType) {
  yield sagaHandling<GroupReceiveType>({
    method: 'PATCH',
    href: '/group/name',
    server: 'messenger',
    body: action.payload,
    isDataInAction: true,
    action: (data?: GroupReceiveType) => put(groupActions.setUpdatedGroup(data)),
  });
}

function* editGroupDescriptionSaga(action: EditGroupDescriptionActionType) {
  yield sagaHandling<GroupReceiveType>({
    method: 'PATCH',
    href: '/group/description',
    server: 'messenger',
    body: action.payload,
    isDataInAction: true,
    action: (data?: GroupReceiveType) => put(groupActions.setUpdatedGroup(data)),
  });
}

function* getGroupListSaga(action: GetGroupListActionType) {
  yield sagaHandling<GroupReceiveType[]>({
    method: 'GET',
    href: '/group/list',
    server: 'messenger',
    body: action.payload,
    isDataInAction: true,
    action: (data?: GroupReceiveType[]) => put(groupActions.setGroupList(data)),
  });
}

function* getGroupFilesSaga(action: GetGroupFilesActionType) {
  yield sagaHandling<FileListReceiveType>({
    method: 'GET',
    href: '/group/files',
    server: 'messenger',
    body: action.payload,
    isDataInAction: true,
    action: (data?: FileListReceiveType) => put(groupActions.setGroupFiles(data)),
  });
}

export default function* groupSaga() {
  yield all([
    takeEvery(groupActions.createGroup, createGroupSaga),
    takeEvery(groupActions.deleteGroup, deleteGroupSaga),
    takeEvery(groupActions.addMemberToGroup, addMemberToGroupSaga),
    takeEvery(groupActions.deleteMemberFromGroup, deleteMemberFromGroupSaga),
    takeEvery(groupActions.addGroupAdmin, addGroupAdminSaga),
    takeEvery(groupActions.deleteGroupAdmin, deleteGroupAdminSaga),
    takeEvery(groupActions.editGroupPhoto, editGroupPhotoSaga),
    takeEvery(groupActions.editGroupName, editGroupNameSaga),
    takeEvery(groupActions.editGroupDescription, editGroupDescriptionSaga),
    takeEvery(groupActions.getGroupList, getGroupListSaga),
    takeEvery(groupActions.getGroupFiles, getGroupFilesSaga),
  ]);
}
