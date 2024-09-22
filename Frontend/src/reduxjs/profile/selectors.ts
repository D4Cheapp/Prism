import { createSelector } from '@reduxjs/toolkit';
import { RootStateType } from '..';

const selector = (state: RootStateType) => state.profile;
export const currentUserProfileSelector = createSelector(selector, (state) => state.currentProfile);
