import { PayloadAction } from '@reduxjs/toolkit';
import { UserReceiveType } from '@/src/types/authRecieveTypes';
import { LoginRequestType, RegistrationRequestType } from '@/src/types/authRequestTypes';

export type SetCurrentUserActionType = PayloadAction<UserReceiveType | null>;
export type LoginActionType = PayloadAction<LoginRequestType>;
export type RegistrationActionType = PayloadAction<RegistrationRequestType>;
