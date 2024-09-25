import { createSelector } from '@reduxjs/toolkit';
import { RootStateType } from '..';

const selector = (state: RootStateType) => state.auth;
export const currentUserSelector = createSelector(selector, (state) => state.currentUser);
