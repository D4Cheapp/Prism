/* eslint @typescript-eslint/no-unused-vars: 0 */
import { createSlice } from '@reduxjs/toolkit';
import { CurrentUserProfileType } from '@/src/types/profileReceiveTypes';
import {
  ProfileTagActionType,
  SetChangedProfileInfoActionType,
  SetCurrentUserProfileActionType,
  SetProfileDataActionType,
  SetProfileTagActionType,
} from './types';

interface SliceInterface {
  currentProfile: CurrentUserProfileType | null | undefined;
}

const profileSlice = createSlice({
  name: 'profileSlice',
  initialState: { currentProfile: null } as SliceInterface,
  reducers: {
    unblockUser: (state, tag: ProfileTagActionType) => state,

    getCurrentUserProfile: (state) => state,

    setCurrentUserProfile: (state, profile: SetCurrentUserProfileActionType) => {
      if (profile) {
        state.currentProfile = profile.payload;
      } else {
        state.currentProfile = undefined;
      }
      return state;
    },

    setProfileBio: (state, bio: SetProfileDataActionType) => state,

    setProfileNickName: (state, nickName: SetProfileDataActionType) => state,

    setProfileTag: (state, tag: SetProfileTagActionType) => state,

    setProfilePhoneNumber: (state, phoneNumber: SetProfileDataActionType) => state,

    setChangedProfileInfo: (state, changedProfileInfo: SetChangedProfileInfoActionType) => {
      const { property, value } = changedProfileInfo.payload;
      const isCurrentProfileExists = !!state.currentProfile;
      if (isCurrentProfileExists) {
        //@ts-ignore
        state.currentProfile[property] = value;
      }
      return state;
    },
  },
});

export const profileActions = profileSlice.actions;
export default profileSlice.reducer;
