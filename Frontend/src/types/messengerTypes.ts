export type CurrentUserProfileType = {
  tag: string;
  email: string;
  name: string;
  phoneNumber: string;
  status: string;
  relationWithCurrentProfile: {
    relationToUser: string;
    relationFromUser: string;
  };
  profilePicture: string;
  lastOnlineTime: 0;
  online: true;
};
export type SelectedCategoryType = 'friends' | 'chats' | 'groups';
