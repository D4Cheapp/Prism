import { all, put, takeEvery } from 'redux-saga/effects';
import { sagaHandling } from '@/src/utils/sagaHandling';
import { FileListReceiveType, TextReceiveType } from '@/src/types/receiveTypes';
import { ChatReceiveType } from '@/src/types/chatReceiveTypes';
import { chatActions } from '.';
import {
  CreateChatActionType,
  DeleteChatActionType,
  GetChatListActionType,
  GetChatFilesActionType,
} from './types';

function* createChatSaga(action: CreateChatActionType) {
  yield sagaHandling<ChatReceiveType>({
    method: 'POST',
    href: '/chat',
    server: 'messenger',
    body: action.payload,
    isDataInAction: true,
    action: (data?: ChatReceiveType) => put(chatActions.setCreatedChat(data)),
  });
}

function* deleteChatSaga(action: DeleteChatActionType) {
  yield sagaHandling<TextReceiveType>({
    method: 'DELETE',
    href: '/chat',
    server: 'messenger',
    body: action.payload,
  });
}

function* getChatListSaga(action: GetChatListActionType) {
  yield sagaHandling<ChatReceiveType[]>({
    method: 'GET',
    href: '/chat/list',
    server: 'messenger',
    body: action.payload,
    isDataInAction: true,
    action: (data?: ChatReceiveType[]) => put(chatActions.setChatList(data)),
  });
}

function* getChatFilesSaga(action: GetChatFilesActionType) {
  yield sagaHandling<FileListReceiveType>({
    method: 'GET',
    href: '/chat/files',
    server: 'messenger',
    body: action.payload,
    isDataInAction: true,
    action: (data?: FileListReceiveType) => put(chatActions.setChatFiles(data)),
  });
}

export default function* chatSaga() {
  yield all([
    takeEvery(chatActions.createChat, createChatSaga),
    takeEvery(chatActions.deleteChat, deleteChatSaga),
    takeEvery(chatActions.getChatList, getChatListSaga),
    takeEvery(chatActions.getChatFiles, getChatFilesSaga),
  ]);
}
