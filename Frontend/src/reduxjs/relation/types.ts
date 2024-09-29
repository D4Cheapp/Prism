import { PayloadAction } from '@reduxjs/toolkit';
import { PaginationListReceiveType } from '@/src/types/receiveTypes';
import { ReceiveProfileListModel } from '@/src/types/relationReceiveTypes';

export type RequestPaginationListActionType = PayloadAction<PaginationListReceiveType>;
export type ReceiveProfileListActionType = PayloadAction<ReceiveProfileListModel | undefined>;
