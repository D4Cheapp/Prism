import React, { Dispatch, SetStateAction, useRef, useState } from 'react';
import * as Yup from 'yup';
import { passwordValidationSchema } from '@/src/utils/formValidationSchemas';
import { useActions } from '@/src/hooks/reduxHooks';
import CustomInput from '@/src/ui/CustomInput';
import { HideButton } from '@/src/ui/HideButton';
import ModalWindow from '@/src/components/ModalWindow';
import { UserReceiveType } from '@/src/types/authReceiveTypes';
import s from './ChangePasswordWindow.module.scss';

interface Props {
  setIsChangePasswordOpen: Dispatch<SetStateAction<boolean>>;
  currentUser?: UserReceiveType;
}

const ChangePasswordWindow = ({
  setIsChangePasswordOpen,
  currentUser,
}: Props): React.ReactElement => {
  const oldPasswordRef = useRef<HTMLInputElement>(null);
  const newPasswordRef = useRef<HTMLInputElement>(null);
  const [isOldPasswordHidden, setIsOldPasswordHidden] = useState(true);
  const [isNewPasswordHidden, setIsNewPasswordHidden] = useState(true);
  const { changePassword, setMessagesState } = useActions();

  const handleConfirmChangePasswordClick = async () => {
    const oldPassword = oldPasswordRef.current?.value;
    const newPassword = newPasswordRef.current?.value;
    let isError = false;
    await Yup.object({
      oldPassword: Yup.string().required('Old password is required'),
      newPassword: passwordValidationSchema.required('New password is required'),
    })
      .validate({ oldPassword, newPassword })
      .catch((error: Yup.ValidationError) => {
        setMessagesState({ error: 'Error: ' + error.errors[0] });
        isError = true;
      });
    if (!isError) {
      //@ts-ignore
      changePassword({ id: currentUser?.id, oldPassword, newPassword });
    }
  };

  return (
    <ModalWindow
      setIsActive={setIsChangePasswordOpen}
      title="Change password"
      onConfirmClick={handleConfirmChangePasswordClick}
      buttonInfo={{ withConfirmButton: true }}
    >
      <div className={s.passwordSection}>
        <div className={s.inputContainer}>
          <CustomInput
            name="oldPassword"
            label="Old password"
            placeholder="Enter old password"
            type={isOldPasswordHidden ? 'password' : 'text'}
            reference={oldPasswordRef}
          />
          <HideButton
            isHide={isOldPasswordHidden}
            toggleHide={setIsOldPasswordHidden}
            styles={s.eye}
          />
        </div>
        <div className={s.inputContainer}>
          <CustomInput
            name="newPassword"
            label="New password"
            placeholder="Enter new password"
            type={isNewPasswordHidden ? 'password' : 'text'}
            reference={newPasswordRef}
          />
          <HideButton
            isHide={isNewPasswordHidden}
            toggleHide={setIsNewPasswordHidden}
            styles={s.eye}
          />
        </div>
      </div>
    </ModalWindow>
  );
};

export default ChangePasswordWindow;
