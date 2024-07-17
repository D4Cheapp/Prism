'use client';
import { useEffect } from 'react';
import * as Yup from 'yup';
import { useRouter } from 'next/navigation';
import { useActions, useAppSelector } from '@/src/hooks/reduxHooks';
import { requestStatusSelector } from '@/src/reduxjs/base/selectors';
import { currentUserSelector } from '@/src/reduxjs/auth/selectors';
import Auth from '@/src/pages/Auth';
import { AuthFormType } from '@/src/types/formTypes';

const LoginPage = () => {
  const requestStatus = useAppSelector(requestStatusSelector);
  const { setMessagesState, login } = useActions();
  const currentUser = useAppSelector(currentUserSelector);
  const router = useRouter();

  const handleLoginClick = async (values: AuthFormType) => {
    let isError = false;
    await handleLoginValidate.validate(values).catch((error: Yup.ValidationError) => {
      isError = true;
      setMessagesState({ error: error.errors[0] });
    });
    if (!isError) {
      login(values);
    }
  };

  useEffect(() => {
    const { method, request, isOk } = requestStatus;
    const isLoginRequestSuccess =
      method === 'POST' && request === '/login' && isOk && !!currentUser?.id;
    if (isLoginRequestSuccess) {
      router.push('/');
    }
  }, [currentUser, requestStatus, router]);

  return (
    <Auth
      title="Login"
      redirectTo="/registration"
      redirectText="Don't have an account?"
      onFormSubmitClick={handleLoginClick}
    />
  );
};

const handleLoginValidate = Yup.object({
  email: Yup.string().required('Email is required'),
  password: Yup.string().required('Password is required'),
});

export default LoginPage;
