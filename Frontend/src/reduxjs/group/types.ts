import { PayloadAction } from '@reduxjs/toolkit';
import { FileListReceiveType, PaginationListReceiveType } from '@/src/types/receiveTypes';
import { GroupReceiveType } from '@/src/types/groupReceiveTypes';

export type GroupIdPayload = {
  groupId: string;
};
export type GroupMemberPayload = GroupIdPayload & {
  profileTag: string;
};
export type CreateGroupActionType = PayloadAction<{
  groupName: string;
  groupMemberTags: string[];
}>;
export type DeleteGroupActionType = PayloadAction<GroupIdPayload>;
export type GroupMemberActionType = PayloadAction<GroupMemberPayload>;
export type EditGroupPhotoActionType = PayloadAction<
  GroupIdPayload & {
    groupPhoto: File;
  }
>;
export type EditGroupNameActionType = PayloadAction<
  GroupIdPayload & {
    groupName: string;
  }
>;
export type EditGroupDescriptionActionType = PayloadAction<
  GroupIdPayload & {
    groupDescription: string;
  }
>;
export type GetGroupListActionType = PayloadAction<PaginationListReceiveType>;
export type GetGroupFilesActionType = PayloadAction<GroupIdPayload & PaginationListReceiveType>;

export type SetGroupListActionType = PayloadAction<GroupReceiveType[] | undefined>;
export type SetGroupFilesActionType = PayloadAction<FileListReceiveType | undefined>;
