import React, { Dispatch, SetStateAction, useEffect, useRef, useState } from 'react';
import * as Yup from 'yup';
import { useSelector } from 'react-redux';
import { useRouter } from 'next/navigation';
import { useActions } from '@/src/hooks/reduxHooks';
import ModalWindow from '@/src/components/ModalWindow';
import CustomInput from '@/src/ui/CustomInput';
import { requestStatusSelector } from '@/src/reduxjs/base/selectors';
import EmailConfirmCodeTimer from '@/src/components/EmailConfirmCodeTimer';
import { UserReceiveType } from '@/src/types/authReceiveTypes';
import s from './ChangeEmailWindow.module.scss';

interface Props {
  currentUser?: UserReceiveType;
  setIsChangeEmailOpen: Dispatch<SetStateAction<boolean>>;
}

const ChangeEmailWindow = ({ currentUser, setIsChangeEmailOpen }: Props): React.ReactElement => {
  const [isConfirmMailSent, setIsConfirmMailSent] = useState<boolean>(false);
  const { sendConfirmToChangeEmail, changeEmail, setMessagesState } = useActions();
  const requestStatus = useSelector(requestStatusSelector);
  const emailRef = useRef<HTMLInputElement>(null);
  const confirmCode = useRef<HTMLInputElement>(null);
  const router = useRouter();

  const handleSendConfirmCodeClick = async () => {
    const email = emailRef.current?.value;
    let isError = false;
    await Yup.object({
      email: Yup.string().email('Email is invalid').required('Email is required'),
    })
      .validate({ email })
      .catch((error: Yup.ValidationError) => {
        setMessagesState({ error: error.errors[0] });
        isError = true;
      });
    if (!isError) {
      //@ts-ignore
      sendConfirmToChangeEmail({ email, id: currentUser?.id });
    }
  };

  const handleConfirmCodeClick = async () => {
    const code = confirmCode.current?.value;
    let isError = false;
    await Yup.object({
      code: Yup.string()
        .required('Code is required')
        .min(4, 'Code is invalid')
        .max(4, 'Code is invalid'),
    })
      .validate({ code })
      .catch((error: Yup.ValidationError) => {
        setMessagesState({ error: 'Error: ' + error.errors[0] });
        isError = true;
      });
    if (!isError) {
      //@ts-ignore
      changeEmail({ code });
    }
  };

  const handleResendCodeClick = () => setIsConfirmMailSent(false);

  useEffect(() => {
    const { isOk, method, request } = requestStatus;
    const isConfirmMailSended = isOk && method === 'POST' && request === '/email';
    const isEmailChanged = isOk && method === 'PATCH' && request === '/email';
    if (isConfirmMailSended) {
      //@ts-ignore
      emailRef.current.value = '';
      setIsConfirmMailSent(true);
    }
    if (isEmailChanged) {
      router.push('/auth/login');
    }
  }, [requestStatus, router, setIsChangeEmailOpen]);

  return (
    <ModalWindow
      setIsActive={setIsChangeEmailOpen}
      title={isConfirmMailSent ? 'Enter confirm code' : 'Change email'}
      onConfirmClick={isConfirmMailSent ? handleConfirmCodeClick : handleSendConfirmCodeClick}
      buttonInfo={{ withConfirmButton: true }}
    >
      <div className={s.emailSection}>
        {isConfirmMailSent ? (
          <>
            <CustomInput
              name="code"
              placeholder="Enter code"
              defaultValue=""
              reference={confirmCode}
            />
            <EmailConfirmCodeTimer onResendButtonClick={handleResendCodeClick} />
          </>
        ) : (
          <CustomInput
            name="email"
            type="email"
            reference={emailRef}
            placeholder="Enter new email"
            classNames={{ input: s.email }}
          />
        )}
      </div>
    </ModalWindow>
  );
};

export default ChangeEmailWindow;
