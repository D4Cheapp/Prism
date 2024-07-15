import { createSelector } from '@reduxjs/toolkit';
import { RootStateType } from '..';

const selector = (state: RootStateType) => state.base;
export const errorSelector = createSelector(selector, (state) => state.errors);
