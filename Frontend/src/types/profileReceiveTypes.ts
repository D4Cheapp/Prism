export type CurrentUserProfileType = {
  tag: string;
  email: string;
  name: string;
  phoneNumber: string | undefined;
  status: string | undefined;
  relationWithCurrentProfile: {
    relationToUser: string;
    relationFromUser: string;
  };
  profilePicture: string | undefined;
  lastOnlineTime: number;
  online: boolean;
} | null;
export type ShortProfileInfoType = {
  tag: string;
  name: string;
  lastOnlineTime: number;
  profilePicture: string | undefined;
  online: boolean;
  error?: string;
} | null;
export type SelectedCategoryType = 'friends' | 'chats' | 'groups';
