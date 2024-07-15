import { PayloadAction } from '@reduxjs/toolkit';

export type SetLoadingStateActionType = PayloadAction<boolean>;
export type SetErrorStateActionType = PayloadAction<string>;
export type DeleteErrorStateActionType = PayloadAction<number>;
