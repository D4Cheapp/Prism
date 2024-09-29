/* eslint @typescript-eslint/no-unused-vars: 0 */
import { createSlice } from '@reduxjs/toolkit';
import { ChatReceiveType, FileListReceiveType } from '@/src/types/chatReceiveTypes';
import {
  CreateChatActionType,
  DeleteChatActionType,
  GetChatListActionType,
  GetChatFilesActionType,
  SetCreatedChatActionType,
  SetChatListActionType,
  SetChatFilesActionType,
} from './types';

interface SliceInterface {
  chats: ChatReceiveType[];
  currentChat: ChatReceiveType | null | undefined;
  chatFiles: FileListReceiveType | null | undefined;
}

const chatSlice = createSlice({
  name: 'chatSlice',
  initialState: { chats: [], currentChat: undefined, chatFiles: undefined } as SliceInterface,
  reducers: {
    createChat: (state, action: CreateChatActionType) => state,

    deleteChat: (state, action: DeleteChatActionType) => state,

    getChatList: (state, action: GetChatListActionType) => state,

    getChatFiles: (state, action: GetChatFilesActionType) => state,

    setCreatedChat: (state, action: SetCreatedChatActionType) => {
      if (action.payload) {
        state.currentChat = action.payload;
        state.chats.push(action.payload);
      }
    },

    setChatList: (state, action: SetChatListActionType) => {
      state.chats = action.payload ?? [];
    },

    setChatFiles: (state, action: SetChatFilesActionType) => {},
  },
});

export const chatActions = chatSlice.actions;
export default chatSlice.reducer;
