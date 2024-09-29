/* eslint @typescript-eslint/no-unused-vars: 0 */
import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { GroupReceiveType } from '@/src/types/groupReceiveTypes';
import { FileListReceiveType } from '@/src/types/receiveTypes';
import {
  CreateGroupActionType,
  DeleteGroupActionType,
  EditGroupPhotoActionType,
  EditGroupNameActionType,
  EditGroupDescriptionActionType,
  GetGroupListActionType,
  GetGroupFilesActionType,
  GroupMemberActionType,
  SetGroupListActionType,
  SetGroupFilesActionType,
} from './types';

interface SliceInterface {
  groups: GroupReceiveType[];
  currentGroup: GroupReceiveType | null | undefined;
  groupFiles: FileListReceiveType | null | undefined;
}

const groupSlice = createSlice({
  name: 'groupSlice',
  initialState: {
    groups: [],
    currentGroup: undefined,
    groupFiles: undefined,
  } as SliceInterface,
  reducers: {
    createGroup: (state, action: CreateGroupActionType) => state,

    deleteGroup: (state, action: DeleteGroupActionType) => state,

    addMemberToGroup: (state, action: GroupMemberActionType) => state,

    deleteMemberFromGroup: (state, action: GroupMemberActionType) => state,

    addGroupAdmin: (state, action: GroupMemberActionType) => state,

    deleteGroupAdmin: (state, action: GroupMemberActionType) => state,

    editGroupPhoto: (state, action: EditGroupPhotoActionType) => state,

    editGroupName: (state, action: EditGroupNameActionType) => state,

    editGroupDescription: (state, action: EditGroupDescriptionActionType) => state,

    getGroupList: (state, action: GetGroupListActionType) => state,

    getGroupFiles: (state, action: GetGroupFilesActionType) => state,

    setCreatedGroup: (state, action: PayloadAction<GroupReceiveType | undefined>) => {
      if (action.payload) {
        state.currentGroup = action.payload;
        state.groups.push(action.payload);
      }
    },

    setUpdatedGroup: (state, action: PayloadAction<GroupReceiveType | undefined>) => {
      if (action.payload) {
        state.currentGroup = action.payload;
        const index = state.groups.findIndex((group) => group.groupId === action.payload?.groupId);
        if (index !== -1) {
          state.groups[index] = action.payload;
        }
      }
    },

    setGroupList: (state, action: SetGroupListActionType) => {
      state.groups = action.payload ?? [];
    },

    setGroupFiles: (state, action: SetGroupFilesActionType) => {
      state.groupFiles = action.payload ?? null;
    },
  },
});

export const groupActions = groupSlice.actions;
export default groupSlice.reducer;
