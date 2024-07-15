'use client';
import React from 'react';
import * as Yup from 'yup';
import { useActions } from '@/src/hooks/reduxHooks';
import Auth from '@/src/pages/Auth/Auth';
import { AuthFormType } from '@/src/types/formTypes';

const RegistrationPage = (): React.ReactNode => {
  const { setErrorsState } = useActions();

  const handleRegistrationValidate = Yup.object({
    email: Yup.string().email("Email isn't valid").required('Email is required'),
    password: Yup.string()
      .min(6, 'Password must be at least 6 characters')
      .max(25, 'Password must be less than 25 characters')
      .matches(/.*[A-ZА-Я].*/, 'Password must contain at least one uppercase letter')
      .matches(/.*[a-zа-я].*/, 'Password must contain at least one lowercase letter')
      .matches(/.*[0-9].*/, 'Password must contain at least one number')
      .matches(
        /.*[!\";#$%&'()*+,-./:<=>?@^_`{|}~].*/,
        'Password must contain at least one special character',
      )
      .required('Password is required'),
    confirmPassword: Yup.string()
      .required()
      .oneOf([Yup.ref('password')], 'Passwords must match'),
  });

  const handleRegistrationClick = async (
    values: AuthFormType,
  ) => {
    let isError = false;
    await handleRegistrationValidate.validate(values).catch((error: Yup.ValidationError) => {
      isError = true;
      setErrorsState(error.errors[0]);
    });
    if (!isError) {
      console.log(values);
    }
  };

  return (
    <Auth
      title="Registration"
      redirectTo="/login"
      redirectText="Already have an account?"
      onFormSubmitClick={handleRegistrationClick}
      withAdditionalData={true}
    />
  );
};

export default RegistrationPage;
