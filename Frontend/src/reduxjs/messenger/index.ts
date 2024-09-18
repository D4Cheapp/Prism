/* eslint @typescript-eslint/no-unused-vars: 0 */
import { createSlice } from '@reduxjs/toolkit';
import { CurrentUserProfileType } from '@/src/types/messengerTypes';
import { SetCurrentUserProfileActionType } from './types';

interface SliceInterface {
  currentProfile: CurrentUserProfileType | null | undefined;
}

const messengerSlice = createSlice({
  name: 'messengerSlice',
  initialState: { currentProfile: null } as SliceInterface,
  reducers: {
    getCurrentUserProfile: (state) => state,

    setCurrentUserProfile: (state, profile: SetCurrentUserProfileActionType) => {
      if (profile) {
        state.currentProfile = profile.payload;
      } else {
        state.currentProfile = undefined;
      }
    },
  },
});

export const messengerActions = messengerSlice.actions;
export default messengerSlice.reducer;
