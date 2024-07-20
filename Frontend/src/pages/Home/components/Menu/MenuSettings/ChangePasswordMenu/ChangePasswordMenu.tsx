import React, { useState } from 'react';
import CustomInput from '@/src/ui/CustomInput';
import { HideButton } from '@/src/ui/HideButton';
import s from './ChangePasswordMenu.module.scss';

interface Props {
  oldPasswordRef: React.RefObject<HTMLInputElement>;
  newPasswordRef: React.RefObject<HTMLInputElement>;
}

const ChangePasswordMenu = ({ oldPasswordRef, newPasswordRef }: Props): React.ReactElement => {
  const [isOldPasswordHidden, setIsOldPasswordHidden] = useState(true);
  const [isNewPasswordHidden, setIsNewPasswordHidden] = useState(true);

  return (
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
  );
};

export default ChangePasswordMenu;
