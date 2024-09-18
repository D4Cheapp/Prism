import { PayloadAction } from '@reduxjs/toolkit';
import { CurrentUserProfileType } from '@/src/types/messengerTypes';

export type SetCurrentUserProfileActionType = PayloadAction<CurrentUserProfileType | null>;
