import { PayloadAction } from '@reduxjs/toolkit';
import { FileListReceiveType, PaginationListReceiveType } from '@/src/types/receiveTypes';
import { ChatReceiveType } from '@/src/types/chatReceiveTypes';

export type DialogIdPayload = {
  dialogId: string;
};

export type CreateChatActionType = PayloadAction<{
  tag: string;
}>;
export type DeleteChatActionType = PayloadAction<DialogIdPayload>;
export type GetChatListActionType = PayloadAction<PaginationListReceiveType>;
export type GetChatFilesActionType = PayloadAction<DialogIdPayload & PaginationListReceiveType>;

export type SetCreatedChatActionType = PayloadAction<ChatReceiveType | undefined>;
export type SetChatListActionType = PayloadAction<ChatReceiveType[] | undefined>;
export type SetChatFilesActionType = PayloadAction<FileListReceiveType | undefined>;
