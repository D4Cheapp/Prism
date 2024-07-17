'use client';
import React, { useEffect, useState } from 'react';
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
import { RegistrationRequestType } from '@/src/types/authRequestTypes';

const RegistrationPage = (): React.ReactNode => {
  const [isConfirmCodeSent, setIsConfirmCodeSent] = useState<boolean>(false);
  const [registrationData, setRegistrationData] = useState<RegistrationRequestType>();
  const { setMessagesState, registration, confirmRegistration } = useActions();
  const requestStatus = useAppSelector(requestStatusSelector);
  const router = useRouter();

  const handleRegistrationClick = async (values: AuthFormType) => {
    let isError = false;
    await handleRegistrationValidate.validate(values).catch((error: Yup.ValidationError) => {
      isError = true;
      setMessagesState({ error: error.errors[0] });
    });
    if (!isError) {
      registration(values);
      setRegistrationData(values);
    }
  };

  const handleConfirmCodeClick = async (values: ConfirmCodeFormType) => {
    let isError = false;
    await Yup.object({ code: confirmCodeValidation })
      .validate(values)
      .catch((error: Yup.ValidationError) => {
        isError = true;
        setMessagesState({ error: error.errors[0] });
      });
    if (!isError) {
      confirmRegistration(values);
    }
  };

  const handleBackToRegistrationClick = () => {
    setIsConfirmCodeSent(false);
  };

  const handleResendCodeClick = () => {
    if (registrationData) {
      registration(registrationData);
    }
  };

  useEffect(() => {
    const { method, request, isOk } = requestStatus;
    const isRegistrationCodeSent = request === '/registration' && method === 'POST' && isOk;
    const isRegistrationSuccess = request === '/registration' && method === 'PATCH' && isOk;
    if (isRegistrationCodeSent) {
      setIsConfirmCodeSent(true);
    }
    if (isRegistrationSuccess) {
      router.push('/auth/login');
    }
  }, [requestStatus, router]);

  return (
    <>
      {isConfirmCodeSent ? (
        <ConfirmCodeForm
          title="Confirm your email"
          onBackClick={handleBackToRegistrationClick}
          onFormSubmit={handleConfirmCodeClick}
          onResendButtonClick={handleResendCodeClick}
        />
      ) : (
        <Auth
          title="Registration"
          redirectTo="/auth/login"
          redirectText="Already have an account?"
          onFormSubmitClick={handleRegistrationClick}
          withPassword
          withConfirmPassword
        />
      )}
    </>
  );
};

const registrationValidation = {
  email: Yup.string().email("Email isn't valid").required('Email is required'),
  password: passwordValidationSchema.required('Password is required'),
  confirmPassword: confirmPasswordValidationSchema,
};

const handleRegistrationValidate = Yup.object({
  ...registrationValidation,
});

export default RegistrationPage;
