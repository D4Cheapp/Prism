import { PayloadAction } from '@reduxjs/toolkit';
import { CurrentUserProfileType } from '@/src/types/profileReceiveTypes';

export type ProfileTagActionType = PayloadAction<{
  tag: string;
}>;
export type SetCurrentUserProfileActionType = PayloadAction<CurrentUserProfileType | null>;
export type SetProfileDataActionType = PayloadAction<{ property: string }>;
export type SetChangedProfileInfoActionType = PayloadAction<{
  property: keyof NonNullable<CurrentUserProfileType>;
  value: string | number;
}>;
export type SetProfileTagActionType = PayloadAction<{ tag: string }>;
