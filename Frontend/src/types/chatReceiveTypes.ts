import { ShortProfileInfoType } from './profileReceiveTypes';

export type ChatReceiveType = {
  chatId: string;
  conversationProfile: ShortProfileInfoType;
};

export type FileListReceiveType = {
  totalCount: number;
  files: string[];
};
