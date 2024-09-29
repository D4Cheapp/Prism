/* eslint @typescript-eslint/no-unused-vars: 0 */
import { createSlice } from '@reduxjs/toolkit';
import { ReceiveProfileListModel } from '@/src/types/relationReceiveTypes';
import { ProfileTagActionType } from '../profile/types';
import { ReceiveProfileListActionType, RequestPaginationListActionType } from './types';

interface SliceInterface {
  sentFriendRequests: ReceiveProfileListModel | null | undefined;
  friendRequests: ReceiveProfileListModel | null | undefined;
  friends: ReceiveProfileListModel | null | undefined;
  blockedUsers: ReceiveProfileListModel | null | undefined;
}

const relationSlice = createSlice({
  name: 'relationSlice',
  initialState: {
    sentFriendRequests: undefined,
    friendRequests: undefined,
    friends: undefined,
    blockedUsers: undefined,
  } as SliceInterface,
  reducers: {
    unblockUser: (state, action: ProfileTagActionType) => state,

    addFriend: (state, action: ProfileTagActionType) => state,

    deleteFriend: (state, action: ProfileTagActionType) => state,

    declineFriendRequest: (state, action: ProfileTagActionType) => state,

    blockUser: (state, action: ProfileTagActionType) => state,

    getSentFriendRequestList: (state, action: RequestPaginationListActionType) => state,

    getFriendRequestList: (state, action: RequestPaginationListActionType) => state,

    getFriendList: (state, action: RequestPaginationListActionType) => state,

    getBlockList: (state, action: RequestPaginationListActionType) => state,

    setSentFriendRequestList: (state, action: ReceiveProfileListActionType) => {
      state.sentFriendRequests = action.payload ?? null;
    },

    setFriendRequestList: (state, action: ReceiveProfileListActionType) => {
      state.friendRequests = action.payload ?? null;
    },

    setFriendList: (state, action: ReceiveProfileListActionType) => {
      state.friends = action.payload ?? null;
    },

    setBlockList: (state, action: ReceiveProfileListActionType) => {
      state.blockedUsers = action.payload ?? null;
    },
  },
});

export const relationActions = relationSlice.actions;
export default relationSlice.reducer;
