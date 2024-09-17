/* eslint @typescript-eslint/no-unused-vars: 0 */
import { createSlice } from '@reduxjs/toolkit';
import { UserReceiveType } from '@/src/types/authReceiveTypes';
import {
  ChangeEmailActionType,
  ChangePasswordActionType,
  ConfirmCodeActionType,
  ConfirmRestorePasswordActionType,
  DeleteAccountActionType,
  LoginActionType,
  RegistrationActionType,
  RestorePasswordActionType,
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
      state.currentUser = currentUser.payload;
      return state;
    },

    login: (state, user: LoginActionType) => state,

    logout: (state) => state,

    registration: (state, user: RegistrationActionType) => state,

    confirmRegistration: (state, confirmCode: ConfirmCodeActionType) => state,

    restorePassword: (state, email: RestorePasswordActionType) => state,

    confirmRestorePassword: (state, restorePassword: ConfirmRestorePasswordActionType) => state,

    changePassword: (state, passwords: ChangePasswordActionType) => state,

    changeEmail: (state, confirmCode: ConfirmCodeActionType) => state,

    sendConfirmToChangeEmail: (state, email: ChangeEmailActionType) => state,

    deleteAccount: (state, user: DeleteAccountActionType) => state,
  },
});

export const authActions = authSlice.actions;
export default authSlice.reducer;
