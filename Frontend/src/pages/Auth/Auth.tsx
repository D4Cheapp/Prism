'use client';
import { Form, Formik } from 'formik';
import Link from 'next/link';
import React, { useState } from 'react';
import ChangeThemeIcon from '@/src/ui/ChangeThemeIcon';
import CustomInput from '@/src/ui/CustomInput';
import CustomInputButton from '@/src/ui/CustomInputButton';
import { HideButton } from '@/src/ui/HideButton';
import CustomButton from '@/src/ui/CustomButton';
import { AuthFormType } from '@/src/types/formTypes';
import s from './Auth.module.scss';

interface Props {
  redirectTo: string;
  redirectText: string;
  title: string;
  onFormSubmitClick: (values: AuthFormType) => Promise<void>;
  withConfirmPassword?: boolean;
}

const Auth = ({
  redirectTo,
  redirectText,
  title,
  onFormSubmitClick,
  withConfirmPassword,
}: Props): React.ReactNode => {
  const [isPasswordHidden, setIsPasswordHidden] = useState(true);
  const [isConfirmPasswordHidden, setIsConfirmPasswordHidden] = useState(true);

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
          <div className={s.inputContainer}>
            <CustomInput
              name="password"
              type={isPasswordHidden ? 'password' : 'text'}
              isFormInput
              label="Password"
              placeholder="Enter your password"
              classNames={{ input: s.input }}
            />
            <HideButton styles={s.eye} isHide={isPasswordHidden} toggleHide={setIsPasswordHidden} />
          </div>
          {withConfirmPassword && (
            <div className={s.additionalData}>
              <div className={s.inputContainer}>
                <CustomInput
                  name="confirmPassword"
                  type={isConfirmPasswordHidden ? 'password' : 'text'}
                  isFormInput
                  label="Confirm password"
                  placeholder="Confirm your password"
                  classNames={{ input: s.input }}
                />
                <HideButton
                  styles={s.eye}
                  isHide={isConfirmPasswordHidden}
                  toggleHide={setIsConfirmPasswordHidden}
                />
              </div>
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
            <CustomButton text="Submit" type="submit" withLoader />
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
