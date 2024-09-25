'use client';
import { Form, Formik } from 'formik';
import cn from 'classnames';
import Link from 'next/link';
import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import ChangeThemeIcon from '@/src/ui/ChangeThemeIcon';
import CustomInput from '@/src/ui/CustomInput';
import CustomInputButton from '@/src/ui/CustomInputButton';
import { HideButton } from '@/src/ui/HideButton';
import CustomButton from '@/src/ui/CustomButton';
import { AuthFormType } from '@/src/types/formTypes';
import EmailConfirmCodeTimer from '../../components/EmailConfirmCodeTimer';
import s from './AuthForm.module.scss';

interface Props {
  title: string;
  onFormSubmitClick: (values: AuthFormType) => Promise<void>;
  onResendButtonClick?: () => void;
  onGoBackClick?: () => void;
  redirectTo?: string;
  redirectText?: string;
  withEmail?: boolean;
  withPassword?: boolean;
  withConfirmPassword?: boolean;
  withRestorePassword?: boolean;
  withGoBackButton?: boolean;
  withConfirmCode?: boolean;
  withDeveloperCheckbox?: boolean;
}

const AuthForm = ({
  redirectTo,
  redirectText,
  title,
  onFormSubmitClick,
  onResendButtonClick,
  onGoBackClick,
  withEmail,
  withPassword,
  withConfirmPassword,
  withRestorePassword,
  withGoBackButton,
  withConfirmCode,
  withDeveloperCheckbox,
}: Props): React.ReactNode => {
  const [isPasswordHidden, setIsPasswordHidden] = useState(true);
  const [isConfirmPasswordHidden, setIsConfirmPasswordHidden] = useState(true);
  const router = useRouter();

  const handleForgotPasswordClick = () => {
    router.push('/auth/restore-password');
  };

  const handleGoBackClick = () => {
    if (onGoBackClick) {
      onGoBackClick();
    } else {
      router.back();
    }
  };

  return (
    <section className={s.root}>
      <div className={s.changeThemeContainer}>
        <ChangeThemeIcon size={40} />
      </div>
      <Formik
        initialValues={
          {
            email: '',
            password: '',
            confirmPassword: '',
            code: '',
            isDeveloper: false,
          } as AuthFormType
        }
        onSubmit={onFormSubmitClick}
      >
        <Form className={s.form}>
          <h1 className={s.formTitle}>{title}</h1>
          {withConfirmCode && (
            <div className={s.inputContainer}>
              <CustomInput name="code" placeholder="Enter code" isFormInput label="Confirm code" />
              <EmailConfirmCodeTimer onResendButtonClick={onResendButtonClick} />
            </div>
          )}
          {withEmail && (
            <CustomInput name="email" isFormInput label="Email" placeholder="Enter your email" />
          )}
          {withPassword && (
            <div className={s.inputContainer}>
              <CustomInput
                name="password"
                type={isPasswordHidden ? 'password' : 'text'}
                isFormInput
                label="Password"
                placeholder="Enter your password"
                classNames={{ input: s.input }}
              />
              <HideButton
                styles={s.eye}
                isHide={isPasswordHidden}
                toggleHide={setIsPasswordHidden}
              />
              {withRestorePassword && (
                <button
                  type="button"
                  className={s.forgotPassword}
                  onClick={handleForgotPasswordClick}
                >
                  Forgot your password?
                </button>
              )}
            </div>
          )}
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
              {withDeveloperCheckbox && (
                <label className={s.checkboxContainer}>
                  <p className={s.checkboxLabel}>Is developer</p>
                  <CustomInputButton
                    name="isDeveloper"
                    type="checkbox"
                    isFormInput
                    className={s.customCheckbox}
                  />
                </label>
              )}
            </div>
          )}
          <div className={cn(s.buttonsContainer, { [s.multiButtonsContainer]: withGoBackButton })}>
            <CustomButton
              text="Submit"
              type="submit"
              withLoader
              styles={withGoBackButton ? s.smallButton : ''}
            />
            {redirectTo && redirectText && (
              <Link className={s.redirect} href={redirectTo}>
                {redirectText}
              </Link>
            )}
            {withGoBackButton && (
              <CustomButton
                styles={cn(s.smallButton, s.goBackButton)}
                text="Go back"
                onClick={handleGoBackClick}
              />
            )}
          </div>
        </Form>
      </Formik>
    </section>
  );
};

export default AuthForm;
