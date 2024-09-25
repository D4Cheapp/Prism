'use client';
import { useCallback, useEffect, useState } from 'react';
import * as Yup from 'yup';
import { useRouter } from 'next/navigation';
import {
  confirmCodeValidation,
  confirmPasswordValidationSchema,
  passwordValidationSchema,
} from '@/src/utils/formValidationSchemas';
import { useActions, useAppSelector } from '@/src/hooks/reduxHooks';
import AuthForm from '@/src/pages/AuthForm';
import { requestStatusSelector } from '@/src/reduxjs/base/selectors';
import { AuthFormType } from '@/src/types/formTypes';

const RestorePassword = () => {
  const [isEmailSent, setIsEmailSent] = useState<boolean>(false);
  const [restoreEmail, setRestoreEmail] = useState<string>();
  const { setMessagesState, restorePassword, confirmRestorePassword } = useActions();
  const requestStatus = useAppSelector(requestStatusSelector);
  const router = useRouter();

  const handleGoBackClick = useCallback(() => {
    router.push('/auth/login');
  }, [router]);

  const handleConfirmRestorePasswordClick = useCallback(
    async (values: AuthFormType) => {
      let isError = false;
      await Yup.object({
        code: confirmCodeValidation,
        password: passwordValidationSchema.required('Password is required'),
        confirmPassword: confirmPasswordValidationSchema,
      })
        .validate(values)
        .catch((error: Yup.ValidationError) => {
          isError = true;
          setMessagesState({ error: error.errors[0] });
        });
      if (!isError) {
        confirmRestorePassword(values);
      }
    },
    [confirmRestorePassword, setMessagesState],
  );

  const handleResendCodeClick = useCallback(() => {
    const isRestorePasswordExist = !!restoreEmail;
    if (isRestorePasswordExist) {
      restorePassword({ email: restoreEmail });
    }
  }, [restoreEmail, restorePassword]);

  const handleSendRestorePasswordClick = useCallback(
    async (values: AuthFormType) => {
      let isError = false;
      await emailValidationSchema.validate(values).catch((error: Yup.ValidationError) => {
        isError = true;
        setMessagesState({ error: error.errors[0] });
      });
      if (!isError) {
        restorePassword({ email: values.email });
        setRestoreEmail(values.email);
      }
    },
    [restorePassword, setMessagesState],
  );

  useEffect(() => {
    const { method, request, isOk } = requestStatus;
    const isEmailSent = request === '/restore-password' && method === 'POST' && isOk;
    const isRestorePasswordConfirmed =
      request === '/restore-password' && method === 'PATCH' && isOk;
    if (isEmailSent) {
      setIsEmailSent(true);
    }
    if (isRestorePasswordConfirmed) {
      router.push('/auth/login');
    }
  }, [requestStatus, router]);

  return (
    <>
      {isEmailSent ? (
        <AuthForm
          onGoBackClick={handleGoBackClick}
          onFormSubmitClick={handleConfirmRestorePasswordClick}
          onResendButtonClick={handleResendCodeClick}
          title="Enter your new password"
          withConfirmCode
          withPassword
          withConfirmPassword
          withGoBackButton
        />
      ) : (
        <AuthForm
          onFormSubmitClick={handleSendRestorePasswordClick}
          title="Enter your email"
          withEmail
          withGoBackButton
        />
      )}
    </>
  );
};

const emailValidationSchema = Yup.object({
  email: Yup.string().email('Invalid email').required('Email is required'),
});

export default RestorePassword;
