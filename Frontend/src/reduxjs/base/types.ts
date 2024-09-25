import { PayloadAction } from '@reduxjs/toolkit';

export type SetLoadingStateActionType = PayloadAction<boolean>;
export type SetMessageStateActionType = PayloadAction<
  { error?: string; info?: string } | undefined
>;
export type DeleteMessageStateActionType = PayloadAction<number>;
export type SetRequestStatusActionType = PayloadAction<RequestStatusType>;
export type MessageType = {
  id: number;
  error?: string;
  info?: string;
};
export type RequestStatusType = {
  method: 'POST' | 'GET' | 'PATCH' | 'DELETE';
  request: string;
  isOk: boolean;
};
