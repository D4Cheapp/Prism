'use client';
import { useEffect, useState } from 'react';
import * as Yup from 'yup';
import { useRouter } from 'next/navigation';
import {
  confirmCodeValidation,
  confirmPasswordValidationSchema,
  passwordValidationSchema,
} from '@/src/utils/formValidationSchemas';
import { useActions, useAppSelector } from '@/src/hooks/reduxHooks';
import ConfirmCodeForm from '@/src/components/ConfirmCodeForm';
import Auth from '@/src/pages/Auth';
import { requestStatusSelector } from '@/src/reduxjs/base/selectors';
import { AuthFormType, ConfirmCodeFormType } from '@/src/types/formTypes';

const RestorePassword = () => {
  const [isEmailSent, setIsEmailSent] = useState<boolean>(false);
  const [restoreEmail, setRestoreEmail] = useState<string>();
  const { setMessagesState, restorePassword, confirmRestorePassword } = useActions();
  const requestStatus = useAppSelector(requestStatusSelector);
  const router = useRouter();

  const handleGoBackClick = () => {
    router.push('/auth/login');
  };

  const handleConfirmRestorePasswordClick = async (values: ConfirmCodeFormType) => {
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
  };

  const handleResendCodeClick = () => {
    const isRestorePasswordExist = !!restoreEmail;
    if (isRestorePasswordExist) {
      restorePassword({ email: restoreEmail });
    }
  };

  const handleSendRestorePasswordClick = async (values: AuthFormType) => {
    let isError = false;
    await emailValidationSchema.validate(values).catch((error: Yup.ValidationError) => {
      isError = true;
      setMessagesState({ error: error.errors[0] });
    });
    if (!isError) {
      restorePassword({ email: values.email });
      setRestoreEmail(values.email);
    }
  };

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
        <ConfirmCodeForm
          onBackClick={handleGoBackClick}
          onFormSubmit={handleConfirmRestorePasswordClick}
          onResendButtonClick={handleResendCodeClick}
          title="Enter your new password"
          isConfirmPassword
        />
      ) : (
        <Auth
          onFormSubmitClick={handleSendRestorePasswordClick}
          title="Enter your email"
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
