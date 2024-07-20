import { Dispatch, SetStateAction, useCallback, useEffect, useRef, useState } from 'react';
import cn from 'classnames';
import * as Yup from 'yup';
import { useRouter } from 'next/navigation';
import { passwordValidationSchema } from '@/src/utils/formValidationSchemas';
import { useActions, useAppSelector } from '@/src/hooks/reduxHooks';
import ModalWindow from '@/src/components/ModalWindow';
import { currentUserSelector } from '@/src/reduxjs/auth/selectors';
import { requestStatusSelector } from '@/src/reduxjs/base/selectors';
import ChangePasswordMenu from './ChangePasswordMenu';
import s from './MenuSettings.module.scss';

interface Props {
  setIsSettingsOpen: Dispatch<SetStateAction<boolean>>;
  setTheme: () => void;
}

const MenuSettings = ({ setIsSettingsOpen, setTheme }: Props): React.ReactElement => {
  const [isLogoutConfirmOpen, setIsLogoutConfirmOpen] = useState(false);
  const [isDeleteAccountConfirmOpen, setIsDeleteAccountConfirmOpen] = useState(false);
  const [isChangePasswordOpen, setIsChangePasswordOpen] = useState(false);
  const oldPasswordRef = useRef<HTMLInputElement>(null);
  const newPasswordRef = useRef<HTMLInputElement>(null);
  const currentUser = useAppSelector(currentUserSelector);
  const requestStatus = useAppSelector(requestStatusSelector);
  const { logout, deleteAccount, changePassword, setMessagesState } = useActions();
  const router = useRouter();

  const handleLogoutClick = () => {
    setIsLogoutConfirmOpen(true);
  };

  const handleConfirmLogoutClick = () => {
    logout();
  };

  const handleChangePasswordClick = () => {
    setIsChangePasswordOpen(true);
  };

  const handleConfirmChangePasswordClick = useCallback(async () => {
    const oldPassword = oldPasswordRef.current?.value;
    const newPassword = newPasswordRef.current?.value;
    let isError = false;
    await Yup.object({
      oldPassword: Yup.string().required('Old password is required'),
      newPassword: passwordValidationSchema.required('New password is required'),
    })
      .validate({ oldPassword, newPassword })
      .catch((error: Yup.ValidationError) => {
        setMessagesState({ error: error.errors[0] });
        isError = true;
      });
    if (!isError) {
      //@ts-ignore
      changePassword({ id: currentUser?.id, oldPassword, newPassword });
    }
  }, [changePassword, currentUser?.id, setMessagesState]);

  const handleThemeChangeClick = () => {
    setTheme();
  };

  const handleDeleteAccount = () => {
    setIsDeleteAccountConfirmOpen(true);
  };

  const handleConfirmDeleteAccount = useCallback(() => {
    const isCurrentUserExist = currentUser?.id;
    if (isCurrentUserExist) {
      deleteAccount({ userId: currentUser?.id });
    }
  }, [currentUser?.id, deleteAccount]);

  useEffect(() => {
    const isLogoutStatusCompleted =
      requestStatus.isOk &&
      requestStatus.method === 'DELETE' &&
      requestStatus.request === '/logout';
    const isDeleteAccountStatusCompleted =
      requestStatus.isOk && requestStatus.method === 'DELETE' && requestStatus.request === '/user';
    const isChangePasswordStatusCompleted =
      requestStatus.isOk &&
      requestStatus.method === 'PATCH' &&
      requestStatus.request === '/password';
    if (isLogoutStatusCompleted || isDeleteAccountStatusCompleted) {
      router.push('/auth/login');
    }
    if (isChangePasswordStatusCompleted) {
      setIsChangePasswordOpen(false);
    }
  }, [requestStatus, router]);

  return (
    <ModalWindow setIsActive={setIsSettingsOpen} title={'Settings'}>
      <div className={s.root}>
        <div className={s.settingsSection}>
          <button className={s.settingsButton} onClick={handleChangePasswordClick}>
            Change password
          </button>
          {isChangePasswordOpen && (
            <ModalWindow
              setIsActive={setIsChangePasswordOpen}
              title="Change password"
              onConfirmClick={handleConfirmChangePasswordClick}
              buttonInfo={{ withConfirmButton: true }}
            >
              <ChangePasswordMenu oldPasswordRef={oldPasswordRef} newPasswordRef={newPasswordRef} />
            </ModalWindow>
          )}
          <button className={s.settingsButton} onClick={handleThemeChangeClick}>
            Change Theme
          </button>
        </div>
        <div className={s.settingsSection}>
          <button className={cn(s.settingsButton, s.dangerButton)} onClick={handleLogoutClick}>
            Logout
          </button>
          {isLogoutConfirmOpen && (
            <ModalWindow
              title="Are you sure you want to logout?"
              setIsActive={setIsLogoutConfirmOpen}
              onConfirmClick={handleConfirmLogoutClick}
              buttonInfo={{ withConfirmButton: true }}
            />
          )}
          <button className={cn(s.settingsButton, s.dangerButton)} onClick={handleDeleteAccount}>
            Delete account
          </button>
          {isDeleteAccountConfirmOpen && (
            <ModalWindow
              title="Are you sure you want to delete your account?"
              setIsActive={setIsDeleteAccountConfirmOpen}
              onConfirmClick={handleConfirmDeleteAccount}
              buttonInfo={{ withConfirmButton: true }}
            />
          )}
        </div>
      </div>
    </ModalWindow>
  );
};

export default MenuSettings;
