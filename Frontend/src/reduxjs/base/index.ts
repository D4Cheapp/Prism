/* eslint @typescript-eslint/no-unused-vars: 0 */
import { createSlice } from '@reduxjs/toolkit';
import { SetMessageStateActionType, SetRequestStatusActionType } from './types';
import {
  DeleteMessageStateActionType,
  SetLoadingStateActionType,
  RequestStatusType,
  MessageType,
} from './types';

interface BaseSliceInterface {
  loadingState: boolean;
  messages: MessageType[];
  requestStatus: RequestStatusType;
}

const baseSlice = createSlice({
  name: 'baseSlice',
  initialState: {
    loadingState: false,
    requestStatus: {
      method: 'GET',
      request: '',
      isOk: true,
    },
    messages: [],
  } as BaseSliceInterface,
  reducers: {
    setLoadingState: (state, isLoading: SetLoadingStateActionType) => {
      state.loadingState = isLoading.payload;
      return state;
    },

    setRequestStatus: (state, status: SetRequestStatusActionType) => {
      state.requestStatus = status.payload;
      return state;
    },

    setMessagesState: (state, messages: SetMessageStateActionType) => {
      const isPayloadExist =
        messages.payload !== undefined && (!!messages.payload?.error || !!messages.payload?.info);
      if (isPayloadExist) {
        const error = !!messages.payload?.error ? messages.payload.error : undefined;
        const info = messages.payload?.info ?? undefined;
        state.messages = [
          ...state.messages,
          {
            id: Date.now(),
            error,
            info,
          },
        ];
      } else {
        state.messages = [];
      }
      return state;
    },

    deleteMessageState: (state, messageIndex: DeleteMessageStateActionType) => {
      state.messages = state.messages.filter(
        (message) => message && message.id !== messageIndex.payload,
      );
    },
  },
});

export const baseActions = baseSlice.actions;
export default baseSlice.reducer;
