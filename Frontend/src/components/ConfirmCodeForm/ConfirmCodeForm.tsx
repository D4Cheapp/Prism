'use client';
import { useEffect, useState } from 'react';
import cn from 'classnames';
import { Form, Formik } from 'formik';
import CustomInput from '@/src/ui/CustomInput';
import { HideButton } from '@/src/ui/HideButton';
import ChangeThemeIcon from '@/src/ui/ChangeThemeIcon';
import CustomButton from '@/src/ui/CustomButton';
import { ConfirmCodeFormType } from '@/src/types/formTypes';
import s from './ConfirmCodeForm.module.scss';

interface Props {
  title: string;
  onBackClick: () => void;
  onFormSubmit: (values: ConfirmCodeFormType) => Promise<void>;
  onResendButtonClick: () => void;
  isConfirmPassword?: boolean;
}

const ConfirmCodeForm = ({
  title,
  onBackClick,
  onFormSubmit,
  onResendButtonClick,
  isConfirmPassword,
}: Props): React.ReactNode => {
  const [sendAgainBlockTimer, setSendAgainBlockTimer] = useState<number>(60);
  const [isPasswordHidden, setIsPasswordHidden] = useState<boolean>(true);
  const [isConfirmPasswordHidden, setIsConfirmPasswordHidden] = useState<boolean>(true);
  const timerSeconds = sendAgainBlockTimer % 60;
  const timerMinutes = Math.trunc(sendAgainBlockTimer / 60);

  const handleSendEmailAgainClick = () => {
    setSendAgainBlockTimer(60);
    onResendButtonClick();
  };

  useEffect(() => {
    const timer = setInterval(() => {
      if (sendAgainBlockTimer > 0) {
        setSendAgainBlockTimer((time) => time - 1);
      } else {
        () => clearInterval(timer);
      }
    }, 1000);
    return () => clearInterval(timer);
  }, [sendAgainBlockTimer]);

  return (
    <section className={s.root}>
      <div className={s.changeThemeContainer}>
        <ChangeThemeIcon />
      </div>
      <Formik
        initialValues={{ code: '', password: '', confirmPassword: '' } as ConfirmCodeFormType}
        onSubmit={onFormSubmit}
      >
        <Form className={s.form}>
          <h1 className={s.formTitle}>{title}</h1>
          <div className={s.inputContainer}>
            <CustomInput name="code" isFormInput placeholder="Enter code" />
            {sendAgainBlockTimer > 0 ? (
              <p className={s.resendText}>
                Please, wait {timerMinutes}:{timerSeconds < 10 ? `0${timerSeconds}` : timerSeconds}{' '}
                to resend confirm code
              </p>
            ) : (
              <button className={s.resendButton} type="button" onClick={handleSendEmailAgainClick}>
                <p className={s.resendButtonText}>Send confirm code again</p>
              </button>
            )}
          </div>
          {isConfirmPassword && (
            <>
              <div className={s.inputContainer}>
                <CustomInput
                  classNames={{ input: s.input }}
                  name="password"
                  type="password"
                  isFormInput
                  placeholder="Enter password"
                />
                <HideButton
                  styles={s.eye}
                  isHide={isPasswordHidden}
                  toggleHide={setIsPasswordHidden}
                />
              </div>
              <div className={s.inputContainer}>
                <CustomInput
                  classNames={{ input: s.input }}
                  name="confirmPassword"
                  type="password"
                  isFormInput
                  placeholder="Confirm password"
                />
                <HideButton
                  styles={s.eye}
                  isHide={isConfirmPasswordHidden}
                  toggleHide={setIsConfirmPasswordHidden}
                />
              </div>
            </>
          )}
          <div className={s.buttonsContainer}>
            <CustomButton styles={s.button} text="Confirm" type="submit" />
            <CustomButton
              styles={cn(s.button, s.goBackButton)}
              text="Go back"
              onClick={onBackClick}
            />
          </div>
        </Form>
      </Formik>
    </section>
  );
};

export default ConfirmCodeForm;
