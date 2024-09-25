import { createSelector } from '@reduxjs/toolkit';
import { RootStateType } from '..';

const selector = (state: RootStateType) => state.base;
export const messageSelector = createSelector(selector, (state) => state.messages);
export const requestStatusSelector = createSelector(selector, (state) => state.requestStatus);
export const loadingSelector = createSelector(selector, (state) => state.loadingState);
