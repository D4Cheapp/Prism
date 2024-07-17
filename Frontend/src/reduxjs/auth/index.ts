/* eslint @typescript-eslint/no-unused-vars: 0 */
import { createSlice } from '@reduxjs/toolkit';
import { UserReceiveType } from '@/src/types/authRecieveTypes';
import {
  ConfirmCodeActionType,
  LoginActionType,
  RegistrationActionType,
  SetCurrentUserActionType,
} from './types';

interface AuthSliceInterface {
  currentUser?: UserReceiveType;
}

const authSlice = createSlice({
  name: 'authSlice',
  initialState: { currentUser: undefined } as AuthSliceInterface,
  reducers: {
    getCurrentUser: (state) => state,

    setCurrentUser: (state, currentUser: SetCurrentUserActionType) => {
      const isResponseCorrect = currentUser.payload !== null;
      if (isResponseCorrect) {
        //@ts-ignore
        state.currentUser = currentUser.payload;
      }
      return state;
    },

    login: (state, user: LoginActionType) => state,

    registration: (state, user: RegistrationActionType) => state,

    confirmRegistration: (state, confirmCode: ConfirmCodeActionType) => state,

    logout: (state) => state,
  },
});

export const authActions = authSlice.actions;
export const { getCurrentUser, setCurrentUser, login, registration, confirmRegistration, logout } =
  authSlice.actions;
export default authSlice.reducer;
