/* eslint @typescript-eslint/no-unused-vars: 0 */
import { createSlice } from '@reduxjs/toolkit';
import {
  DeleteErrorStateActionType,
  SetErrorStateActionType,
  SetLoadingStateActionType,
} from './types';

interface BaseSliceInterface {
  loadingState: boolean;
  errors: { error: string; id: number }[];
}

const baseSlice = createSlice({
  name: 'baseSlice',
  initialState: {
    loadingState: false,
    errors: [],
  } as BaseSliceInterface,
  reducers: {
    setLoadingState: (state, isLoading: SetLoadingStateActionType) => {
      state.loadingState = isLoading.payload;
    },

    setErrorsState: (state, errors: SetErrorStateActionType) => {
      const isPayloadEmpty = errors.payload !== undefined;
      if (isPayloadEmpty) {
        state.errors = [...state.errors, { error: 'Error: ' + errors.payload, id: Date.now() }];
      } else {
        state.errors = [];
      }
    },

    deleteErrorState: (state, errorIndex: DeleteErrorStateActionType) => {
      state.errors = state.errors.filter((error) => error.id !== errorIndex.payload);
    },
  },
});

export const baseActions = baseSlice.actions;
export const { setErrorsState, deleteErrorState, setLoadingState } = baseSlice.actions;
export default baseSlice.reducer;
