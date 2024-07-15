'use client';
import { Form, Formik } from 'formik';
import Link from 'next/link';
import React, { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useAppSelector } from '@/src/hooks/reduxHooks';
import { currentUserSelector } from '@/src/reduxjs/auth/selectors';
import ChangeThemeIcon from '@/src/ui/ChangeThemeIcon';
import CustomInput from '@/src/ui/CustomInput';
import CustomInputButton from '@/src/ui/CustomInputButton';
import { AuthFormType } from '@/src/types/formTypes';
import s from './Auth.module.scss';

interface Props {
  redirectTo: string;
  redirectText: string;
  title: string;
  onFormSubmitClick: (values: AuthFormType) => Promise<void>;
  withAdditionalData?: boolean;
}

const Auth = ({
  redirectTo,
  redirectText,
  title,
  onFormSubmitClick,
  withAdditionalData,
}: Props): React.ReactNode => {
  const currentUser = useAppSelector(currentUserSelector);
  const router = useRouter();

  useEffect(() => {
    if (currentUser?.id) {
      router.push('/');
    }
  }, [currentUser, router]);

  return (
    <section className={s.root}>
      <div className={s.changeThemeContainer}>
        <ChangeThemeIcon />
      </div>
      <Formik
        initialValues={
          {
            email: '',
            password: '',
            confirmPassword: '',
            isDeveloper: false,
          } as AuthFormType
        }
        onSubmit={onFormSubmitClick}
      >
        <Form className={s.form}>
          <h1 className={s.formTitle}>{title}</h1>
          <CustomInput name="email" isFormInput label="Email" placeholder="Enter your email" />
          <CustomInput
            name="password"
            type="password"
            isFormInput
            label="Password"
            placeholder="Enter your password"
          />
          {withAdditionalData && (
            <div className={s.additionalData}>
              <CustomInput
                name="confirmPassword"
                type="password"
                isFormInput
                label="Confirm password"
                placeholder="Confirm your password"
              />
              <label className={s.checkboxContainer}>
                <p className={s.checkboxLabel}>Is developer</p>
                <CustomInputButton
                  name="isDeveloper"
                  type="checkbox"
                  isFormInput
                  className={s.customCheckbox}
                />
              </label>
            </div>
          )}
          <div className={s.buttonsContainer}>
            <button className={s.submitButton} type="submit">
              Submit
            </button>
            <Link className={s.redirect} href={redirectTo}>
              {redirectText}
            </Link>
          </div>
        </Form>
      </Formik>
    </section>
  );
};

export default Auth;
