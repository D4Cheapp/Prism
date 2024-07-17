'use client';
import React, { useEffect, useState } from 'react';
import * as Yup from 'yup';
import { useRouter } from 'next/navigation';
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
    await confirmCodeValidation.validate(values).catch((error: Yup.ValidationError) => {
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
      router.push('/login');
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
          redirectTo="/login"
          redirectText="Already have an account?"
          onFormSubmitClick={handleRegistrationClick}
          withConfirmPassword={true}
        />
      )}
    </>
  );
};

export const passwordSchema = Yup.string()
  .min(6, 'Password must be at least 6 characters')
  .max(25, 'Password must be less than 25 characters')
  .matches(/.*[A-ZА-Я].*/, 'Password must contain at least one uppercase letter')
  .matches(/.*[a-zа-я].*/, 'Password must contain at least one lowercase letter')
  .matches(/.*[0-9].*/, 'Password must contain at least one number')
  .matches(
    /.*[!\";#$%&'()*+,-./:<=>?@^_`{|}~].*/,
    'Password must contain at least one special character',
  );

const registrationValidation = {
  email: Yup.string().email("Email isn't valid").required('Email is required'),
  password: passwordSchema.required('Password is required'),
  confirmPassword: Yup.string()
    .required()
    .oneOf([Yup.ref('password')], 'Passwords must match'),
};

const confirmCodeValidation = Yup.object({
  code: Yup.string().min(4, 'Invalid code').max(4, 'Invalid code').required('Code is required'),
});

const handleRegistrationValidate = Yup.object({
  ...registrationValidation,
});

export default RegistrationPage;
