import { PayloadAction } from '@reduxjs/toolkit';
import { UserReceiveType } from '@/src/types/authReceiveTypes';
import {
  ChangePasswordType,
  LoginRequestType,
  RegistrationRequestType,
} from '@/src/types/authRequestTypes';
import { AuthFormType } from '@/src/types/formTypes';

export type SetCurrentUserActionType = PayloadAction<UserReceiveType | null>;
export type LoginActionType = PayloadAction<LoginRequestType>;
export type RegistrationActionType = PayloadAction<RegistrationRequestType>;
export type ConfirmCodeActionType = PayloadAction<{ code: string }>;
export type RestorePasswordActionType = PayloadAction<{ email: string }>;
export type ConfirmRestorePasswordActionType = PayloadAction<AuthFormType>;
export type DeleteAccountActionType = PayloadAction<{ userId: number }>;
export type ChangePasswordActionType = PayloadAction<ChangePasswordType>;
