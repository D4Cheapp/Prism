'use client';
import React, { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { useActions, useAppSelector } from '@/src/hooks/reduxHooks';
import { currentUserSelector } from '@/src/reduxjs/auth/selectors';
import { requestStatusSelector } from '@/src/reduxjs/base/selectors';
import LoadingScreen from '../LoadingScreen';

interface Props {
  permission?: 'developer' | 'user';
  children: React.ReactElement;
}

const UserVerification = ({ children, permission = 'user' }: Props) => {
  const [isVerified, setIsVerified] = useState<boolean>(false);
  const currentUser = useAppSelector(currentUserSelector);
  const requestStatus = useAppSelector(requestStatusSelector);
  const { getCurrentUser } = useActions();
  const router = useRouter();

  useEffect(() => {
    getCurrentUser();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    const isResponseFinished = requestStatus.method === 'GET' && requestStatus.request === '/user';
    if (isResponseFinished) {
      const isErrorResponse = !requestStatus.isOk;
      if (isErrorResponse) {
        return router.push('/auth/login');
      }
      //@ts-ignore
      const isPermissionDenied = !currentUser.isDeveloper && permission === 'developer';
      if (isPermissionDenied) {
        return router.push('/');
      }
      setIsVerified(true);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [currentUser]);

  return <>{isVerified ? children : <LoadingScreen />}</>;
};

export default UserVerification;
